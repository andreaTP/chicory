/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasm;

import com.dylibso.chicory.wasm.ChicoryException;

public class UnlinkableException
extends ChicoryException {
    public UnlinkableException(String msg) {
        super(msg);
    }

    public UnlinkableException(Throwable cause) {
        super(cause);
    }

    public UnlinkableException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

