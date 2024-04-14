package qemulib

import qemulib.misc.*
import java.io.File

def setAccel(accel: String): Vector[String] =
  val os = getHostOS
  val supportedAccels =
    if os.contains("Windows") then
      Vector("whpx", "tcg")
    else if os.toLowerCase().contains("mac") then //i don't know how the name is presented
      Vector("hvf", "tcg")
    else
      os match
        case "Linux" => Vector("kvm", "xen", "tcg")
        case "NetBSD" => Vector("nvmm", "tcg")
        case _ => Vector("tcg")

  if supportedAccels.contains(accel) then Vector("-accel", accel)
  else Vector()

def setMachine(machine: String = "virt"): Vector[String] =
  if machine == "" then Vector() else Vector("-machine", machine)

def setCPU(cores: Short, threads: Short = 0, sockets: Short = 0): Vector[String] = //add model
  val c = s"cores=$cores"
  val t = if threads != 0 then s"threads=$threads" else ""
  val s = if sockets != 0 then s"sockets=$sockets" else ""
  val cpuconfig = mkarg(Vector(c, t, s).filter(x => x.length != 0))
  Vector("-smp", cpuconfig)

def setRAM(amt: Int): Vector[String] =
  if amt < 1 then Vector()
  else Vector("-m", s"${amt}M")

private def isFileOk(file: String): Boolean =
  val f = File(file)
  f.isFile() && f.canWrite()

private def mkdiskArg(base: String, disk: String): Vector[String] =
  if disk != "" && isFileOk(disk) then Vector(base, disk) else Vector()

// def setDisk_floppy(file: String, id: Byte): Vector[String] =
//   val arg =
//     if id == 0 then Vector("-fda", file)
//     else Vector("-fdb", file)
//   if isFileOk(file) then arg else Vector()

def setDisks_floppy(disk1: String, disk2: String = ""): Vector[String] =
  mkdiskArg("-fda", disk1) ++ mkdiskArg("-fdb", disk2)

// def setDisk_drive(file: String, id: Byte): Vector[String] = //use multiple strings instead of ID
//   val arg =
//     id match
//       case 0 => Vector("-hda", file)
//       case 1 => Vector("-hdb", file)
//       case 2 => Vector("-hdc", file)
//       case 3 => Vector("-hdd", file)
//       case _ => Vector("-hda", file)
//   if isFileOk(file) then arg else Vector()

def setDisks_drive(disk1: String, disk2: String = "", disk3: String = "", disk4: String = ""): Vector[String] =
  mkdiskArg("-hda", disk1) ++ mkdiskArg("-hdb", disk2) ++ mkdiskArg("-hdc", disk3) ++ mkdiskArg("-hdd", disk4)


def setDisk_cdrom(file: String): Vector[String] =
  if isFileOk(file) then Vector("-cdrom", file) else Vector()

def addDrive(file: String, readonly: Boolean = false): Vector[String] =
  if readonly then Vector("-drive", s"file=$file,readonly=on")
  else Vector("-drive", s"file=$file,readonly=off")

def fullscreenOnStart(): Vector[String] = Vector("-full-screen")

def setGraphicsMode(mode: String): Vector[String] =
  val supported = Vector("std", "cirrus", "vmware", "qxl", "xenfb", "tcx", "cg3", "virtio", "none")
  if supported.contains(mode) then
    Vector("-vga", mode)
  else Vector()

def configureBoot(order: String, menu: Boolean = false, splash: String = ""): Vector[String] = //add splash image later and make order optional
  def isOrderOk(i: Int = 0): Boolean =
    if i >= order.length then true
    else if "acdn".contains(order(i)) == false then false
    else isOrderOk(i+1)

  val arg_order = if isOrderOk() then s"order=$order" else ""
  val arg_menu = if menu then "menu=on" else "menu=off"
  val arg_splash = if splash != "" && File(splash).isFile() && menu then s"splash=$splash" else ""
  Vector("-boot", mkarg(Vector(arg_order, arg_menu, arg_splash).filter(x => x.length != 0)))

def setAudio(driver: String, model: String): Vector[String] = //figure out -audiodev later
  Vector("-audio", s"driver=$driver,model=$model")

// def setNetwork(backend: String = "user", model: String = "virtio-net-pci"): Vector[String] =
//   if backend != "" && model != "" then Vector("-nic", s"$backend,model=$model")
//   else Vector("-nic", "user,model=virtio-net-pci")
def setNetwork(backend: String = "user", model: String = "virtio-net-pci"): Vector[String] =
  Vector("-nic", s"$backend,model=$model")


///Misc arguments

def setVGAMemory(mem: Int): Vector[String] = if mem <= 0 then Vector() else Vector("-device", s"VGA,vgamem_mb=$mem")

def setKeyboardLayout(layout: String): Vector[String] = Vector("-k", layout)
