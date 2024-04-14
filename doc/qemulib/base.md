# Virtpebble's qemulib

Virtpebble runs QEMU through its CLI interface to achieve the virtual machine and emulation capabilities it requires.

To achieve a clean and maintainable way to do this, Virtpebble isolates this functionality into a library called "qemulib". This library is responsible for all QEMU executions, retrieving and parsing device and backend support, parsing CLI args, etc.

If you are a Scala developer and you would like to make use of QEMU in your software, you use qemulib. Like the rest of the project, it's licensed under MIT.

# How to use

Download the library's source, located in ```src/qemulib/```, add it to your project's source, and import qemulib:
```scala
import qemulib.*
```

# Documentation

#### [Runner](qemulib/runner.md)
* Base functions for running QEMU
#### [Args](qemulib/args.md)
* Functions that parse and provide you CLI arguments to use on QEMU
#### [Info](qemulib/info.md)
* Functions that run QEMU to retrieve information about supported drivers, backends, models, etc

# Getting started

Qemulib is a functional library that makes use mostly of function composition. Here's a quick example of how you can run an existing virtual machine:


```scala
qemuimage_create("/path/to/diskimage.qcow2", 6000, "qcow2") //Create the disk image of size 6GB with the format qcow2
val args = //CLI arguments for QEMU
  setAccel("kvm")
  ++ setCPU(4)
  ++ setRAM(2048)
  ++ setDisk_drive("/path/to/diskimage.qcow2")
  ++ setGraphicsMode("qxl")
  ++ setAudio("pa", "hda")
  ++ setNetwork()
qemu_run(args) //QEMU execution, by default for x86_64 guests
```

This is an example you can reproduce on a Linux system. First we build our commandline arguments:
* Enable KVM acceleration (most common usecase of QEMU and other virtual machine managers like virt-manager)
* Set the CPU core count to 4 cores
* Set the guest system's ram to 2048 megabytes
* Set the main disk drive as the file in "/path/to/diskimage.qcow2"
* Set the graphics acceleration mode to QXL
* Enable audio, and use Pulseaudio for the host, and emulate the Intel HDA card for the guest.
* Enable network, but don't specify parameters, the default ones are fine (user model and virtio-pci driver)

Now you can run QEMU and pass its arguments to it. The default QEMU binary is for x86_64 guests, so it doesn't have to be specified in this example. The quiet value is left at default, which is true, and so QEMU won't print any message to the terminal.

QEMU runs in parallel to your program, so you can launch multiple instances of it or do something else.
