package virtpebble.setup

import qemulib.*
import virtpebble.config.getVMpath
import bananatui.*

import java.io.File, java.nio.file.Files, java.nio.file.Path

def diskimage_dir(): String = s"${getVMpath()}/disks"

def setup_manageDisks() =
  val disks_dir = diskimage_dir()
  val opts = Vector("Create a disk image", "Delete a disk image")
  val ans = chooseOption(opts, "Choose what to do")
  if ans != 0 then ans match
    case 1 => setup_createImage(disks_dir)
    case 2 => setup_deleteImage(disks_dir)

def setup_createImage(path: String) =
//   val title = s"${foreground("green")}[VM disk image creation]${foreground("default")}"
  val name = readUserInput("Type the name of your disk image")
  val size = readInt("Type the size of the disk image (megabytes)") //add a better tui function for this later

  val f = chooseOption_string(Vector("qcow2", "raw"), "Choose the disk format", "Default (qcow2)")
  val format = if f == "" then "qcow2" else f
  val extension = if format == "raw" then "img" else format
  qemuimage_create(s"$path/$name.$extension", size, format)


def setup_deleteImage(path: String) =
  val disks = File(path).list()

  val d = chooseOption_astring(disks, "Choose a disk image to delete", "Cancel")
  if d != "" && askPrompt(s"The image disk $d will be deleted permanently, this cannot be undone\nProceed?") then
    Files.delete(Path.of(s"$path/$d")) //todo: check if you have delete permission
