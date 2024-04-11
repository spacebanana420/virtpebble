# Virtpebble's qemulib

Here are the functions related to running QEMU.

For ```args```, you need to retrieve and concatenate the results of calling the functions as seen [here](args.md)

---
```scala
def check_qemu(path: String = "qemu-system-x86_64"): Boolean
```
Attempts to run QEMU, good for checking whether you have it in your system or not.

```exec``` should be the program name or the path to its executable.

---
```scala
def qemu_run(args: Seq[String] = Vector(), exec: String = "qemu-system-x86_64", quiet: Boolean = true)
```
Runs QEMU to launch a virtual machine. QEMU is launched in parallel, and so your program isn't stalled.

```args``` are the arguments you retrieve from the [argument functions](args.md).

```exec``` is the path to the QEMU executable. You can set absolute paths, or just change the name of the binary if QEMU is installed in your system. Change the "x86_64" to the desired architecture you want to emulate.

if ```quiet``` is set to true, QEMU won't print anything in your terminal's output.

---
```scala
def qemuimage_create(path: String, size: Int, format: String = "")
```
Creates a disk image in the path ```path```. The ```size``` is measured in megabytes.

By default, the disk image ```format``` is assumed based on the file extension you give. If you want to be sure it uses the correct format, you can specify it.

#### Supported formats:e
* raw
* qcow2
* qed
* qcow
* luks
* vdi
* vmdk
* vpc
* VHDX
* bochs
* cloop
* dmg
* parallels

---
```scala
def qemuimage_resize(path: String, size: Int)
def qemuimage_resize_add(path: String, size: Int)
def qemuimage_resize_lower(path: String, size: Int)
```
Resizes an existing disk image, measured in megabytes.

qemuimage_resize_add() and qemuimage_resize_lower() add and remove space, respectively. if you run ```qemuimage_resize_add("disk.img", 300)```, it raises the disk image's size by 300MB. The opposite is true for lower.

qemuimage_resize() sets the total size instead.

---
```scala
def qemuimage_info(path: String): String
```
Gives you information about the disk image that is in ```path```.
