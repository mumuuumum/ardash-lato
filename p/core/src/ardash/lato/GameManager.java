package ardash.lato;

import java.util.Collection;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.PerformanceCounters;

import ardash.lato.terrain.TerrainManager;
import ardash.lato.terrain.TerrainSeg;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

public class GameManager {
	
	public final LatoGame game;
	public final Assets assets;
	public final AnnotationAssetManager am;
	public TerrainManager tm;
	public PerformanceCounters performanceCounters = new PerformanceCounters();

	/**
	 * Indicates if forward movement is going on. User must tap initially to start and movement will end after crash.
	 */
	private boolean started;

	public GameManager(LatoGame game) {
		this.game = game;
		this.assets = new Assets();
		this.am = assets.manager;
		assets.loadAll();
		this.tm = new TerrainManager();
		reset();
	}
	
	private void reset() {
		tm.reset();
		started = false;
	}

	public Screen getScreen() {
		return game.getScreen();
	}

	public GameScreen getGameScreen() {
		return (GameScreen)game.getScreen();
	}
	
	public void setStarted(boolean started) {
		this.started = started;
	}
	
	public boolean isStarted()
	{
		return started;
	}

}
