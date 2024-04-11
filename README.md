# Virtpebble
Virtpebble is a terminal-based virtual machine manager and runner. It uses [QEMU](https://www.qemu.org/) to run your virtual machines, with support for acceleration, such as KVM.

With Virtpebble, you can easily and conveniently create virtual machines and disk images, manage them and run them. All configurations are in plain text and easy to modify, as long as you know the correct values.

Due to the huge scale of QEMU, Virtpebble only supports a fraction of what you can do with it. I slowly implement new features as I discover QEMU and feel more comfortable with it.

Exceptionally, Bhyve support for FreeBSD as an alternative to QEMU is in my plans.

**This project is still in early development.**

# Requirements

* Java 8 or Scala 3
* [QEMU](https://www.qemu.org/)

# Download & how to use
The first release of Virtpebble is not out yet. To try out the project in its fresh state, check the instructions on how to build from source below.

# Compile from source
Virtpebble requires either Scala or Scala-CLI to build and run.

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

# User documentation

# Developer documentation
