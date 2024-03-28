# Virtpebble's qemulib

Here are the functions related to running QEMU.

For ```args```, you need to retrieve and concatenate the results of calling the functions as seen [here](args.md)

---
```scala
def qemu_run(args: Seq[String] = Vector(), exec: String = "qemu-system-x86_64", quiet: Boolean = true)
```
Runs QEMU to launch a virtual machine.

```args``` are the arguments you retrieve from the [argument functions](args.md).

```exec``` is the path to the QEMU executable. You can set absolute paths, or just change the name of the binary if QEMU is installed in your system. Change the "x86_64" to the desired architecture you want to emulate.

if ```quiet``` is set to true, QEMU won't print anything in your terminal's output.
