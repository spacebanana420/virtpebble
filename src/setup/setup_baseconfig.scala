package virtpebble.setup

import virtpebble.config.*, bananatui.*
import java.io.File

def createVMPath(path: String) =
  //if !File(path).isDirectory() then File(path).mkdirs()
  if !File(s"$path/disks").isDirectory() then File(s"$path/disks").mkdirs() //creates everything at once, more efficient

def setupBaseConfig() =
  val arch = spawnAndRead("Type the name of the default architecture to use\nThis is only used in case a virtual machine lacks architecture information") //replace with a set of options
  val vmpath = chooseOption_dir("Type the path to store your virtual machine configurations")
  writeBaseConfig(s"arch=$arch\nvmpath=$vmpath")
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

