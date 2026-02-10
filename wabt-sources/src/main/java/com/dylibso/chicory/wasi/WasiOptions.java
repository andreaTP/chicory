/*
 * Decompiled with CFR 0.152.
 */
package com.dylibso.chicory.wasi;

import com.dylibso.chicory.wasi.IO;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.time.Clock;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class WasiOptions {
    private final Random random;
    private final Clock clock;
    private final OutputStream stdout;
    private final OutputStream stderr;
    private final InputStream stdin;
    private final List<String> arguments;
    private final Map<String, String> environment;
    private final Map<String, Path> directories;

    public static Builder builder() {
        return new Builder();
    }

    private WasiOptions(Random random, Clock clock, OutputStream stdout, OutputStream stderr, InputStream stdin, List<String> arguments, Map<String, String> environment, Map<String, Path> directories) {
        this.random = Objects.requireNonNull(random);
        this.clock = Objects.requireNonNull(clock);
        this.stdout = Objects.requireNonNull(stdout);
        this.stderr = Objects.requireNonNull(stderr);
        this.stdin = Objects.requireNonNull(stdin);
        this.arguments = List.copyOf(arguments);
        this.environment = Collections.unmodifiableMap(new LinkedHashMap<String, String>(environment));
        this.directories = Collections.unmodifiableMap(new LinkedHashMap<String, Path>(directories));
    }

    public Random random() {
        return this.random;
    }

    public Clock clock() {
        return this.clock;
    }

    public OutputStream stdout() {
        return this.stdout;
    }

    public OutputStream stderr() {
        return this.stderr;
    }

    public InputStream stdin() {
        return this.stdin;
    }

    public List<String> arguments() {
        return this.arguments;
    }

    public Map<String, String> environment() {
        return this.environment;
    }

    public Map<String, Path> directories() {
        return this.directories;
    }

    public static final class Builder {
        private Random random = ThreadLocalRandom.current();
        private Clock clock = Clock.systemUTC();
        private OutputStream stdout = IO.nullOutputStream();
        private OutputStream stderr = IO.nullOutputStream();
        private InputStream stdin = IO.nullInputStream();
        private List<String> arguments = List.of();
        private final Map<String, String> environment = new LinkedHashMap<String, String>();
        private final Map<String, Path> directories = new LinkedHashMap<String, Path>();

        private Builder() {
        }

        public Builder withRandom(Random random) {
            this.random = random;
            return this;
        }

        public Builder withClock(Clock clock) {
            this.clock = clock;
            return this;
        }

        public Builder withStdout(OutputStream stdout) {
            this.stdout = stdout;
            return this;
        }

        public Builder withStderr(OutputStream stderr) {
            this.stderr = stderr;
            return this;
        }

        public Builder withStdin(InputStream stdin) {
            this.stdin = stdin;
            return this;
        }

        public Builder inheritSystem() {
            this.stdout = System.out;
            this.stdin = System.in;
            this.stderr = System.err;
            return this;
        }

        public Builder withArguments(List<String> arguments) {
            this.arguments = List.copyOf(arguments);
            return this;
        }

        public Builder withEnvironment(String name, String value) {
            this.environment.put(name, value);
            return this;
        }

        public Builder withDirectory(String guest, Path host) {
            this.directories.put(guest, host);
            return this;
        }

        public WasiOptions build() {
            return new WasiOptions(this.random, this.clock, this.stdout, this.stderr, this.stdin, this.arguments, this.environment, this.directories);
        }
    }
}

