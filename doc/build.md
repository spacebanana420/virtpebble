# Compile from source
Virtpebble requires either Scala or Scala-CLI to build and run.

There are many ways to build Virtpebble, depending on the tooling or methods.

A lightweight JAR requires Scala to run, while a fat JAR only requires Java, as it contains the Scala runtime already.

## Build lightweight JAR (with Scalac)
```
scalac src/*.scala src/*/*.scala -d virtpebble.jar
```

You can run Virtpebble with Scala or Scala-CLI:

```
scala virtpebble.jar
```

## Build fat JAR (with Scala-CLI)
```
scala-cli --power package src --assembly --preamble=false -o virtpebble.jar
```
You can run Virtpebble with Java:

```
java -jar virtpebble.jar
```

## Build and run directly (with Scala-CLI)
```
scala-cli src
```
This builds the first time and runs the program. Next time you run Virtpebble, it won't compile again, since it's already compiled.

To re-compile, run:
```
scala-cli compile src
```
