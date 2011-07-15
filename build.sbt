name := "pamflet-plugin"

version := "0.2.4"

sbtPlugin := true

libraryDependencies <++= (version) { v => Seq(
  "net.databinder" %% "pamflet-library" % v
) }