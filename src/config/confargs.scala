package virtpebble.config

import qemulib.*

private def getVal_string(opt: String): String =
  if opt != "none" then opt else ""

private def getVal_int(opt: String): Int =
  try opt.toInt
  catch case e: Exception => 0

private def getVal_short(opt: String): Short =
  try opt.toShort
  catch case e: Exception => 0

private def getVal_boolean(opt: String): Boolean =
  val l_opt = opt.toLowerCase()
  if l_opt == "true" || l_opt == "yes" then true else false

private def fill_string(opt: Vector[String], max: Int): Vector[String] =
  if opt.length >= max then opt
  else fill_string(opt :+ "", max)

private def fill_int(opt: Vector[Int], max: Int): Vector[Int] =
  if opt.length >= max then opt
  else fill_int(opt :+ 0, max)

private def fill_short(opt: Vector[Short], max: Int): Vector[Short] =
  if opt.length >= max then opt
  else fill_short(opt :+ 0, max)

def getAccel(cfg: Seq[String]): String = getFirstValue(cfg, "accel=")

def getCPU(cfg: Seq[String]): Vector[Short] =
  val cpu = parseFirstValue(cfg, "cpu=").map(x => getVal_short(x))
  fill_short(cpu, 3)

def getRAM(cfg: Seq[String]): Int =
  val ram = getFirstValue(cfg, "ram=")
  getVal_int(ram)

def getDrives_HD(cfg: Seq[String]): Vector[String] = fill_string(parseFirstValue(cfg, "hd="), 4)
def getDrives_floppy(cfg: Seq[String]): Vector[String] = fill_string(parseFirstValue(cfg, "fd="), 2)
def getDrives_cdrom(cfg: Seq[String]): String = getFirstValue(cfg, "cdrom=")

def getDrives(cfg: Seq[String]): Vector[Vector[String]] = //test
  def parse(drives: Vector[String], parsed: Vector[Vector[String]] = Vector(), i: Int = 0): Vector[Vector[String]] =
    if i >= drives.length then parsed
    else
      val dsettings = fill_string(parseEntry(drives(i)), 2)
      parse(drives, parsed :+ dsettings, i+1)

  parse(getValues(cfg, "drive="))

def getBoot(cfg: Seq[String]): Vector[String] =
  val boot = fill_string(parseFirstValue(cfg, "boot="), 3)
  Vector(boot(0), boot(1), boot(2))

def getVGA(cfg: Seq[String]): String = getFirstValue(cfg, "vga=")

def getAudio(cfg: Seq[String]): Vector[String] = fill_string(parseFirstValue(cfg, "audio="), 2)
def getNet(cfg: Seq[String]): Vector[String] = fill_string(parseFirstValue(cfg, "net="), 2)

def setQEMUArgs(cfile: String): Vector[String] =
  def getDrivesArgs(d: Vector[Vector[String]], args: Vector[String] = Vector(), i: Int = 0): Vector[String] =
    if i >= d.length then args
    else
      val drive_args = addDrive(d(i)(0), getVal_boolean(d(i)(1)))
      getDrivesArgs(d, args ++ drive_args, i+1)

  val conf = readConfig_VM(cfile) //check if it exists

  val accel = getAccel(conf)
  val cpu = getCPU(conf)
  val ram = getRAM(conf)
  val hd = getDrives_HD(conf)
  val fd = getDrives_floppy(conf)
  val cdrom = getDrives_cdrom(conf)
  val drives = getDrives(conf)
  val boot = getBoot(conf)
  val vga = getVGA(conf)
  val audio = getAudio(conf)
  val net = getNet(conf)

  val arg_accel = setAccel(accel)
  val arg_cpu = setCPU(cpu(0), cpu(1), cpu(2))
  val arg_ram = setRAM(ram)
  val arg_hd = setDisks_drive(hd(0), hd(1), hd(2), hd(3))
  val arg_fd = setDisks_floppy(fd(0), fd(1))
  val arg_cdrom = setDisk_cdrom(cdrom)
  val arg_drives = getDrivesArgs(drives)
  //do for all drives too
  val arg_boot = configureBoot(boot(0), getVal_boolean(boot(1)), boot(2))
  val arg_vga = setGraphicsMode(vga)
  val arg_audio = setAudio(audio(0), audio(1))
  val arg_net = setNetwork(net(0), net(1))

  arg_accel ++ arg_cpu ++ arg_hd ++ arg_fd ++ arg_cdrom ++ arg_drives ++ arg_boot ++ arg_vga ++ arg_audio ++ arg_net
