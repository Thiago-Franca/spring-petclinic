{ pkgs ? (import <nixpkgs> { }), ... }:

with pkgs;
mkShell {
  name = "petclinic";
  buildInputs = [ jdk11 ];
  shellHook = ''
    export JAVA_HOME=${jdk11}
    java -version
  '';
}
