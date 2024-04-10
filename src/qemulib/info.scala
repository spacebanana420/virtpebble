package qemulib

import scala.sys.process.*

private def getHostOS = System.getProperty("os.name")
private def getHostArch = System.getProperty("os.arch")

//Some stdouts require special parsing, for those, the parsing is inside the function. Otherwise use simpleParse

private def simpleParse(stdout: String, s: String = "", seq: Vector[String] = Vector(), i: Int = 0, first: Boolean = true): Vector[String] =
  if i >= stdout.length then
    seq
  else if stdout(i) == '\n' then
    if first then simpleParse(stdout, "", seq, i+1, false)
    else simpleParse(stdout, "", seq :+ s, i+1, first)
  else
    simpleParse(stdout, s + stdout(i), seq, i+1, first)

//needs fixing
def getCPUList(exec: String = "qemu-system-x86_64"): Vector[String] =
  def parse(stdout: String, s: String = "", seq: Vector[String] = Vector(), i: Int = 0): Vector[String] =
    if i >= stdout.length || s.contains("Recognized CPUID") then
      seq
    else if stdout(i) == '\n' || (stdout(i) == ' ' && s.contains(' ')) then //excludes cpu model descriptions too
      if s.contains("Available CPUs:") || s == "" then parse(stdout, "", seq, i+1)
      else parse(stdout, "", seq :+ s, i+1)
    else
      parse(stdout, s + stdout(i), seq, i+1)

  val stdout = Vector(exec, "-cpu", "help").!!
  parse(stdout)

def getAccelerators(exec: String = "qemu-system-x86_64"): Vector[String] =
  val stdout = Vector(exec, "-accel", "help").!!
  simpleParse(stdout)

def getGraphicalAccelerators(exec: String = "qemu-system-x86_64"): Vector[String] =
  def parse(stdout: String, s: String = "", seq: Vector[String] = Vector(), i: Int = 0, copy: Boolean = true): Vector[String] =
    if i >= stdout.length then
      seq
    else if stdout(i) == '\n' then parse(stdout, "", seq :+ s, i+1, true)
    else if stdout(i) == ' ' then parse(stdout, s, seq, i+1, false)
    else if copy then
      parse(stdout, s + stdout(i), seq, i+1, copy)
    else
      parse(stdout, s, seq, i+1, copy)

  val stdout = Vector(exec, "-vga", "help").!!
  parse(stdout)

def getMachines(exec: String = "qemu-system-x86_64"): Vector[String] =
  val stdout = Vector(exec, "-machine", "help").!!
  simpleParse(stdout)

def getAudioDrivers(exec: String = "qemu-system-x86_64"): Vector[String] =
  val stdout = Vector(exec, "-audio", "driver=help").!!
  simpleParse(stdout)

def getAudioModels(exec: String = "qemu-system-x86_64"): Vector[String] =
  def parse(stdout: String, first: Boolean, s: String = "", seq: Vector[String] = Vector(), i: Int = 0, copy: Boolean = true): Vector[String] =
    if i >= stdout.length then
      seq
    else if stdout(i) == '\n' then
      if s != "" then parse(stdout, false, "", seq :+ s, i+1, true)
      else parse(stdout, false, "", seq, i+1, true)
    else if stdout(i) == ' ' then parse(stdout, first, s, seq, i+1, false)
    else if copy && !first then
      parse(stdout, first, s + stdout(i), seq, i+1, copy)
    else
      parse(stdout, first, s, seq, i+1, copy)

  val stdout = Vector(exec, "-audio", "model=help").!!
  parse(stdout, true)

def getNetInfo(exec: String = "qemu-system-x86_64"): String = //not parsing this at least for now
  Vector(exec, "-nic", "help").!!
