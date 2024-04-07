package virtpebble.setup

import qemulib.*
import bananatui.*

def setup_manageDisks() =
  val opts = Vector("Create a disk image")
  val ans = chooseOption(opts, "Choose what to do")
  ans match
    case 1 => setup_createImage()

def setup_createImage() =
//   val title = s"${foreground("green")}[VM disk image creation]${foreground("default")}"
  val path = chooseOption_dir("Type the path to create the disk imgae")
  val name = readUserInput("Type the name of your disk image")
  val size = readInt("Type the size of the disk image (megabytes)") //add a better tui function for this later

  val f = chooseOption_string(Vector("qcow2", "raw"), "Choose the disk format", "Default (qcow2)")
  val format = if f == "" then "qcow2" else f
  val extension = if format == "raw" then "img" else format
  qemuimage_create(s"$path/$name.$extension", size, format)
