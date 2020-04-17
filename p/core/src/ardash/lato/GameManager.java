package ardash.lato;

import java.util.Collection;

import com.badlogic.gdx.Screen;

import ardash.lato.terrain.TerrainManager;
import ardash.lato.terrain.TerrainSeg;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

public class GameManager {
	
	public final LatoGame game;
	public final Assets assets;
	public final AnnotationAssetManager am;
	public TerrainManager tm;

	public GameManager(LatoGame game) {
		this.game = game;
		this.assets = new Assets();
		this.am = assets.manager;
		assets.loadAll();
		this.tm = new TerrainManager();
	}
	
	public Screen getScreen() {
		return game.getScreen();
	}

	public GameScreen getGameScreen() {
		return (GameScreen)game.getScreen();
	}

}
