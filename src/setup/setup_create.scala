package virtpebble.setup

import virtpebble.config.{writeConfig, getVMpath, getDefaultArch}
import qemulib.{getGraphicalAccelerators, getNetInfo, getAudioDrivers, getAudioModels, getNetModels}
import bananatui.*
import java.io.File
import virtpebble.config.getDiskList
import virtpebble.createImage
import qemulib.getAccelerators
import qemulib.supportedHostArchitectures
import qemulib.getDisplayDevices


// def create_config() =

private def generateName(path: String, name: String = "virtpebble", i: Int = 0): String =
  if File(s"$path/${name}-${i}_vm.txt").isFile() then
    generateName(path, name, i+1)
  else "${name}-${i}"

def setupVM() = //add a way to cancel through recursion, use accel and machine as function args
  val host_arch =  getDefaultArch()

  val vms_path = getVMpath()
  val n = readUserInput("Type the name for your virtual machine")
  val name = if n != "" then n else generateName(vms_path)
  val a = setupArch(); val arch = s"arch=$a" //exceptional
  val accel = setupAccel(a)
  val m =
    if a == host_arch then "" else setupMachine() //implement arch too
  val machine = s"machine=$m"
  val cpu = setupCPU()
  val ram = setupRAM()
  val drives = setupDrives()
  val vga = setupVGA(a, m)
  val audio = setupAudio(a, m)
  val net = setupNet(a, m)
  val opts = drives ++ Vector(arch, accel, machine, cpu, ram, vga, audio, net)

  writeConfig(s"$vms_path/${name}_vm.txt", opts, false)

def setupArch(): String =
  val opts = supportedHostArchitectures() //add more arches later
  val arch = chooseOption_string(opts, "Choose the virtual machine's architecture", "Default (x86_64)")
  if arch == "" then "x86_64" else arch

def setupAccel(arch: String = "x86_64"): String =
  val accels = getAccelerators(s"qemu-system-$arch")
  val accel = chooseOption_string(accels, "Choose a hypervisor/accelerator\n\nAny available hypervisor is preferred over tcg for better VM performance", "Default (tcg)")
  if accel == "" then "accel=tcg" else s"accel=$accel"

def setupMachine(): String =
  val machines = Vector("virt") //add more in the future
  val m = chooseOption_string(machines, "Choose a machine for QEMU to emulate\n\nThis is needed for cross-architecture emulation\nIf available, \"virt\" is recommended", s"Default (${machines(0)})")
  if m == "" then "none" else m

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

def addDisks(disks: Vector[String] = Vector()): Vector[String] =
  val available_disks = getDiskList()
  def addDrive(mode: Int): String =
//     val file = chooseOption_file("Type the path to the disk file in your system")
    val choice = chooseOption_astring(available_disks, "Choose a disk image or create your own", "Create disk image")
    val file = if choice == "" then setup_createImage(diskimage_dir()) else choice
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
      "cd"
    else "dc"
  val menu = askPrompt("Do you want to enable the boot menu?")
  if askPrompt("Do you want to add a splash image to the boot menu?") then
    val splash = chooseOption_file("Add the path to an image file")
    s"boot=$order:$menu:$splash"
  else s"boot=$order:$menu"

def setupVGA(arch: String = "x86_64", machine: String): String =
  if arch != "x86_64" then
    val supported = getDisplayDevices(s"qemu-system-$arch", machine)
    val ans = chooseOption_string(supported, "Choose what graphical acceleration to use\n\nIf available, virtio-gpu-pci should be a good choice", s"Default (${supported(0)})")
    if ans != "" then s"device=$ans" //replace with device!!!!!!!!
    else s"device=${supported(0)}"
  else
    val supported = getGraphicalAccelerators(s"qemu-system-$arch", machine)
    val ans = chooseOption_string(supported, "Choose what graphical acceleration to use\n\nVirtio and QXL, if available, are usually ideal, otherwise you can choose STD", "Default (std)")
    if ans != "" then s"vga=$ans"
    else s"vga=std"

def setupAudio(arch: String = "x86_64", machine: String): String =
  val supported_d = getAudioDrivers(s"qemu-system-$arch", machine)
  val supported_m = getAudioModels(s"qemu-system-$arch", machine)
  val d = chooseOption_string(supported_d, "Choose an audio backend\n\npa is recommended for Linux-based systems\noss is recommended for FreeBSD", "Default (none)")
  val driver =
    if d == "" then "none"
    else d
  val m = chooseOption_string(supported_m, "Choose a virtual audio model", s"Default (${supported_m(0)})")
  val model =
    if m != "" then supported_m(0)
    else m
  s"audio=$driver:$model"

def setupNet(arch: String = "x86_64", machine: String): String =
  val models = getNetModels(s"qemu-system-$arch", machine)
  val model = chooseOption_string(models, "Choose a network model\n\n\"none\" disables network connections for the virtual machine\nIf available, \"virtio-net-pci\" is a good choice", "Default (none)")
  if model == "" then "" else s"net=user:$model"

