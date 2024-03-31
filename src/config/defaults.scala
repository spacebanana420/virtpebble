package virtpebble.config

def defaultconf_linux(): String =
  "accel=kvm\ncpu=1\nram=1024\nvga=qxl\nboot=ac:true\nvga=qxl\nnet=user:virtio-net-pci"
