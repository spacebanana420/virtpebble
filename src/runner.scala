package virtpebble

import virtpebble.config.{readConfig_VM, setQEMUArgs, getVMpath, getVMEntries, getArch}
import bananatui.*
import qemulib.qemu_run
import qemulib.qemuimage_create

def vmLoader() =
  val vm_path = getVMpath()
  val vm_entries = getVMEntries(vm_path)
  if vm_entries.length == 0 then pressToContinue("You have no virtual machines created!")
  else
    val vm = chooseOption_astring(vm_entries, "Choose a virtual machine to launch")
    if vm != "" then
      runVM(vm)

def runVM(conf: String) = //add support for more architectures through a config setting
  val args = setQEMUArgs(conf)
  val arch = getArch(readConfig_VM(conf))
  qemu_run(args, exec = s"qemu-system-$arch")

def createImage(path: String, size: Int, format: String) =
  qemuimage_create(path, size, format)
