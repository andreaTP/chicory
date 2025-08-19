package com.dylibso.chicory.runtime;

public interface DebugMapper {

    class DebugInfo {
        final String fileName;
        final String functionName;
        final long line;

        public DebugInfo(String fileName, String functionName, long line) {
            this.fileName = fileName;
            this.functionName = functionName;
            this.line = line;
        }

        public String fileName() {
            return fileName;
        }

        public String functionName() {
            return functionName;
        }

        public long line() {
            return line;
        }
    }

    DebugInfo getDebugInfo(int lineAddress);
}
