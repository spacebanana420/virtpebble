package virtpebble

import virtpebble.config.*, virtpebble.setup.*
import bananatui.*
import scala.sys.exit
import qemulib.check_qemu

@main def main(args: String*) =
  if !check_qemu() then noQEMU()
  else
    val debug = isDebugEnabled(args.toVector)
    while true do
      val green = foreground("green"); val default = foreground("default")
      if !configExists("config.txt") then createConfig("config.txt")
      checkConfig()

      val opts = Vector("Run a virtual machine", "Create a virtual machine", "Delete a virtual machine", "Manage disk images", "Configure virtpebble")
      val ans = chooseOption(opts, s"$green===Virtpebble v0.1.1===$default\n\nChoose an option", "Exit")
      ans match
        case 0 => exit()
        case 1 => vmLoader(debug)
        case 2 => setupVM()
        case 3 => setup_deleteVM()
        case 4 => setup_manageDisks()
        case 5 => setupBaseConfig()

def noQEMU() =
  readUserInput("You do not have QEMU installed in your system!\nQEMU is required to run your virtual machines.\n\nhttps://qemu.org")

def isDebugEnabled(args: Vector[String], i: Int = 0): Boolean =
  if i >= args.length then false
  else if args(i) == "--debug" then true
  else isDebugEnabled(args, i+1)

//   writeConfig_string("testconfig.txt", defaultconf_linux(), false)
//   test()


// def test() =
//   val args = setQEMUArgs("testconfig.txt")
//   println("///printing args///\n\n")
//   for a <- args do print(s"$a ")
