package ardash.lato;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

import ardash.lato.LatoGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
@SuppressWarnings("unused")
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode((int)(2400*0.97), (int)(1080*0.97));
//		config.
//		config.width = 1280; // XHDPI 1.77
//		config.height = 720; // XHDPI
		// 16/9 = 1.77
		// 19/9 = 2.1111
//		config.width = 480; // MDPI 1.33
//		config.height = 360; // MDPI
////		config.width = 800; // HDPI 1.66
////		config.height = 480; // HDPI
//		config.width = 1280; // XHDPI 1.77
//		config.height = 720; // XHDPI
//		config.width = 1920/2; // XXHDPI 1.77
//		config.height = 1080/2; // XXHDPI
//		config.width = 2560/2; // XXXHDPI 1.77
//		config.height = 1440/2; // XXXHDPI
//		config.width = 3040/2; // 19/9 -> 2.11 max expectable screen aspect ratio
//		config.height = 1440/2; // XXXHDPI
		

		

		config.setForegroundFPS(60);
		config.setTitle("Lato");

        Settings settings = new Settings();
        settings.maxWidth = 2048;
        settings.maxHeight = 2048;
//		TexturePacker.process(settings, "./art/sprites/ui", "./android/assets", "ui");
//		TexturePacker.process(settings, "../../texturepacker/scene", "../assets", "scene");
		TexturePacker t; // keep this here, otherwise import gets always removed

		
		
		new Lwjgl3Application(new LatoGame(), config);
	}
}
