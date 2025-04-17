import re

def extract_methods(file_path):
    pattern = re.compile(r"com\.dylibso\.chicory\.wabt\.Wat2WasmMachine\$AotMethods\.(\w+)")
    methods = set()

    with open(file_path, "r", encoding="utf-8") as f:
        for line in f:
            matches = pattern.findall(line)
            methods.update(matches)

    return sorted(methods)

# Example usage
if __name__ == "__main__":
    file_path = "wabt/target/surefire-reports/2025-04-17T09-48-49_750-jvmRun1.dumpstream"  # Replace with your file path
    results = extract_methods(file_path)
    for method in results:
        print(method)
