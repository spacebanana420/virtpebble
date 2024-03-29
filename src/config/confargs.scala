package virtpebble.config

import qemulib.*

def getAccel(cfg: Seq[String]): String = getFirstValue(cfg, "accel=")
def getCPU(cfg: Seq[String]): List[String] = getValues(cfg, "cpu=")
def getRAM(cfg: Seq[String]): String = getFirstValue(cfg, "ram=")

def getDrives_HD(cfg: Seq[String]): List[String] = getValues(cfg, "hd=")
def getDrives_floppy(cfg: Seq[String]): List[String] = getValues(cfg, "fd=")
def getDrives_cdrom(cfg: Seq[String]): String = getFirstValue(cfg, "cdrom=")

def getDrives(cfg: Seq[String]): List[String] = //finish
  val drives = cfg.filter(x => x.contains("drive="))

def getBoot(cfg: Seq[String]): List[String] = getValues(cfg, "boot=")
def getVGA(cfg: Seq[String]): String = getFirstValue(cfg, "vga=")

def getAudio(cfg: Seq[String]): List[String] = getValues(cfg, "audio=")
def getNet(cfg: Seq[String]): List[String] = getValues(cfg, "net=")

def setQEMUArgs(cfile: String): Vector[String] = //do error checks and multi param checks
  def convertValue(v: String): String = //not used yet
    if v == "none" then "" else v

  val conf = readConfig(cfile) //check if it exists

  val accel = getAccel(conf)
  val cpu = getCPU(conf).map(x => x.toShort) //can crash
  val ram = getRAM(conf).toInt
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
  val arg_ram = setRAM(ram(0))
  val arg_hd = setDisk_drive(hd(0), 0) //make recursive function for this
  val arg_fd = setDisk_floppy(fd(0), 0)
  val arg_cdrom = setDisk_cdrom(cdrom)
  //do for all drives too
  val arg_boot = configureBoot(boot(0), boot(1).toBoolean, boot(2))
  val arg_vga = setGraphicsMode(vga)
  val arg_audio = setAudio(audio(0), audio(1))
  val arg_net = setNetwork(net(0), net(1))

  arg_accel ++ arg_cpu ++ arg_hd ++ arg_fd ++ arg_cdrom ++ arg_boot ++ arg_vga ++ arg_audio ++ arg_net
