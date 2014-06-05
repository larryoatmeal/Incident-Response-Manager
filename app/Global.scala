import play.api.Application
import play.api.GlobalSettings
import java.io.File
import play.api._
import com.typesafe.config.ConfigFactory

object Global extends GlobalSettings {
	override def onLoadConfig(config: Configuration, path: File, classloader: ClassLoader, mode: Mode.Mode): Configuration = {
    	val configFileName = System.getProperty("config.file")
		println(s"mode = ${mode}, config = ${configFileName}")
		// do more clever things here with the configuration, if necessary
		config
    }
}