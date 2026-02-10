/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.FormatMethod
 */
package com.dylibso.chicory.log;

import com.google.errorprone.annotations.FormatMethod;
import java.util.Objects;
import java.util.function.Supplier;

public interface Logger {
    public void log(Level var1, String var2, Throwable var3);

    public boolean isLoggable(Level var1);

    default public void trace(String msg) {
        Objects.requireNonNull(msg);
        if (this.isLoggable(Level.TRACE)) {
            this.log(Level.TRACE, msg, null);
        }
    }

    default public void trace(Supplier<String> msgSupplier) {
        Objects.requireNonNull(msgSupplier);
        if (this.isLoggable(Level.TRACE)) {
            this.log(Level.TRACE, msgSupplier.get(), null);
        }
    }

    default public void trace(Supplier<String> msgSupplier, Throwable throwable) {
        Objects.requireNonNull(msgSupplier);
        if (this.isLoggable(Level.TRACE)) {
            this.log(Level.TRACE, msgSupplier.get(), throwable);
        }
    }

    @FormatMethod
    default public void tracef(String format, Object ... args) {
        Objects.requireNonNull(format);
        if (this.isLoggable(Level.TRACE)) {
            String msg = format;
            if (args.length != 0) {
                msg = String.format(msg, args);
            }
            this.log(Level.TRACE, msg, null);
        }
    }

    default public void debug(String msg) {
        Objects.requireNonNull(msg);
        if (this.isLoggable(Level.DEBUG)) {
            this.log(Level.DEBUG, msg, null);
        }
    }

    default public void debug(Supplier<String> msgSupplier) {
        Objects.requireNonNull(msgSupplier);
        if (this.isLoggable(Level.DEBUG)) {
            this.log(Level.DEBUG, msgSupplier.get(), null);
        }
    }

    default public void debug(Supplier<String> msgSupplier, Throwable throwable) {
        Objects.requireNonNull(msgSupplier);
        if (this.isLoggable(Level.DEBUG)) {
            this.log(Level.DEBUG, msgSupplier.get(), throwable);
        }
    }

    @FormatMethod
    default public void debugf(String format, Object ... args) {
        Objects.requireNonNull(format);
        if (this.isLoggable(Level.DEBUG)) {
            String msg = format;
            if (args.length != 0) {
                msg = String.format(msg, args);
            }
            this.log(Level.DEBUG, msg, null);
        }
    }

    default public void info(String msg) {
        Objects.requireNonNull(msg);
        if (this.isLoggable(Level.INFO)) {
            this.log(Level.INFO, msg, null);
        }
    }

    default public void info(Supplier<String> msgSupplier) {
        Objects.requireNonNull(msgSupplier);
        if (this.isLoggable(Level.INFO)) {
            this.log(Level.INFO, msgSupplier.get(), null);
        }
    }

    default public void info(Supplier<String> msgSupplier, Throwable throwable) {
        Objects.requireNonNull(msgSupplier);
        if (this.isLoggable(Level.INFO)) {
            this.log(Level.INFO, msgSupplier.get(), throwable);
        }
    }

    @FormatMethod
    default public void infof(String format, Object ... args) {
        Objects.requireNonNull(format);
        if (this.isLoggable(Level.INFO)) {
            String msg = format;
            if (args.length != 0) {
                msg = String.format(msg, args);
            }
            this.log(Level.INFO, msg, null);
        }
    }

    default public void warn(String msg) {
        Objects.requireNonNull(msg);
        if (this.isLoggable(Level.WARNING)) {
            this.log(Level.WARNING, msg, null);
        }
    }

    default public void warn(Supplier<String> msgSupplier) {
        Objects.requireNonNull(msgSupplier);
        if (this.isLoggable(Level.WARNING)) {
            this.log(Level.WARNING, msgSupplier.get(), null);
        }
    }

    default public void warn(Supplier<String> msgSupplier, Throwable throwable) {
        Objects.requireNonNull(msgSupplier);
        if (this.isLoggable(Level.WARNING)) {
            this.log(Level.WARNING, msgSupplier.get(), throwable);
        }
    }

    @FormatMethod
    default public void warnf(String format, Object ... args) {
        Objects.requireNonNull(format);
        if (this.isLoggable(Level.WARNING)) {
            String msg = format;
            if (args.length != 0) {
                msg = String.format(msg, args);
            }
            this.log(Level.WARNING, msg, null);
        }
    }

    default public void error(String msg) {
        Objects.requireNonNull(msg);
        if (this.isLoggable(Level.ERROR)) {
            this.log(Level.ERROR, msg, null);
        }
    }

    default public void error(Supplier<String> msgSupplier) {
        Objects.requireNonNull(msgSupplier);
        if (this.isLoggable(Level.ERROR)) {
            this.log(Level.ERROR, msgSupplier.get(), null);
        }
    }

    default public void error(Supplier<String> msgSupplier, Throwable throwable) {
        Objects.requireNonNull(msgSupplier);
        if (this.isLoggable(Level.ERROR)) {
            this.log(Level.ERROR, msgSupplier.get(), throwable);
        }
    }

    @FormatMethod
    default public void errorf(String format, Object ... args) {
        Objects.requireNonNull(format);
        if (this.isLoggable(Level.ERROR)) {
            String msg = format;
            if (args.length != 0) {
                msg = String.format(msg, args);
            }
            this.log(Level.ERROR, msg, null);
        }
    }

    public static enum Level {
        ALL(Integer.MIN_VALUE),
        TRACE(400),
        DEBUG(500),
        INFO(800),
        WARNING(900),
        ERROR(1000),
        OFF(Integer.MAX_VALUE);

        private final int severity;

        private Level(int severity) {
            this.severity = severity;
        }

        public final String getName() {
            return this.name();
        }

        public final int getSeverity() {
            return this.severity;
        }
    }
}

