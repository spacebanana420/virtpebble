package virtpebble.setup

import virtpebble.config.*, bananatui.*
import java.io.File

def setupBaseConfig() =
  val arch = readUserInput("Type the name of the default architecture to use\nThis is only used in case a virtual machine lacks architecture information") //replace with a set of options
  val vmpath = chooseOption_dir("Type the path to store your virtual machine configurations")
  writeBaseConfig(s"arch=$arch\nvmpath=$vmpath")
  if !File(vmpath).isDirectory() then File(vmpath).mkdirs()


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
      if !File(default_vmpath).isDirectory() then File(default_vmpath).mkdirs()

