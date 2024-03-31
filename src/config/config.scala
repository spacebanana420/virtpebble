package virtpebble.config

//import tanuki.misc.similarInList
import java.io.File
import java.io.FileOutputStream
import scala.io.Source


def configExists(conf: String): Boolean = File(conf).isFile()

private def isSetting(line: String, keywords: Seq[String], i: Int = 0): Boolean =
  def startsWith(key: String, c: Int = 0): Boolean =
    if c >= key.length then true
    else if line.length <= key.length || line(c) != key(c) then false //excludes empty configurations too
    else startsWith(key, c+1)

  if i >= keywords.length then false
  else if startsWith(keywords(i)) then true
  else isSetting(line, keywords, i+1)

def readConfig(conf: String): Vector[String] =
  val settings =
    Vector(
    "name=", "accel=", "cpu=", "ram=",
    "hd=", "fd=", "cdrom=",
    "drive=", "boot=",
    "vga=",
    "audio=", "net="
    )
  val src = Source.fromFile(conf)
  val cfg = src
    .getLines()
    .filter(x => x.length > 0 && isSetting(x, settings) && x(0) != '#')
    .toVector
  src.close()
  cfg

private def getValue(l: String, setting: String, tmp: String = "", value: String = "", i: Int = 0): String =
  if i >= l.length || (i >= setting.length && setting != tmp) then
    value
  else if tmp == setting then
    getValue(l, setting, tmp, value + l(i), i+1)
  else
    getValue(l, setting, tmp + l(i), value, i+1)

private def getValues(cfg: Seq[String], setting: String, vals: Vector[String] = Vector(), i: Int = 0): Vector[String] =
  if i >= cfg.length then
    vals
  else
    val value = getValue(cfg(i), setting)
    if value != "" then
      getValues(cfg, setting, vals :+ value, i+1)
    else
      getValues(cfg, setting, vals, i+1)

private def getFirstValue(cfg: Seq[String], setting: String, i: Int = 0): String =
  if i >= cfg.length then
    ""
  else
    val value = getValue(cfg(i), setting)
    if value != "" then
      value
    else
      getFirstValue(cfg, setting, i+1)


private def parseFirstValue(cfg: Seq[String], setting: String, i: Int = 0): Vector[String] =
  parseEntry(getFirstValue(cfg, setting, i))

// def getStartCmd(cfg: Seq[String]): List[String] = parseCommand(getFirstValue(cfg, "sidecommand_start="))
// def getCloseCmd(cfg: Seq[String]): List[String] = parseCommand(getFirstValue(cfg, "sidecommand_close="))

// private def parseCommand(cmd: String, arg: String = "", cmdl: List[String] = List(), i: Int = 0): List[String] =
//   if i >= cmd.length then
//     if arg == "" then cmdl else cmdl :+ arg
//   else if cmd(i) == ' ' then
//     parseCommand(cmd, "", cmdl :+ arg, i+1)
//   else
//     parseCommand(cmd, arg + cmd(i), cmdl, i+1)

def parseEntry(entry: String, e1: String = "", e2: String = "", i: Int = 0, first: Boolean = true): Vector[String] =
  if i >= entry.length then
    Vector(e1, e2)
  else if entry(i) == ':' && first then
    parseEntry(entry, e1, e2, i+1, false)
  else if first then
    parseEntry(entry, e1 + entry(i), e2, i+1, first)
  else
    parseEntry(entry, e1, e2 + entry(i), i+1, first)
