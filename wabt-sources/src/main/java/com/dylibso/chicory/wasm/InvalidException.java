/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm;

import com.dylibso.chicory.wasm.ChicoryException;

public class InvalidException
extends ChicoryException {
    public InvalidException(String msg) {
        super(msg);
    }

    public InvalidException(Throwable cause) {
        super(cause);
    }

    public InvalidException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

