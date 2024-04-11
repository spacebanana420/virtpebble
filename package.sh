echo "Building Virtpebble"
scalac src/*.scala src/*/*.scala -d virtpebble.jar
scala-cli --power package src --assembly --preamble=false -f --jvm 8 -o virtpebble-java.jar

echo "Building native binary (Linux, static)"
native-image --no-fallback --static -O3 -jar virtpebble-java.jar -o virtpebble
# echo "Building native binary (Linux, static, native cpu)"
# native-image --no-fallback --static -march=native -O3 -jar virtpebble-java.jar -o virtpebble-native
echo "Packaging Linux native binary"
7z a -mx4 -mmt0 virtpebble-linux-x86_64.zip virtpebble

