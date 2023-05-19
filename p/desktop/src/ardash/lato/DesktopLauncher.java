package ardash.lato;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ardash.lato.LatoGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode((int)(2400*0.7), (int)(1080*0.7));
//		config.
//		config.width = 1280; // XHDPI 1.77
//		config.height = 720; // XHDPI

		config.setForegroundFPS(60);
		config.setTitle("Lato");
		new Lwjgl3Application(new LatoGame(), config);
	}
}
