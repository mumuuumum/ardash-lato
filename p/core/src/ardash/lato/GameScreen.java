package ardash.lato;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ardash.lato.Assets.SceneTexture;
import ardash.lato.actors.MountainRange;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

public class GameScreen implements Screen {
	
//	The world adjusted to MDPI/10 , the smallest expectable device.
	public static float WORLD_WIDTH=80; // visible meters from left to right, with default zoom on smallest display
	public static float WORLD_HEIGHT=60;
	public static float SNOWBOARD_LENGTH=1.85f; // length of snowboard in meters 

	/**
	 * The actual world-width in the viewport can be taken from Stage.Viewport.WorldWidth
	 */
	public static final float MAX_WORLD_WIDTH=WORLD_HEIGHT*(19f/9f); // on 19/9 display , we don't need to draw farer than this
	public static float CURRENT_WORLD_WIDTH=MAX_WORLD_WIDTH;      // current on display , we don't need to draw farer than this

	public GameManager gm;
	public AnnotationAssetManager am;
	public Assets assets;
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
		stage = new LatoStage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT), this);
		CURRENT_WORLD_WIDTH = stage.getViewport().getWorldWidth();
//		stage.setDebugAll(true);
		Gdx.input.setInputProcessor(stage);

		for (int i = 0; i<4 ; i++)
		{
			MountainRange mr = new MountainRange(10);
			stage.addActor(mr);
			mr.init();
//			mr.moveBy(50, 60);
//			mr.setSpeed(20f);
			
			// fog layer
			Image fog = new Image(assets.getSTexture(SceneTexture.FOG_PIX));
			fog.setSize(2048, 2048); // TODO reduce to display size
			stage.addActor(fog);
//			fog.setColor(1f, 0.9f, 0.9f, 0.125f);
			fog.setColor(EnvColors.DAY.fog);
			fog.getColor().a = 0.225f;
			
			// range offset
			mr.moveBy(-20f*(i+1), -4f*i);
			mr.setSpeed((i*i+1)*0.9f);
			
			// the collection a bit higher
			mr.moveBy(0,10f);
		}

	}

	@Override
	public void render(float delta) {
		//draw something nice to look at
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	stage.act(delta);
    	stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		
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
		stage.dispose();
		
	}

}
