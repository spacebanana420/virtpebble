package virtpebble.config

def defaultconf_linux(): String =
  "accel=kvm\ncpu=1\nram=1024\nvga=qxl\nboot=ac:true\nvga=qxl\nnet=user:virtio-net-pci"

def defaultbaseconf(): String = s"arch=x86_64\nvmpath=${defaultVMPath()}"

def defaultVMPath(): String = "virtpebble_vms" //this function is standalone to keep maintainability sane
