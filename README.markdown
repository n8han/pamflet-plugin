This is a plugin for previewing [Pamflet][pf] project documentation.
Install it as a global plugin built from source to use it with
any project that has a Pamflet docs directory.

[pf]: http://pamflet.databinder.net/

**~/.sbt/plugins/project/build.scala**

```scala
import sbt._
object PluginDef extends Build {
  lazy val root = Project("plugins", file(".")) dependsOn( 
    pamflet)
  lazy val pamflet = uri("git://github.com/n8han/pamflet-plugin#0.3.0")
}
```
