package qemulib

import scala.sys.process.*
import java.io.File

private def exec_command(path: String, args: Seq[String], quiet: Boolean = true) =
  val cmd = Vector(path) ++ args
  if quiet then
    cmd.run(ProcessLogger(line => ()))
  else
    cmd.run()

// private def verifyExec() =
//   val arches =

def qemu_run(args: Seq[String] = Vector(), exec: String = "qemu-system-x86_64", quiet: Boolean = true) =
  exec_command(exec, args, quiet)
