package ardash.lato.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

import ardash.lato.LatoGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280; // PHONE SIZE (Ifor screenshots)
		config.height = 720; // PHONE SIZE
		

		
        Settings settings = new Settings();
        settings.maxWidth = 2048;
        settings.maxHeight = 2048;
//		TexturePacker.process(settings, "./art/sprites/ui", "./android/assets", "ui");
		TexturePacker.process(settings, "../../sprites/scene", "../android/assets", "scene");
		TexturePacker t; // keep this here, otherwise import gets always removed
		
		new LwjglApplication(new LatoGame(), config);
	}
}
