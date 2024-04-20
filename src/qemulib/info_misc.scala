package qemulib

private def stdout_to_vector(stdout: String, line: String = "", seq: Vector[String] = Vector(), i: Int = 0): Vector[String] = //only add starting from display devices, for optimization
  if i >= stdout.length then
    if line == "" then seq else seq :+ line
  else if stdout(i) == '\n' then
    stdout_to_vector(stdout, "", seq :+ line, i+1)
  else
    stdout_to_vector(stdout, line + stdout(i), seq, i+1)

private def getDevice(line: String, dev: String = "", copy: Boolean = false, i: Int = 0): String =
  if i >= line.length then dev
  else if line(i) == '\"' then
    if copy then dev else getDevice(line, dev, true, i+1)
  else if copy then getDevice(line, dev + line(i), copy, i+1)
  else getDevice(line, dev, copy, i+1)


private def filter_stdout_seq(stdout: Vector[String], keyline: String, quotation_aware: Boolean, devlist: Vector[String] = Vector(), copy: Boolean = false, i: Int = 0): Vector[String] =
  if i >= stdout.length || (stdout(i) == "" && copy) then devlist
  else if stdout(i) == keyline then filter_stdout_seq(stdout, keyline, quotation_aware, devlist, true, i+1)
  else if copy then
    val dev = if quotation_aware then getDevice(stdout(i)) else stdout(i)
    filter_stdout_seq(stdout, keyline, quotation_aware, devlist :+ dev, copy, i+1)
  else filter_stdout_seq(stdout, keyline, quotation_aware, devlist, copy, i+1)
