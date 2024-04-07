package virtpebble.setup

import virtpebble.config.{writeConfig, getVMpath}
import qemulib.{getGraphicalAccelerators, getNetInfo, getAudioDrivers, getAudioModels}
import bananatui.*
import java.io.File


// def create_config() =

private def generateName(path: String, name: String = "virtpebble", i: Int = 0): String =
  if File(s"$path/${name}-${i}_vm.txt").isFile() then
    generateName(path, name, i+1)
  else "${name}-${i}"

def setupVM() =
  val vms_path = getVMpath()
  val n = readUserInput("Type the name for your virtual machine")
  val name = if n != "" then n else generateName(vms_path)
  val cpu = setupCPU()
  val ram = setupRAM()
  val drives = setupDrives()
  val vga = setupVGA()
  val audio = setupAudio()
  val net = setupNet()
  val opts = drives ++ Vector(cpu, ram, vga, audio, net)

  writeConfig(s"$vms_path/${name}_vm.txt", opts)

def setupCPU(cores: Int = 1, threads: Int = 0, sockets: Int = 0): String =
  val answer = chooseOption(Vector("Set virtual cores", "Set virtual threads", "Set virtual sockets"), "Configure the virtual CPU", "Done")
  answer match
    case 0 =>
      s"cpu=$cores:$threads:$sockets"
    case 1 =>
      setupCPU(readInt("Type the number of cores for the virtual CPU"), threads, sockets)
    case 2 =>
      setupCPU(cores, readInt("Type the number of threads for the virtual CPU"), sockets)
    case 3 =>
      setupCPU(cores, threads, readInt("Type the number of sockets for the virtual CPU"))

def setupRAM(): String =
  val ram = readInt("Type the amount of RAM for the virtual machine")
  s"ram=$ram"

def setupDrives(): Vector[String] = addDisks() :+ configureBoot()

def addDisks(disks: Vector[String] = Vector()): Vector[String] = //add option to create disk image
  def addDrive(mode: Int): String =
    val file = chooseOption_file("Type the path to the disk file in your system")
    mode match
      case 2 => s"cdrom=$file"
      case 3 => s"drive=$file:${!File(file).canWrite()}"
      case _ => s"hd=$file"

  val opts = Vector("Add main drive", "Add CDROM", "Add secondary drive")
  val ans = chooseOption(opts, "What disk to add?", "Done")
  if ans == 0 then disks else addDisks(disks :+ addDrive(ans))

def configureBoot(): String =
  val ans = chooseOption(Vector("Main drive", "CDROM"), "Choose what disk to prioritize on boot", "Default (Main drive)")
  val order =
    if ans == 0 || ans == 1 then
      "dc"
    else "cd"
  val menu = askPrompt("Do you want to enable the boot menu?")
  if askPrompt("Do you want to add a splash image to the boot menu?") then
    val splash = chooseOption_file("Add the path to an image file")
    s"boot=$order:$menu:$splash"
  else s"boot=$order:$menu"

def setupVGA(): String =
  val supported = getGraphicalAccelerators()
  val ans = chooseOption_string(supported, "Choose what graphical acceleration to use", "Default (std)")
  if ans != "" then s"vga=$ans"
  else s"vga=std"

def setupAudio(): String =
  val supported_d = getAudioDrivers()
  val supported_m = getAudioModels()
  val d = chooseOption_string(supported_d, "Choose an audio backend", "Default (none)")
  val driver =
    if d != "" then "none"
    else d
  val m = chooseOption_string(supported_m, "Choose a virtual audio model", s"Default (${supported_m(0)})")
  val model =
    if m != "" then supported_m(0)
    else m
  s"audio=$driver:$model"


// def setupNet(): String = //this needs to be finished
//   val supported = getNetInfo()
//   val ans = chooseOption_string(supported, "Choose what graphical acceleration to use", "Default (std)")
//   if ans != "" then s"vga=$ans"
//   else s"vga=std"


def setupNet(): String = s"vga=user:virtio-net-pci"
