/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm;

import com.dylibso.chicory.wasm.ChicoryException;

public class MalformedException
extends ChicoryException {
    public MalformedException(String msg) {
        super(msg);
    }

    public MalformedException(Throwable cause) {
        super(cause);
    }

    public MalformedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

