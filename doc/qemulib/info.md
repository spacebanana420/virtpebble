# Virtpebble's qemulib

Here are the functions related to retrieving device/driver/backend support for your system.

These functions run QEMU to retrieve information, and so ```exec``` exists to specify the path to the QEMU executable.

---
```scala
def supportedHostArchitectures(): Vector[String]
```
Gets the list of supported host architectures.

#### Supported architectures:
* x86_64
* i386
* aarch64
* arm
* riscv64
* riscv32
* ppc64
* ppc

---
```scala
def supportedGuestArchitectures(): Vector[String]
```
Gets the list of supported guest architectures.

#### Supported architectures:
* x86_64
* i386
* aarch64
* arm
* riscv64
* riscv32
* ppc64
* ppc
* alpha
* avr
* cris
* hppa
* loongarch64
* m68k
* microblaze
* microblazeel
* mips
* mips64
* mips64el
* mipsel
* nios2
* or1k
* rx
* s390x
* sh4
* sh4eb
* sparc
* sparc64
* tricore
* xtensa
* xtensaeb

---
```scala
def getCPUList(exec: String = "qemu-system-x86_64"): Vector[String]
```
Gets the list of supported CPUs you can emulate.

---
```scala
def getAccelerators(exec: String = "qemu-system-x86_64"): Vector[String]
```
Gets the list of hypervisors that are supported by your system.

---
```scala
def getGraphicalAccelerators(exec: String = "qemu-system-x86_64"): Vector[String]
```
Gets the list of graphical accelerators that are supported by your system.

---
```scala
def getMachines(exec: String = "qemu-system-x86_64"): Vector[String]
```
Gets the list of machines QEMU can emulate given the guest architecture.

---
```scala
def getAudioDrivers(exec: String = "qemu-system-x86_64"): Vector[String]
```
```scala
def getAudioModels(exec: String = "qemu-system-x86_64"): Vector[String]
```
Gets the list of audio drivers and models you can use to configure audio.

---
```scala
def getNetInfo(exec: String = "qemu-system-x86_64"): String
```
Gets network information info you can use to configure network.

---
```scala
def getDevices(exec: String = "qemu-system-x86_64"): String
```
Gets a list of all devices you can use.

---
```scala
def getDisplayDevices(exec: String = "qemu-system-x86_64"): Vector[String]
```
Gets a list of all display/graphical devices you can use. It's parsed and ready to use
