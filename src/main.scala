package virtpebble

import virtpebble.config.*

@main def main() =
  println("wow you made it this far")
  writeConfig_string("testconfig.txt", defaultconf_linux(), false)
  test()


def test() =
  val args = setQEMUArgs("testconfig.txt")
  println("///printing args///\n\n")
  for a <- args do print(s"$a ")
