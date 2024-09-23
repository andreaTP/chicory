package com.dylibso.chicory.function.processor;

import static com.github.javaparser.StaticJavaParser.parseType;
import static javax.tools.Diagnostic.Kind.ERROR;

import com.dylibso.chicory.function.annotations.Buffer;
import com.dylibso.chicory.function.annotations.CString;
import com.dylibso.chicory.function.annotations.HostModule;
import com.dylibso.chicory.function.annotations.WasmExport;
import com.dylibso.chicory.wasm.Parser;
import com.dylibso.chicory.wasm.types.ValueType;
import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

public final class HostModuleProcessor extends AbstractModuleProcessor {
    public HostModuleProcessor() {
        super(HostModule.class);
    }

    @Override
    protected void processModule(TypeElement type) {
        var annotation = type.getAnnotation(HostModule.class);
        var moduleName = annotation.name();
        var moduleFile = annotation.file();

        com.dylibso.chicory.wasm.Module module = null;

        if (!moduleFile.isEmpty()) {
            try {
                // TODO: FIXME for some reasons passing "moduleAndPkg" doesn't seem to work
                FileObject fileObject =
                        filer().getResource(StandardLocation.CLASS_OUTPUT, "", moduleFile);
                InputStream inputStream = fileObject.openInputStream();
                module = Parser.parse(inputStream);
            } catch (IOException e) {
                log(ERROR, "Couldn't load the wasm resource file: " + moduleFile, type);
                throw new AbortProcessingException();
            }

            // TODO: refactor and extract this validation logic
            // TODO: it should be possible to toggle off the checks
            Map<String, List<ValueType>> params = new HashMap<>();
            Map<String, List<ValueType>> returns = new HashMap<>();
            // now we can check that all the exports are implemented
            for (var exportIdx = 0; exportIdx < module.exportSection().exportCount(); exportIdx++) {
                var export = module.exportSection().getExport(exportIdx);
                var functionType =
                        module.typeSection()
                                .getType(module.functionSection().getFunctionType(export.index()));
                params.put(export.name(), functionType.params());
                returns.put(export.name(), functionType.returns());
            }

            for (Element member : elements().getAllMembers(type)) {
                if (member instanceof ExecutableElement
                        && annotatedWith(member, WasmExport.class)) {
                    var name = exportName((ExecutableElement) member);
                    var expectedParameters = params.get(name);
                    var receivedParameters = extractParameters((ExecutableElement) member);
                    checkType(name, expectedParameters, receivedParameters);

                    var expectedReturns = returns.get(name);
                    var receivedReturns = extractReturns((ExecutableElement) member);
                    if (expectedReturns.size() > 1) {
                        if (!((ExecutableElement) member)
                                .getReturnType()
                                .toString()
                                .equals("Value[]")) {
                            log(
                                    ERROR,
                                    "When the WASM module declares a function returning multiple"
                                            + " values, from JAva we need to fallback to Value[]",
                                    member);
                            throw new AbortProcessingException();
                        }
                    } else {
                        checkType(name, expectedReturns, receivedReturns);
                    }
                }
            }
        }

        var functions = new NodeList<Expression>();
        for (Element member : elements().getAllMembers(type)) {
            if (member instanceof ExecutableElement && annotatedWith(member, WasmExport.class)) {
                functions.add(processMethod((ExecutableElement) member, moduleName));
            }
        }

        var pkg = getPackageName(type);
        var cu = createCompilationUnit(pkg, type);
        cu.addImport("com.dylibso.chicory.runtime.HostFunction");
        cu.addImport("com.dylibso.chicory.runtime.Instance");
        cu.addImport("com.dylibso.chicory.wasm.types.Value");
        cu.addImport("com.dylibso.chicory.wasm.types.ValueType");
        cu.addImport("java.util.List");

        var typeName = type.getSimpleName().toString();
        var classDef = cu.addClass(typeName + "_ModuleFactory").setPublic(true).setFinal(true);
        addGeneratedAnnotation(classDef);

        classDef.addConstructor().setPrivate(true);

        var newHostFunctions =
                new ArrayCreationExpr(
                        parseType("HostFunction"),
                        new NodeList<>(new ArrayCreationLevel()),
                        new ArrayInitializerExpr(functions));

        classDef.addMethod("toHostFunctions")
                .setPublic(true)
                .setStatic(true)
                .addParameter(typeName, "functions")
                .setType("HostFunction[]")
                .setBody(new BlockStmt(new NodeList<>(new ReturnStmt(newHostFunctions))));

        writeSourceFile(cu, pkg, type, "_ModuleFactory");
    }

