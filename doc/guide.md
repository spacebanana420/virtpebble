# Getting started with Virtpebble

Virtpebble is a cross-platform virtual machine runner/manager/launcher that is powered by QEMU.

Before running Virtpebble, make sure you have [QEMU](https://qemu.org) installed in your system, otherwise you lack the core functionality, which would be running your virtual machines.

Once you have QEMU in your system, Virtpebble can work.

Virtpebble stores base information, such as the path to your virtual machines, in config.txt. This config file is created in the same place where Virtpebble is.

The path to your virtual machines is used to store your virtual machine configurations as well as your disk images. You can create these within Virtpebble, or you can manually add ones you already have yourself.

To get started with running your virtual machines, you can begin creating one through Virtpebble's configuration helper. If you do not have a disk image yet, you can create one with Virtpebble, and the configuration helper will detect if you don't have any disk images and prompt you to create one.

If you have yet to install an operating system on your virtual machine, don't forget to donwload the installation ISO you want, and add it to the "disks" directory inside the location of the virtual machines as seen in ```config.txt```.

# Creating a virtual machine

## Choosing an architecture

When creating a virtual machine, you have to choose its architecture. For most use cases, you will want to choose the same architecture as your host machine's. In less common cases, you might want to emulate a machine that uses a different architecture, and this choise will reflect that.

## Choosing an accelerator

A QEMU accelerator is a hypervisor. The hypervisor makes use of your machine's hardware resources for your virtual machine, which removes the necessity to emulate a CPU and improves performance.

TCG is the emulated option, and any other acceleration option is a hypervisor. TCG is usually not recommended unless you have no other option (for example, cross-architecture emulation).

## Choosing a machine

If you want to emulate a virtual machine of an architecture different than your host's then you have to specify a machine to emulate. If avaliable, "virt" is recommended.

## Configuring the virtual CPU

On Virtpebble, you can configure the number of cores, threads and sockets of your virtual CPU. Unless you have a reason not to, it's recommended to just configure the number of cores and threads, or just the number of cores.

## Configuring RAM

Virtpebble configures QEMU's memory allocation in a more "static" way, as in, the RAM allocation is constant, and doesn't vary between a minimum and maximum. For desktop systems with a GUI, 2 or more gigabytes of RAM is recommended.

## Configuring disks

Virtpebble has 3 types of virtual disks, based on 3 of multiple types in QEMU: main disks, CDROM disks and secondary disks.

Main disks and CDROM disks are the ones you use to boot when starting up your virtual machine. You can prioritize one of these 2 types over the other.

Secondary disks are disk images you want to include in your virtual machine but you don't want to boot from them.

You can also enable the QEMU boot menu, and add a splash image to it.

## Configuring graphics

The avaliable graphical backends depend on the virtual machine's CPU architecture. For x86_64, Virtio (and also QXL for Linux) is usually recommended, otherwise you can use std or cirrus if they work better for you.

For other architectures, virtio-gpu-pci (if available) is a reasonable option.

## Configuring audio

Audio backends vary between operating systems. To choose one, you must have some level of knowledge of your operating system's sound backends. For Linux-based systems, pulseaudio (labeled as "pa") is recommended. For FreeBSD, OSS is recommended.

Audio drivers are emulated. Most options are fine, but hda is more popular.

**this documentation is not finished yet**
