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

def setCPU(cores: Short, threads: Short = 0, sockets: Short = 0): Vector[String] =
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

def setDisk_floppy(file: String, id: Byte): Vector[String] =
  val arg =
    if id == 0 then Vector("-fda", file)
    else Vector("-fdb", file)
  if isFileOk(file) then arg else Vector()

def setDisk_drive(file: String, id: Byte): Vector[String] =
  val arg =
    id match
      case 0 => Vector("-hda", file)
      case 1 => Vector("-hdb", file)
      case 2 => Vector("-hdc", file)
      case 3 => Vector("-hdd", file)
      case _ => Vector("-hda", file)
  if isFileOk(file) then arg else Vector()


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

def configureBoot(order: String, menu: Boolean = false): Vector[String] = //add splash image later
  def isOrderOk(i: Int = 0): Boolean =
    if i >= order.length then true
    else if "acdn".contains(order(i)) == false then false
    else isOrderOk(i+1)

  val arg_order = if isOrderOk() then s"order=$order" else ""
  val arg_menu = if menu then "menu=on" else "menu=off"
  Vector("-boot", mkarg(Vector(arg_order, arg_menu).filter(x => x.length != 0)))

def setAudio(driver: String, model: String): Vector[String] = //figure out -audiodev later
  Vector("-audio", s"driver=$driver,model=$model")

def setNetwork(backend: String = "user", model: String = "virtio-net-pci"): Vector[String] =
  Vector("-nic", s"$backend,model=$model") //finish
