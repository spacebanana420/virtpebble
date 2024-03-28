{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  packages = with pkgs; [
    #Base packages
      scala_3
      scala-cli
      git
      p7zip
    #Runtime dependencies
      qemu
    #Native compilation
      graalvm-ce
      gcc
      glibc
      zlib
  ];
}
