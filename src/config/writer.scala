package tanuki.config

//import tanuki.system_platform
import java.io.File
import java.io.FileOutputStream

def createConfig(conf: String) =
  val config = FileOutputStream(conf)
  if system_platform == 1 || system_platform == 2 then
    config.write("command=wine".getBytes())

def writeConfig(conf: String, cfg: List[String], append: Boolean = true) =
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
