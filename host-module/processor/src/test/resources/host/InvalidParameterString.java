package chicory.testing;

import com.dylibso.chicory.function.annotations.HostModule;
import com.dylibso.chicory.function.annotations.WasmExport;

@HostModule(name = "bad_param")
public final class InvalidParameterString {

    @WasmExport
    public long concat(int a, String s) {
        return (s + a).length();
    }
}
