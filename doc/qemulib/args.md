# Virtpebble's qemulib

Here are the functions related to retrieving CLI arguments to use on QEMU.

These functions return string vectors containing the CLI arguments. You can concatenate these vectors with each other, and then pass them to ```qemu_run()```.

---

```scala
def setAccel(accel: String): Vector[String]
```
Sets the hypervisor to use with QEMU. Default is ```tcg``` which is a system-agnostic runtime compiler built into QEMU.

#### Windows:
* whpx
* tcg
#### Linux-based:
* kvm
* xen
* tcg
#### MacOS:
* hvf
* tcg
#### NetBSD:
* nvmm
* tcg
#### Other OSes:
* tcg

---
```scala
def setCPU(cores: Short, threads: Short = 0, sockets: Short = 0): Vector[String]
```
Configures the guest CPU. Setting the core count is mandatory, but the rest is automatic if set to 0.

---
```scala
def setRAM(amt: Int): Vector[String]
```
Sets the RAM size for the guest virtual machine, in **megabytes**. Setting this is highly reommended, otherwise you will run out of guest memory very quickly.

---
```scala
def setDisk_floppy(file: String, id: Byte): Vector[String]
```
Adds a disk image as a floppy disk to your virtual machine.

```file``` must be a path leading to a file.

```id``` can be 0 or 1. This means you can add 2 floppy disks.

---
```scala
def setDisk_drive(file: String, id: Byte): Vector[String]
```
Adds a disk image as a regular drive to your virtual machine.

```file``` must be a path leading to a file.

```id``` can be 0, 1, 2, or 3. This means you can add 4 disks.

---
```scala
def setDisk_cdrom(file: String): Vector[String]
```
Adds a disk image as a CDROM to your virtual machine. There can only be 1.

```file``` must be a path leading to a file.

---
```scala
def addDrive(file: String, readonly: Boolean = false): Vector[String]
```
Adds a drive to your virtual machine. There is no restriction on how many you can add.

```file``` must be a path leading to a file.

```readonly``` is false by default, but can be set to true to prevent the guest to write data to it.

---
```scala
def fullscreenOnStart(): Vector[String]
```
Makes QEMU launch the display on fullscreen by default once you run the virtual machine.

---
```scala
def setGraphicsMode(mode: String): Vector[String]
```
Sets the graphics mode for your virtual machine.

#### Supported modes:
* std
* cirrus
* vmware
* qxl
* xenfb
* tcx
* cg3
* virtio
* none

---
```scala
def configureBoot(order: String, menu: Boolean = false, splash: String = ""): Vector[String]
```
Configures the boot order of your drives, following a specific format.

```order``` is a special string that can only contain the characters "a", "c", "d" or "n".
* "a" is for floppy disks
* "c" is for cdrom drives
* "d" is for regular disks
* "n" is for network storage

If you want to, for example, set the boot order to attempt to boot from the disk first, and cdrom after, you can use the string ```"dc"```.

If ```menu``` is true, then QEMU will open the boot menu on launch.
```splash``` sets the splash image to use on the boot menu. This only matters if you have the menu enabled.

---
```scala
def setAudio(driver: String, model: String): Vector[String]
```
Configures audio for your virtual machine.

Supported drivers and models for your system can be found out by running the functions ```getAudioDrivers()``` and ```getAudioModels()```.

---
```scala
def setNetwork(backend: String = "user", model: String = "virtio-net-pci"): Vector[String]
```
Configures the network for your virtual machine.

Supported backends and models for your system can be found out by running the function ```getNetInfo()```.

Default values should be fine.
