#!/bin/bash
# Decompile all classes from the JAR and create source structure

JAR_DIR="/home/andreatp/workspace/chicory6/reproducer"
OUT_DIR="/home/andreatp/workspace/wabt-sources/src/main/java"

cd "$JAR_DIR" || exit 1

# Create output directory structure
mkdir -p "$OUT_DIR"

# Extract all class files
echo "Extracting class files..."
jar -xf wabt-999-SNAPSHOT.jar

# Find all classes and decompile them
echo "Decompiling classes..."
find com -name "*.class" | while read classfile; do
    # Convert class file path to Java package path
    pkg_path="${classfile%.class}"
    pkg_dir=$(dirname "$pkg_path")
    class_name=$(basename "$pkg_path")
    
    # Create package directory
    mkdir -p "$OUT_DIR/$pkg_dir"
    
    # Decompile to Java source (using javap as fallback, but ideally use a real decompiler)
    echo "Decompiling: $pkg_path"
    javap -c -p "$pkg_path" > "$OUT_DIR/${pkg_path}.javap" 2>&1
    
    # Try to extract class name and basic structure
    javap -p "$pkg_path" 2>&1 | head -50 > "$OUT_DIR/${pkg_path}.sig" 2>&1
done

echo "Decompilation complete. Files in $OUT_DIR"
echo "Note: Full Java source reconstruction requires a proper decompiler (jadx, jd-cli, or cfr)"
echo "The .javap files contain bytecode, .sig files contain signatures"
