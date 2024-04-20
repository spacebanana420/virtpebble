# Virtpebble
Virtpebble is a terminal-based virtual machine manager and runner. It uses [QEMU](https://www.qemu.org/) to run your virtual machines, with support for acceleration, such as KVM.

With Virtpebble, you can easily and conveniently create virtual machines and disk images, manage them and run them. All configurations are in plain text and easy to modify, as long as you know the correct values.

# Requirements

* Java 11 or Scala 3
* [QEMU](https://www.qemu.org/)

# Download & how to use

You can download the latest versions of Virtpebble in [the releases page](https://github.com/spacebanana420/virtpebble/releases)

If you have Scala installed, download ```virtpebble.jar```

If you only have Java, download ```virtpebble-java.jar```

If you have neither but you are on an x86_64 Linux system, download ```virtpebble-linux-x86_64.zip```

# User documentation

* [Getting started](doc/guide.md)
* [Platform support](doc/platform.md)
* [Compile from source](doc/build.md)

# Developer documentation

* [Qemulib documentation](doc/qemulib/base.md)
