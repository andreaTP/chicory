/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm;

import com.dylibso.chicory.wasm.ChicoryException;

public class UninstantiableException
extends ChicoryException {
    public UninstantiableException(String msg) {
        super(msg);
    }

    public UninstantiableException(Throwable cause) {
        super(cause);
    }

    public UninstantiableException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

