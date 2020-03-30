package ardash.lato;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;

import ardash.lato.Assets.SceneTexture;
import ardash.lato.actors.MountainRange;
import ardash.lato.actors.Performer;
import ardash.lato.actors.WaveDrawer;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

public class GameScreen implements Screen {
	
//	The world adjusted to MDPI/10 , the smallest expectable device.
	public static float WORLD_WIDTH=35.59f; // visible meters from left to right, with default zoom on smallest display
	public static float WORLD_HEIGHT=26.76f;
	public static float SNOWBOARD_LENGTH=1.85f; // length of snowboard in meters 

	/**
	 * The actual world-width in the viewport can be taken from Stage.Viewport.WorldWidth
	 */
	public static final float MAX_WORLD_WIDTH=WORLD_HEIGHT*(19f/9f); // on 19/9 display , we don't need to draw farer than this
	public static float CURRENT_WORLD_WIDTH=MAX_WORLD_WIDTH;      // current on display , we don't need to draw farer than this

	public GameManager gm;
	public AnnotationAssetManager am;
	public Assets assets;
	public LatoStage backStage;
	public LatoStage stage;

	public GameScreen(GameManager gm) {
		this.gm = gm;
		this.am = gm.am;
		this.assets = gm.assets;
	}

	@Override
	public void show() {
		Texture ball = am.get(Assets.ball); // Assets.ball is a String
		
//		stage = new LatoStage(new FitViewport(480, 360), this);
		backStage = new LatoStage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT), this);
		stage = new LatoStage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT), this);
		CURRENT_WORLD_WIDTH = backStage.getViewport().getWorldWidth();
		stage.setDebugAll(true);
//		Gdx.input.setInputProcessor(backStage);

		for (int i = 0; i<4 ; i++)
		{
			MountainRange mr = new MountainRange(10);
			backStage.addActor(mr);
			mr.init();
//			mr.moveBy(50, 60);
//			mr.setSpeed(20f);
			
			// fog layer
			Image fog = new Image(assets.getSTexture(SceneTexture.FOG_PIX));
			fog.setSize(204, 204); // TODO reduce to display size
			backStage.addActor(fog);
//			fog.setColor(1f, 0.9f, 0.9f, 0.125f);
			fog.setColor(EnvColors.DAY.fog);
			fog.getColor().a = 0.225f;
			
			// range offset
			mr.moveBy(-MountainRange.MOUNT_SIZE*(i+1), -3f*i-4);
			mr.setSpeed((i*i+1)*0.2f);
			
			// the collection a bit higher
			mr.moveBy(0,10f);
		}
		
		// test to add shaperenderers
		WaveDrawer hl = new WaveDrawer(Color.WHITE);
		stage.addActor(hl);
		stage.setWaveDrawer(hl);
		
		// add performer
		Performer p = new Performer();
		stage.addActor(p);
		p.init();
//		p.moveBy(4*1.8f, 10f);
		p.moveBy(8*1.8f, 10f); // tmp becasue no starting groudn yet
		stage.setPerformer(p); // attach the camera to him
	}

	@Override
	public void render(float delta) {
		//draw something nice to look at
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	backStage.act(delta);
    	stage.act(delta);
    	
    	// move camera before drawing
//    	stage.getCamera().translate(0.1f, -0.15f, 0);
    	
    	backStage.draw();
    	stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		backStage.getViewport().update(width, height, true);
		stage.getViewport().update(width, height, false);		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		Disposables.gracefullyDisposeOf(backStage, stage);
	}

}
