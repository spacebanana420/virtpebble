# Getting started with Virtpebble

Virtpebble is a cross-platform virtual machine runner/manager/launcher that is powered by QEMU.

Before running Virtpebble, make sure you have QEMU installed in your system, otherwise you lack the core functionality, which would be running your virtual machines.

Once you have QEMU in your system, Virtpebble can work.

Virtpebble stores base information, such as the path to your virtual machines, in config.txt. This config file is created in the same place where Virtpebble is.

The path to your virtual machines is used to store your virtual machine configurations as well as your disk images. You can create these within Virtpebble, or you can manually add ones you already have yourself.

To get started with running your virtual machines, you can begin creating one through Virtpebble's configuration helper. If you do not have a disk image yet, you can create one with Virtpebble, and the configuration helper will detect if you don't have any disk images and prompt you to create one.

If you have yet to install an operating system on your virtual machine, don't forget to donwload the installation ISO you want, and add it to the "disks" directory inside the location of the virtual machines as seen in ```config.txt```.

**this documentation is not finished yet**
