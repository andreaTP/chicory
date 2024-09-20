package chicory.test;

import com.dylibso.chicory.function.annotations.HostModule;
import com.dylibso.chicory.runtime.Instance;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class WithImportsTest {
    public final AtomicInteger count = new AtomicInteger();

    // TODO: verify the file should be available in the current project and cannot be shared
    @HostModule(name = "console", file = "host-function.wat.wasm")
    class TestModule {
        //        private final Instance instance;
        private static final String EXPECTED = "Hello, World!";

        public TestModule() {
            //            instance =
            //                    Instance.builder(
            //                                    Parser.parse(
            //                                            WithImportsTest.class.getResourceAsStream(
            //
            // "/compiled/host-function.wat.wasm")))
            //                            .withHostImports(
            //                                    new
            // HostImports(TestModule_ModuleFactory.toHostFunctions(this)))
            //                            .build();
        }

        //        @WasmExport
        //        public void log(int len, int offset) {
        //            var message = instance.memory().readString(offset, len);
        //
        //            if (EXPECTED.equals(message)) {
        //                count.incrementAndGet();
        //            }
        //        }
    }

    @Test
    public void withImportsModule() {
        // Arrange
        var withImportsModule = new TestModule();

        // Act
        //        withImportsModule.logIt();

        // Assert
        //        assertEquals(10, count.get());
    }
}
