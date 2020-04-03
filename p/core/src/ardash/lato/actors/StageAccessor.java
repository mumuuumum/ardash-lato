package ardash.lato.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import ardash.lato.Assets;
import ardash.lato.GameManager;
import ardash.lato.GameScreen;
import ardash.lato.LatoStage;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

/**
 * To be used for Actor instances.
 * @author z
 *
 */
public interface StageAccessor {
	public default GameManager getGameManager()
	{
		final Stage stage = getStage();
		if (stage == null)
			throw new RuntimeException("Actor must be added to a stage first.");
		if (stage instanceof LatoStage) {
			LatoStage ls = (LatoStage) stage;
			return ls.gm;
		}
		else
			throw new RuntimeException("Stage was wrong type.");
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
	
	/**
	 * Call this after the Actor has been added to a stage.
	 */
	public void init();

}
