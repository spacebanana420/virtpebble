package virtpebble

import virtpebble.config.setQEMUArgs
import qemulib.qemu_run
import qemulib.qemuimage_create

def runQEMU(conf: String) = //add support for more architectures through a config setting
  val args = setQEMUArgs(conf: String)
  qemu_run(args)

def createImage(path: String, size: Int, format: String) =
  qemuimage_create(path, size, format)
