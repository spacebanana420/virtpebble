# Virtpebble's qemulib

Here are the functions related to retrieving device/driver/backend support for your system.

These functions run QEMU to retrieve information, and so ```exec``` exists to specify the path to the QEMU executable.

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
