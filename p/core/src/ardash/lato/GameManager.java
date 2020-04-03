package ardash.lato;

import com.badlogic.gdx.Screen;

import net.dermetfan.gdx.assets.AnnotationAssetManager;

public class GameManager {
	
	public final LatoGame game;
	public final Assets assets;
	public final AnnotationAssetManager am;

	public GameManager(LatoGame game) {
		this.game = game;
		this.assets = new Assets();
		this.am = assets.manager;
		assets.loadAll();
	}
	
	public Screen getScreen() {
		return game.getScreen();
	}

	public GameScreen getGameScreen() {
		return (GameScreen)game.getScreen();
	}

}
