package virtpebble

import virtpebble.config.*, virtpebble.setup.*
import bananatui.*
import scala.sys.exit

@main def main() =
  val green = foreground("green"); val default = foreground("default")
  if !configExists("config.txt") then createConfig("config.txt")

  val opts = Vector("Run a virtual machine", "Create a virtual machine", "Delete a virtual machine", "Manage disk images", "Configure virtpebble")
  val ans = chooseOption(opts, s"$green===Virtpebble v0.1-dev===$default\n\nChoose an option", "Exit")
  ans match
    case 0 => exit()
    case 1 => vmLoader()
    case 2 => setupVM()
    case 3 =>
    case 4 =>
//   writeConfig_string("testconfig.txt", defaultconf_linux(), false)
//   test()


// def test() =
//   val args = setQEMUArgs("testconfig.txt")
//   println("///printing args///\n\n")
//   for a <- args do print(s"$a ")
