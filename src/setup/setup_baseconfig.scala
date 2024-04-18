package virtpebble.setup

import virtpebble.config.*, bananatui.*
import qemulib.supportedHostArchitectures
import java.io.File

def createVMPath(path: String) =
  //if !File(path).isDirectory() then File(path).mkdirs()
  if !File(s"$path/disks").isDirectory() then File(s"$path/disks").mkdirs() //creates everything at once, more efficient

def setupBaseConfig() =
//   val arch = spawnAndRead("Type the name of the default architecture to use\nThis is only used in case a virtual machine lacks architecture information, and so it should be your machine's architecture") //replace with a set of options
  val opts = supportedHostArchitectures() //add more arches later
  val a = chooseOption_string(opts, "Choose the default architecture to use\nThis is only used in case a virtual machine lacks architecture information, and so it should be your machine's architecture", "Default (x86_64)")
  val arch = if a == "" then "x86_64" else a

  val vmpath = chooseOption_dir("Type the path to store your virtual machine configurations")
  val bios = readUserInput("Type the path to a BIOS firmware to use in virtual machines\nThis is only necessary for cross-architecture emulation, otherwise you can leave an empty prompt to disable")
  val option_bios = if bios != "" && File(bios).isFile() then s"\nbios=$bios" else ""

  writeBaseConfig(s"arch=$arch\nvmpath=$vmpath$option_bios")
  createVMPath(vmpath)

def checkConfig() =
  val conf = readConfig_base("config.txt")

  val vmpath = getVMpath(conf)
  val arch = getDefaultArch(conf)

  if vmpath == "" || arch == "" || !File(vmpath).isDirectory() then
    if askPrompt("Your base config \"config.txt\" seems to be incorrectly set up.\nWould you like to manually re-configure? If not, then the default setup will be used") then
      setupBaseConfig()
    else
      createBaseConfig(true)
      val default_vmpath = defaultVMPath()
      createVMPath(default_vmpath)

