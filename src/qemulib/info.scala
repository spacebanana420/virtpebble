package qemulib

import scala.sys.process.*
import qemulib.misc.mkarg

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

private def mkMachineArg(machine: String): Vector[String] =
  if machine != "" && machine != "none" then Vector("-machine", machine) else Vector()

private def runInfoCmd(basecmd: Vector[String], machine: String): String =
  val cmd = basecmd ++ mkMachineArg(machine)
  cmd.!!

def supportedHostArchitectures(): Vector[String] = Vector("x86_64", "i386", "aarch64", "arm", "riscv64", "riscv32","ppc64", "ppc")

def supportedGuestArchitectures(): Vector[String] = Vector("x86_64", "i386", "aarch64", "arm", "riscv64", "riscv32","ppc64", "ppc", "alpha", "avr", "cris", "hppa", "loongarch64", "m68k", "microblaze", "microblazeel", "mips", "mips64", "mips64el", "mipsel", "nios2", "or1k", "rx", "s390x", "sh4", "sh4eb", "sparc", "sparc64", "tricore", "xtensa", "xtensaeb")

//needs fixing
def getCPUList(exec: String = "qemu-system-x86_64", machine: String = ""): Vector[String] =
  def parse(stdout: String, s: String = "", seq: Vector[String] = Vector(), i: Int = 0): Vector[String] =
    if i >= stdout.length || s.contains("Recognized CPUID") then
      seq
    else if stdout(i) == '\n' || (stdout(i) == ' ' && s.contains(' ')) then //excludes cpu model descriptions too
      if s.contains("Available CPUs:") || s == "" then parse(stdout, "", seq, i+1)
      else parse(stdout, "", seq :+ s, i+1)
    else
      parse(stdout, s + stdout(i), seq, i+1)
  val stdout = runInfoCmd(Vector(exec, "-cpu", "help"), machine)
  parse(stdout)

def getAccelerators(exec: String = "qemu-system-x86_64", machine: String = ""): Vector[String] =
  val stdout = runInfoCmd(Vector(exec, "-accel", "help"), machine)
  simpleParse(stdout)

def getGraphicalAccelerators(exec: String = "qemu-system-x86_64", machine: String = ""): Vector[String] =
  def parse(stdout: String, s: String = "", seq: Vector[String] = Vector(), i: Int = 0, copy: Boolean = true): Vector[String] =
    if i >= stdout.length then
      seq
    else if stdout(i) == '\n' then parse(stdout, "", seq :+ s, i+1, true)
    else if stdout(i) == ' ' then parse(stdout, s, seq, i+1, false)
    else if copy then
      parse(stdout, s + stdout(i), seq, i+1, copy)
    else
      parse(stdout, s, seq, i+1, copy)

  val stdout = runInfoCmd(Vector(exec, "-vga", "help"), machine)
  parse(stdout)

def getMachines(exec: String = "qemu-system-x86_64"): Vector[String] =
  val stdout = Vector(exec, "-machine", "help").!!
  simpleParse(stdout)

def getAudioDrivers(exec: String = "qemu-system-x86_64", machine: String = ""): Vector[String] =
  val stdout = runInfoCmd(Vector(exec, "-audio", "driver=help"), machine)
  simpleParse(stdout)

def getAudioModels(exec: String = "qemu-system-x86_64", machine: String = ""): Vector[String] =
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

  val stdout = runInfoCmd(Vector(exec, "-audio", "model=help"), machine)
  parse(stdout, true)

def getNetInfo(exec: String = "qemu-system-x86_64", machine: String = ""): String = //not parsing this at least for now
  runInfoCmd(Vector(exec, "-nic", "help"), machine)

def getNetModels(exec: String = "qemu-system-x86_64", machine: String = ""): Vector[String] =
  val stdout = stdout_to_vector(runInfoCmd(Vector(exec, "-nic", "help"), machine))
  filter_stdout_seq(stdout, "Available NIC models:", false)
def getDevices(exec: String = "qemu-system-x86_64", machine: String = ""): String =
  runInfoCmd(Vector(exec, "-device", "help"), machine)

def getDisplayDevices(exec: String = "qemu-system-x86_64", machine: String = ""): Vector[String] = //seems to be working
  val stdout = stdout_to_vector(getDevices(exec, machine))
  filter_stdout_seq(stdout, "Display devices:", true)
