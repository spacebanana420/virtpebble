package qemulib

import scala.sys.process.*
import java.io.File

private def exec_parallel(path: String, args: Seq[String], quiet: Boolean = true) =
  val cmd = Vector(path) ++ args
  if quiet then
    cmd.run(ProcessLogger(line => ()))
  else
    cmd.run()

private def exec_status(path: String, args: Seq[String], quiet: Boolean = true): Int =
  val cmd = Vector(path) ++ args
  if quiet then
    cmd.!(ProcessLogger(line => ()))
  else
    cmd.!

// private def exec_status(cmd: Seq[String], quiet: Boolean = true): Int =
//   if quiet then
//     cmd.!(ProcessLogger(line => ()))
//   else
//     cmd.!

private def exec(cmd: Seq[String], quiet: Boolean = false): Int =
  try if quiet then cmd.! else cmd.!(ProcessLogger(line => ()))
  catch case e: Exception => -1

private def exec_s(cmd: Seq[String]): String =
  try cmd.!!
  catch case e: Exception => ""

// private def verifyExec() =
//   val arches =

def check_qemu(path: String = "qemu-system-x86_64"): Boolean =
  val status = exec(Vector(path, "--version"), true)
  status == 0

def qemu_run(args: Seq[String] = Vector(), exec: String = "qemu-system-x86_64", quiet: Boolean = true) = //return the process trait
  exec_parallel(exec, args, quiet)

def qemuimage_create(path: String, size: Int, format: String = "") =
  val supportedFormats = Vector(
    "raw", "qcow2", "qed", "qcow", "luks", "vdi", "vmdk", "vpc", "VHDX",
    "bochs", "cloop", "dmg", "parallels"
    )
  val args = if format != "" && supportedFormats.contains(format) then
    Vector("-q", "-f", format, path, s"${size}M")
  else
    Vector("-q", path, s"${size}M")
  exec(Vector("qemu-img", "create") ++ args)

def qemuimage_resize(path: String, size: Int) =
  exec(Vector("qemu-img", "resize", "-q", path, s"${size}M"))

def qemuimage_resize_add(path: String, size: Int) =
  exec(Vector("qemu-img", "resize", "-q", path, s"+${size}M"))

def qemuimage_resize_lower(path: String, size: Int) =
  exec(Vector("qemu-img", "resize", "-q", path, s"-${size}M"))

def qemuimage_info(path: String): String =
  exec_s(Vector("qemu-img", "info", "-q", path))
