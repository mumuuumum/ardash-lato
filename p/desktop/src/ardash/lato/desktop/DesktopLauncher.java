package ardash.lato.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

import ardash.lato.LatoGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// 16/9 = 1.77
		// 19/9 = 2.1111
		config.width = 480; // MDPI 1.33
		config.height = 360; // MDPI
//		config.width = 800; // HDPI 1.66
//		config.height = 480; // HDPI
		config.width = 1280; // XHDPI 1.77
		config.height = 720; // XHDPI
//		config.width = 1920/2; // XXHDPI 1.77
//		config.height = 1080/2; // XXHDPI
//		config.width = 2560/2; // XXXHDPI 1.77
//		config.height = 1440/2; // XXXHDPI
//		config.width = 3040/2; // 19/9 -> 2.11 max expectable screen aspect ratio
//		config.height = 1440/2; // XXXHDPI
		

		
        Settings settings = new Settings();
        settings.maxWidth = 2048;
        settings.maxHeight = 2048;
//		TexturePacker.process(settings, "./art/sprites/ui", "./android/assets", "ui");
//		TexturePacker.process(settings, "../../sprites/scene", "../android/assets", "scene");
		TexturePacker t; // keep this here, otherwise import gets always removed
		
		new LwjglApplication(new LatoGame(), config);
	}
}
