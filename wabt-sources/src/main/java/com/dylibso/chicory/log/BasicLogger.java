/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.log;

import com.dylibso.chicory.log.Logger;
import java.util.logging.Level;

public class BasicLogger
implements Logger {
    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger("chicory");

    @Override
    public void log(Logger.Level level, String msg, Throwable throwable) {
        LOGGER.log(this.toJavaLoggerLevel(level), msg, throwable);
    }

    @Override
    public boolean isLoggable(Logger.Level level) {
        return LOGGER.isLoggable(this.toJavaLoggerLevel(level));
    }

    Level toJavaLoggerLevel(Logger.Level level) {
        switch (level) {
            case ALL: {
                return Level.ALL;
            }
            case TRACE: {
                return Level.FINEST;
            }
            case DEBUG: {
                return Level.FINE;
            }
            case INFO: {
                return Level.INFO;
            }
            case WARNING: {
                return Level.WARNING;
            }
            case ERROR: {
                return Level.SEVERE;
            }
            case OFF: {
                return Level.OFF;
            }
        }
        throw new IllegalArgumentException("Unsupported logger level: " + String.valueOf((Object)level));
    }
}

