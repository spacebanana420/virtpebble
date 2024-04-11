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
def setMachine(machine: String = "virt"): Vector[String]
```
Sets a machine for QEMU to emulate. This is only necessary if the guest architecture is not the same as the host's.

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
def setDisks_floppy(disk1: String, disk2: String = ""): Vector[String]
```
Adds disk images as floppy disks to your virtual machine.

Up to 2 floppy disks can be added.

Empty strings and strings that don't lead to the path of a file are discarded.

---
```scala
def setDisks_drive(disk1: String, disk2: String = "", disk3: String = "", disk4: String = ""): Vector[String]
```
Adds disk images as regular drives to your virtual machine.

Up to 4 hard drives can be added.

Empty strings and strings that don't lead to the path of a file are discarded.

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
* "d" is for cdrom drives
* "c" is for regular disks
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

---
```scala
def setVGAMemory(mem: Int): Vector[String]
```
Sets manually the video memory for the virtual machine's graphics, in megabytes.

A value between 64 and 256 is recommended. Minimum value accepted is 1.
