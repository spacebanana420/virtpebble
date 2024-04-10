package virtpebble.setup

import virtpebble.config.{writeConfig, getVMpath, getVMEntries}, virtpebble.hasAnyVMs
import bananatui.*
import java.io.File, java.nio.file.Files, java.nio.file.Path


def setup_deleteVM() =
  val vmpath = getVMpath()
  val vms = getVMEntries(vmpath) //doesnt crash if null, filter() probably changes it to an empty array()
  val vm = chooseOption_astring(vms, "Choose a virtual machine to delete", "Cancel")
  if vm != "" && askPrompt(s"The virtual machine $vm will be deleted permanently, this cannot be undone\nProceed?") then
    Files.delete(Path.of(s"$vmpath/$vm"))
