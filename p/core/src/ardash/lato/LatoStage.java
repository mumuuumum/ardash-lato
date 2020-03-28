package ardash.lato;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.dermetfan.gdx.assets.AnnotationAssetManager;

public class LatoStage extends Stage {

	public final GameScreen screen;
//	public final LatoGame game;
	public final Assets assets;
	public final AnnotationAssetManager am;
	public final GameManager gm;

	public LatoStage(Viewport vp, GameScreen gameScreen) {
		super(vp);
		this.screen = gameScreen;
//		this.game = screen.;
		this.assets = screen.assets;
		this.am = screen.am;
		this.gm = screen.gm;
	}

	@Override
	/**
	 * to handle inputs
	 */
	public void draw() {
		super.draw();
		OrthographicCamera cam = (OrthographicCamera)getCamera();
		if (Gdx.input.isKeyPressed(Keys.Z))
		{
			cam.zoom+=0.1f;
		}
		if (Gdx.input.isKeyPressed(Keys.X))
		{
			cam.zoom-=0.1f;
			if (cam.zoom < 0f)
			{
				cam.zoom =0f;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT))
		{
			cam.translate(-1.1f, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
		{
			cam.translate(1.1f, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.UP))
		{
			cam.translate(0, 1.1f);
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN))
		{
			cam.translate(0, -1.1f);
		}
	}

	@Override
	public void dispose() {
		// parent implementation clears root, so call that after we disposed all content
		clearAndDisposeActors(getRoot());
		super.dispose();
	}

	/**
	 * Removes the given actor from the given group, and disposes the actor. Also,
	 * if the actor itself is a Group, its children are cleared and disposed
	 * recursively.
	 * 
	 * @param actor
	 * @param group
	 */
	public static void removeAndDispose(Actor actor, Group group) {
		if (actor instanceof Group) {
			clearAndDisposeActors((Group) actor);
		}
		group.removeActor(actor);
		disposeObject(actor);
	}

	public static void clearAndDisposeActors(Group group) {
		for (Actor actor : group.getChildren().items) {
			removeAndDispose(actor, group);
		}
		group.clearChildren();
	}

	public static void disposeObject(Object object) {
		if (object instanceof Disposable) {
			((Disposable) object).dispose();
		}
	}
}
