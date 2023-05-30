package ardash.lato.actors;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import ardash.lato.GameManager;
import ardash.lato.GameScreen;
import ardash.lato.LatoGame;

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
	
	public default GameScreen getGameScreen()
	{
		return getGameManager().getGameScreen();
	}
	
	public default Actor spawnFlareInForeground(Actor emitter, float size)
	{
		return getGameScreen().flarePlane.spawnFlare(emitter, size);
	}
	
	public Stage getStage();
	
}
