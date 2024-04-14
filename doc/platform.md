# Virtpebble platform support

Virtpebble is cross-platform, although its OS and CPU support is limited to QEMU's support itself.

### Supported operating systems

* Linux-based
* Windows
* MacOS
* FreeBSD, OpenBSD, NetBSD and other similar systems

### Supported host CPU architectures

* x86
* ARM
* MIPS
* PPC
* RISC-V
* s390x
* SPARC

While you can run QEMU on an unsupported CPU architecture, this requires that QEMU uses an interpreter for translating CPU instructions, which results in worse performance.

### Virtpebble software requirements

* Java 11 or Scala
* QEMU

You need either Java 11 (or greater) or Scala (running a JVM >= 11) in your system. For most functionality you need QEMU of course, as it's what Virtpebble uses for running your virtual machines. Without QEMU, Virtpebble refuses to boot into the main menu as a security measure.
