package virtpebble.config

import java.io.File
import java.io.FileOutputStream

def createBaseConfig(usedefaults: Boolean = false) =
  val config = FileOutputStream("config.txt")
  if usedefaults then config.write(defaultbaseconf().getBytes())

def writeBaseConfig(conf: String) = FileOutputStream("config.txt").write(conf.getBytes())

def createConfig(conf: String) =
  val config = FileOutputStream(conf)
//   if system_platform == 1 || system_platform == 2 then
//     config.write("command=wine".getBytes())

def writeConfig(conf: String, cfg: Seq[String], append: Boolean = true) =
  def createStr(str: String = "", i: Int = 0): String =
    if i >= cfg.length then
      str
    else if cfg(i) == "" then
      createStr(str, i+1)
    else
      createStr(str + cfg(i) + '\n', i+1)

  val cfgstr = createStr()
  if cfgstr != "" then
    FileOutputStream(conf, append).write(cfgstr.getBytes())

def writeConfig_string(conf: String, cfg: String, append: Boolean = true) =
  if cfg != "" then
    FileOutputStream(conf, append).write(cfg.getBytes())

def writeConfig_bytes(conf: String, cfg: Array[Byte], append: Boolean = true) =
  if cfg.length > 0 then
    FileOutputStream(conf, append).write(cfg)
