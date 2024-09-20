package chicory.testing;

import com.dylibso.chicory.function.annotations.HostModule;
import com.dylibso.chicory.function.annotations.WasmExport;

@HostModule(name = "bad_return")
public final class InvalidReturn {

    @WasmExport
    public String toString(int x) {
        return String.valueOf(x);
    }
}
