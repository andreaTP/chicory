/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.log;

import com.dylibso.chicory.log.Logger;

public class SystemLogger
implements Logger {
    private static final System.Logger LOGGER = System.getLogger("chicory");

    @Override
    public void log(Logger.Level level, String msg, Throwable throwable) {
        System.Logger.Level sll = this.toSystemLoggerLevel(level);
        LOGGER.log(sll, msg, throwable);
    }

    @Override
    public boolean isLoggable(Logger.Level level) {
        System.Logger.Level sll = this.toSystemLoggerLevel(level);
        return LOGGER.isLoggable(sll);
    }

    System.Logger.Level toSystemLoggerLevel(Logger.Level level) {
        switch (level) {
            case ALL: {
                return System.Logger.Level.ALL;
            }
            case TRACE: {
                return System.Logger.Level.TRACE;
            }
            case DEBUG: {
                return System.Logger.Level.DEBUG;
            }
            case INFO: {
                return System.Logger.Level.INFO;
            }
            case WARNING: {
                return System.Logger.Level.WARNING;
            }
            case ERROR: {
                return System.Logger.Level.ERROR;
            }
        }
        throw new IllegalArgumentException("Unsupported logger level: " + String.valueOf((Object)level));
    }
}

