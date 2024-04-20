package qemulib.misc

import java.io.File

def mkarg(opts: Vector[String], str: String = "", i: Int = 0): String = //grouping up multiple options like user,driver=something
  if i >= opts.length then str
  else if i == opts.length-1 then mkarg(opts, str + opts(i), i+1)
  else mkarg(opts, s"$str${opts(i)},", i+1)