    private void checkType(String name, List<ValueType> expected, List<ValueType> received) {
        if (!expected.equals(received)) {
            throw new IllegalArgumentException(
                    String.format(
                            "Function type mismatch for '%s': expected %s <=> actual %s",
                            name, expected, received));
        }
    }

    private String exportName(ExecutableElement executable) {
        // compute function name
        var name = executable.getAnnotation(WasmExport.class).value();
        if (name.isEmpty()) {
            name = camelCaseToSnakeCase(executable.getSimpleName().toString());
        }
        return name;
    }

    private List<ValueType> extractReturns(ExecutableElement executable) {
        switch (executable.getReturnType().toString()) {
            case "int":
                return List.of(ValueType.I32);
            case "long":
                return List.of(ValueType.I64);
            case "float":
                return List.of(ValueType.F32);
            case "double":
                return List.of(ValueType.F64);
            case "java.lang.String":
                // TODO: FIXME
                log(ERROR, "java.lang.String not supported as a return type", executable);
                throw new AbortProcessingException();
            default:
                log(
                        ERROR,
                        "Cannot extract the Wasm Return type from "
                                + executable.getReturnType().toString(),
                        executable);
                throw new AbortProcessingException();
        }
    }

    private List<ValueType> extractParameters(ExecutableElement executable) {
        var params = new ArrayList<ValueType>();
        for (VariableElement parameter : executable.getParameters()) {
            // TODO: implement a fallback in case the argument is "Value[]"
            switch (parameter.asType().toString()) {
                case "int":
                    params.add(ValueType.I32);
                    break;
                case "long":
                    params.add(ValueType.I64);
                    break;
                case "float":
                    params.add(ValueType.F32);
                    break;
                case "double":
                    params.add(ValueType.F64);
                    break;
                case "java.lang.String":
                    if (annotatedWith(parameter, Buffer.class)) {
                        params.add(ValueType.I32);
                        params.add(ValueType.I32);
                    } else if (annotatedWith(parameter, CString.class)) {
                        params.add(ValueType.I32);
                    } else {
                        log(ERROR, "Missing annotation for WASM type: java.lang.String", parameter);
                        throw new AbortProcessingException();
                    }
                    break;
                default:
                    log(
                            ERROR,
                            "Cannot extract the Wasm Parameter type from "
                                    + parameter.asType().toString(),
                            parameter);
                    throw new AbortProcessingException();
            }
        }
        return params;
    }

