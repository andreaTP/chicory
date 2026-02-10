/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm.types;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class CatchOpCode
extends Enum<CatchOpCode> {
    public static final /* enum */ CatchOpCode CATCH = new CatchOpCode(0);
    public static final /* enum */ CatchOpCode CATCH_REF = new CatchOpCode(1);
    public static final /* enum */ CatchOpCode CATCH_ALL = new CatchOpCode(2);
    public static final /* enum */ CatchOpCode CATCH_ALL_REF = new CatchOpCode(3);
    private static final int OP_CODES_SIZE = 4;
    private final int opcode;
    private static final /* synthetic */ CatchOpCode[] $VALUES;

    public static CatchOpCode[] values() {
        return (CatchOpCode[])$VALUES.clone();
    }

    public static CatchOpCode valueOf(String name) {
        return Enum.valueOf(CatchOpCode.class, name);
    }

    private CatchOpCode(int opcode) {
        this.opcode = opcode;
        CatchOpCodes.byOpCode[opcode] = this;
    }

    public int opcode() {
        return this.opcode;
    }

    public static CatchOpCode byOpCode(int opcode) {
        return CatchOpCodes.byOpCode[opcode];
    }

    public static List<Catch> decode(long[] operands) {
        long length = operands[1];
        ArrayList<Catch> result = new ArrayList<Catch>();
        block4: for (int i = 2; i < operands.length; ++i) {
            CatchOpCode catchEnum = CatchOpCode.byOpCode((int)operands[i++]);
            switch (catchEnum) {
                case CATCH: 
                case CATCH_REF: {
                    int tag = (int)operands[i++];
                    int label = (int)operands[i];
                    result.add(new Catch(catchEnum, tag, label));
                    continue block4;
                }
                case CATCH_ALL: 
                case CATCH_ALL_REF: {
                    int label = (int)operands[i];
                    result.add(new Catch(catchEnum, label));
                    continue block4;
                }
            }
        }
        assert ((long)result.size() == length);
        return result;
    }

    public static List<Integer> allLabels(long[] operands) {
        return CatchOpCode.decode(operands).stream().map(Catch::label).collect(Collectors.toList());
    }

    private static /* synthetic */ CatchOpCode[] $values() {
        return new CatchOpCode[]{CATCH, CATCH_REF, CATCH_ALL, CATCH_ALL_REF};
    }

    static {
        $VALUES = CatchOpCode.$values();
    }

    private static final class CatchOpCodes {
        private static final CatchOpCode[] byOpCode = new CatchOpCode[4];

        private CatchOpCodes() {
        }
    }

    public static final class Catch {
        private final CatchOpCode opcode;
        private final int tag;
        private final int label;
        private int resolvedLabel;

        private Catch(CatchOpCode opcode, int label) {
            this(opcode, -1, label);
            assert (opcode == CATCH_ALL || opcode == CATCH_ALL_REF);
        }

        private Catch(CatchOpCode opcode, int tag, int label) {
            assert (tag == -1 || opcode == CATCH || opcode == CATCH_REF);
            this.opcode = opcode;
            this.tag = tag;
            this.label = label;
        }

        public CatchOpCode opcode() {
            return this.opcode;
        }

        public int tag() {
            return this.tag;
        }

        public int label() {
            return this.label;
        }

        public void resolvedLabel(int label) {
            this.resolvedLabel = label;
        }

        public int resolvedLabel() {
            return this.resolvedLabel;
        }
    }
}

