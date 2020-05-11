{ pkgs ? (import <nixpkgs> { }), ... }:

let
  nur = if ! builtins.hasAttr "nur" pkgs then
    import (builtins.fetchTarball
      "https://github.com/nix-community/NUR/archive/master.tar.gz") {
        inherit pkgs;
      }
  else
    pkgs.nur;
  jdk14 = if ! builtins.hasAttr "jdk14" pkgs then
    nur.repos.moaxcp.adoptopenjdk-hotspot-bin-14
  else
    pkgs.jdk14;
in pkgs.mkShell {
  name = "petclinic";
  buildInputs = [ jdk14 ];
  shellHook = ''
    export JAVA_HOME=${jdk14}
    java -version
  '';
}