    private Expression processMethod(ExecutableElement executable, String moduleName) {
        var name = exportName(executable);

        // compute parameter types and argument conversions
        NodeList<Expression> paramTypes = new NodeList<>();
        NodeList<Expression> arguments = new NodeList<>();
        for (VariableElement parameter : executable.getParameters()) {
            var argExpr = argExpr(paramTypes.size());
            switch (parameter.asType().toString()) {
                case "int":
                    paramTypes.add(valueType("I32"));
                    arguments.add(new MethodCallExpr(argExpr, "asInt"));
                    break;
                case "long":
                    paramTypes.add(valueType("I64"));
                    arguments.add(new MethodCallExpr(argExpr, "asLong"));
                    break;
                case "float":
                    paramTypes.add(valueType("F32"));
                    arguments.add(new MethodCallExpr(argExpr, "asFloat"));
                    break;
                case "double":
                    paramTypes.add(valueType("F64"));
                    arguments.add(new MethodCallExpr(argExpr, "asDouble"));
                    break;
                case "java.lang.String":
                    if (annotatedWith(parameter, Buffer.class)) {
                        var lenExpr = argExpr(paramTypes.size() + 1);
                        paramTypes.add(valueType("I32"));
                        paramTypes.add(valueType("I32"));
                        arguments.add(
                                new MethodCallExpr(
                                        new MethodCallExpr(new NameExpr("instance"), "memory"),
                                        "readString",
                                        new NodeList<>(
                                                new MethodCallExpr(argExpr, "asInt"),
                                                new MethodCallExpr(lenExpr, "asInt"))));
                    } else if (annotatedWith(parameter, CString.class)) {
                        paramTypes.add(valueType("I32"));
                        arguments.add(
                                new MethodCallExpr(
                                        new MethodCallExpr(new NameExpr("instance"), "memory"),
                                        "readCString",
                                        new NodeList<>(new MethodCallExpr(argExpr, "asInt"))));
                    } else {
                        log(ERROR, "Missing annotation for WASM type: java.lang.String", parameter);
                        throw new AbortProcessingException();
                    }
                    break;
                case "com.dylibso.chicory.runtime.Instance":
                    arguments.add(new NameExpr("instance"));
                    break;
                default:
                    log(ERROR, "Unsupported WASM type: " + parameter.asType(), parameter);
                    throw new AbortProcessingException();
            }
        }

        // compute return type and conversion
        String returnName = executable.getReturnType().toString();
        NodeList<Expression> returnType = new NodeList<>();
        String returnExpr = null;
        switch (returnName) {
            case "void":
                break;
            case "int":
                returnType.add(valueType("I32"));
                returnExpr = "i32";
                break;
            case "long":
                returnType.add(valueType("I64"));
                returnExpr = "i64";
                break;
            case "float":
                returnType.add(valueType("F32"));
                returnExpr = "fromFloat";
                break;
            case "double":
                returnType.add(valueType("F64"));
                returnExpr = "fromDouble";
                break;
            default:
                log(ERROR, "Unsupported WASM type: " + returnName, executable);
                throw new AbortProcessingException();
        }

        // function invocation
        Expression invocation =
                new MethodCallExpr(
                        new NameExpr("functions"),
                        executable.getSimpleName().toString(),
                        arguments);

        // convert return value
        BlockStmt handleBody = new BlockStmt();
        if (returnType.isEmpty()) {
            handleBody.addStatement(invocation).addStatement(new ReturnStmt(new NullLiteralExpr()));
        } else {
            var result = new VariableDeclarator(parseType(returnName), "result", invocation);
            var boxed =
                    new MethodCallExpr(
                            new NameExpr("Value"),
                            returnExpr,
                            new NodeList<>(new NameExpr("result")));
            var wrapped =
                    new ArrayCreationExpr(
                            parseType("Value"),
                            new NodeList<>(new ArrayCreationLevel()),
                            new ArrayInitializerExpr(new NodeList<>(boxed)));
            handleBody
                    .addStatement(new ExpressionStmt(new VariableDeclarationExpr(result)))
                    .addStatement(new ReturnStmt(wrapped));
        }

        // lambda for host function
        var handle =
                new LambdaExpr()
                        .addParameter("Instance", "instance")
                        .addParameter(new Parameter(parseType("Value"), "args").setVarArgs(true))
                        .setEnclosingParameters(true)
                        .setBody(handleBody);

        // create host function
        var function =
                new ObjectCreationExpr()
                        .setType("HostFunction")
                        .addArgument(handle)
                        .addArgument(new StringLiteralExpr(moduleName))
                        .addArgument(new StringLiteralExpr(name))
                        .addArgument(new MethodCallExpr(new NameExpr("List"), "of", paramTypes))
                        .addArgument(new MethodCallExpr(new NameExpr("List"), "of", returnType));
        function.setLineComment("");
        return function;
    }

    private static Expression argExpr(int n) {
        return new ArrayAccessExpr(new NameExpr("args"), new IntegerLiteralExpr(String.valueOf(n)));
    }
}
