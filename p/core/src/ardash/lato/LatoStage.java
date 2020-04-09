package ardash.lato;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;

import ardash.lato.actors.Performer;
import ardash.lato.actors.WaveDrawer;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

public class LatoStage extends Stage {

	public final GameScreen screen;
	public final Assets assets;
	public final AnnotationAssetManager am;
	public final GameManager gm;
	private Performer performer = null;
	private WaveDrawer waveDrawer = null;

	public LatoStage(Viewport vp, GameScreen gameScreen) {
		super(vp);
		this.screen = gameScreen;
		this.assets = screen.assets;
		this.am = screen.am;
		this.gm = screen.gm;
		getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false); // override super constructor
	}

	@Override
	/**
	 * to handle inputs
	 */
	public void draw() {
    	Gdx.gl20.glEnable(GL20.GL_BLEND);
    	Gdx.gl20.glBlendFunc(GL20.GL_ZERO, GL20.GL_ZERO);
		super.draw();
    	Gdx.gl20.glBlendFunc(GL20.GL_ZERO, GL20.GL_ZERO);

		// performer has moved, the camera shall follow on the y axis
		if (performer != null)
		{
//			System.out.print("cam Y: "+getCamera().position.y);
//			System.out.println(" per Y: "+performer.getY());
//			getCamera().position.y;
//			getCamera().translate(0, -(getCamera().position.y - performer.getY()), 0);
//			System.out.print("pcam Y: "+getCamera().position.y);
//			System.out.println("p per Y: "+performer.getY());
			
			// point camera to the camspot
			getCamera().translate(-(getCamera().position.x - performer.getCamSpot().x)
					, -(getCamera().position.y - performer.getCamSpot().y), 0);
//			getCamera().update();
//			getViewport().apply(false);
		}

		
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
		if (Gdx.input.isKeyJustPressed(Keys.NUM_1))
		{
			if (performer != null)
				performer.setSpeed(performer.getSpeed()-1);
		}
		if (Gdx.input.isKeyJustPressed(Keys.NUM_2))
		{
			if (performer != null)
				performer.setSpeed(performer.getSpeed()+1);
		}
		
//		System.out.print("ppos X: "+Gdx.input.getX());
//		System.out.println("ppos Y: "+Gdx.input.getY());

	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if (waveDrawer != null)
		{
			waveDrawer.updateTerrainSegments(performer.getX());
		}
		
	}
	
	public Actor getFirstHitActorAt(float screenX, float screenY)
	{
		Vector2 tempCoords = new Vector2();
		screenToStageCoordinates(tempCoords .set(screenX, screenY));
		Actor target = hit(tempCoords.x, tempCoords.y, true);
		return target;
	}
	
	/**
	 * Checks if the center of the Actor is covered by any other actor on the stage.
	 * This is like a realistic implementation of Actor.isHidden();
	 * @param a
	 * @return
	 */
	public boolean isActorCovered(Actor a)
	{
		if (a == null)
			throw new RuntimeException("a cannot be null");
		// Check if the first hit actor at the center coordinated is the actor itself. If not: it is covered by another Actor.
		Vector2 tempCoords = new Vector2();
		tempCoords.set(a.getX()+a.getWidth(), a.getY()+a.getHeight());
		a.localToScreenCoordinates(tempCoords);
		tempCoords.x = tempCoords.x;
		tempCoords.y = tempCoords.y;
		Actor firstHitActor = getFirstHitActorAt(tempCoords.x, tempCoords.y);
		if (firstHitActor == null)
			System.out.print("first hit on ["+tempCoords+"] : NONE" );
		else
			System.out.print("first hit on ["+tempCoords+"] :" + firstHitActor.getName());
			
		return a != firstHitActor;
	}

	public Performer getPerformer() {
		return performer;
	}

	public void setPerformer(Performer performer) {
		if (this.performer != null)
			throw new RuntimeException("Performer is already set for this stage.");
		this.performer = performer;
	}

	public WaveDrawer getWaveDrawer() {
		return waveDrawer;
	}

	public void setWaveDrawer(WaveDrawer waveDrawer) {
		this.waveDrawer = waveDrawer;
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
			Disposables.gracefullyDisposeOf((Disposable)object);
		}
	}
}
