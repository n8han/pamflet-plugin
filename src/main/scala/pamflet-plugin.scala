package pamflet

import sbt._
import Keys._
import Defaults._

object PamfletPlugin extends Plugin {
  object pamflet {
    val docs = SettingKey[File]("pamflet-docs")
    val properties = SettingKey[File]("pamflet-properties")
    val output = SettingKey[File]("pamflet-output")
    val storage = SettingKey[Storage]("pamflet-storage")
    val server = SettingKey[unfiltered.jetty.Http]("pamflet-server")
    val start = TaskKey[Unit]("start-pamflet")
    val stop = TaskKey[Unit]("stop-pamflet")
    val write = TaskKey[Unit]("write-pamflet")

    val baseSettings: Seq[Project.Setting[_]] = Seq(
      docs <<= baseDirectory / "docs",
      properties <<= docs / "template.properties",
      output <<= target / "docs",
      storage <<= (docs, properties) {
        (docs, properties) =>
          FileStorage(docs, StringTemplate(properties))
      },
      server <<= (storage) { (storage) =>
        Preview(storage.contents)
      },
      start <<= startPamfletTask,
      stop <<= stopPamfletTask,
      write <<= writePamfletTask
    )

    private def startPamfletTask = (server) map { (server) =>
      server.start
      unfiltered.util.Browser.open(
        "http://127.0.0.1:%d/".format(server.port)
      )
      ()
    }

    private def stopPamfletTask = (server) map { (server) =>
      server.stop
      ()
    }

    private def writePamfletTask = (output, storage) map {
      (output, storage) =>
        sbt.IO.createDirectory(output)
        Produce(storage.contents, output)
    }
  }
  
  override lazy val settings = pamflet.baseSettings 
    // FIXME: seems that it's not working in sbt 0.11 
    // ++ Seq(
    //   docs, properties, output, storage,
    //   server, start, stop, write
    // ).map(s => (aggregate in s) := false)
}
