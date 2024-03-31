package virtpebble.setup

import bananatui.*
import java.io.File


// def create_config() =


def setupDrives(): Vector[String] = addDisks() :+ configureBoot()

def addDisks(disks: Vector[String] = Vector()): Vector[String] =
  def addDrive(mode: Int): String =
    val file = chooseOption_file("Type the path to the disk file in your system")
    mode match
      case 2 => s"cdrom=$file"
      case 3 => s"drive=$file:${!File(file).canWrite()}"
      case _ => s"hd=$file"

  val opts = Vector("Add main drive", "Add CDROM", "Add secondary drive")
  val ans = chooseOption(opts, "What disk to add?", "Done")
  if ans == 0 then disks else addDisks(disks :+ addDrive(ans))



def configureBoot(): String =
  val ans = chooseOption(Vector("Main drive", "CDROM"), "Choose what disk to prioritize on boot", "Default (Main drive)")
  val order =
    if ans == 0 || ans == 1 then
      "dc"
    else "cd"
  val menu = askPrompt("Do you want to enable the boot menu?")
  if askPrompt("Do you want to add a splash image to the boot menu?") then
    val splash = chooseOption_file("Add the path to an image file")
    s"boot=$order:$menu:$splash"
  else s"boot=$order:$menu"
  //add splash image
