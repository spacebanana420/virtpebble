package qemulib

import scala.sys.process.*

private def getHostOS = System.getProperty("os.name")
private def getHostArch = System.getProperty("os.arch")

//not all stdouts require special parsing, this will do  the job
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

def getAudioDrivers(exec: String = "qemu-system-x86_64"): Vector[String] =
  val stdout = Vector(exec, "-audio", "driver=help").!!
  simpleParse(stdout)

def getAudioModels(exec: String = "qemu-system-x86_64"): Vector[String] =
  val stdout = Vector(exec, "-audio", "model=help").!!
  simpleParse(stdout)

def getNetInfo(exec: String = "qemu-system-x86_64"): String = //not parsing this at least for now
  Vector(exec, "-nic", "help").!!
