# Virtpebble's qemulib

Virtpebble runs QEMU through its CLI interface to achieve the virtual machine and emulation capabilities it requires.

To achieve a clean and maintainable way to do this, Virtpebble isolates this functionality into a library called "qemulib". This library is responsible for all QEMU executions, retrieving and parsing device and backend support, parsing CLI args, etc.

If you are a Scala developer and you would like to make use of QEMU in your software, you can make use of qemulib's functionality. Like the rest of the project, it's licensed under MIT.

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
