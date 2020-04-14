package ardash.lato.actors;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import ardash.lato.Assets;
import ardash.lato.GameManager;
import ardash.lato.GameScreen;
import ardash.lato.LatoGame;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

/**
 * To be used for Actor instances.
 * @author z
 *
 */
public interface StageAccessor {
	public default GameManager getGameManager()
	{
		final ApplicationListener al = Gdx.app.getApplicationListener();
		LatoGame game = (LatoGame)al;
		return game.gm;
	}
	
	public default Assets getAssets()
	{
		return getGameManager().assets;
	}

	public default AnnotationAssetManager getAssetManager()
	{
		return getGameManager().am;
	}

	public default GameScreen getGameScreen()
	{
		return getGameManager().getGameScreen();
	}
	
	public default Actor spawnFlareInForeground(Actor emitter, float size)
	{
		return getGameScreen().flarePlane.spawnFlare(emitter, size);
	}
	
//	public default float getCenterX()
//	{
//		return getX()+getWidth();
//	}
//
//	public int getX();
//
//	public int getWidth();

	public Stage getStage();
	
//	/**
//	 * Call this after the Actor has been added to a stage.
//	 */
//	public void init();

}
