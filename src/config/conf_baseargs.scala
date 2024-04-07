package virtpebble.config

import java.io.File


def getVMpath(cfg: Seq[String] = Vector()): String =
  if cfg.length > 0 then getFirstValue(cfg, "vmpath=")
  else getFirstValue(readConfig_base("config.txt"), "vmpath=")

def getVMEntries(path: String): Array[String] =
  File(path).list().filter(x => File(s"$path/$x").isFile() && x.contains("_vm.txt"))
