{ pkgs ? (import <nixpkgs> { }), ... }:

with pkgs;
mkShell {
  name = "petclinic";
  buildInputs = [ jdk ];
  shellHook = ''
    export JAVA_HOME=${jdk}
    java -version
  '';
}
