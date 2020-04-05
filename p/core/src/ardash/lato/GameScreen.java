package ardash.lato;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.kiwi.util.gdx.scene2d.Actors;

import ardash.lato.Assets.SceneTexture;
import ardash.lato.actors.ColorChangeListener;
import ardash.lato.actors.FlarePlane;
import ardash.lato.actors.MountainRange;
import ardash.lato.actors.Performer;
import ardash.lato.actors.SkyPlane;
import ardash.lato.actors.WaveDrawer;
import ardash.lato.actors.WeatherProvider;
import box2dLight.RayHandler;
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
	public WeatherProvider weather;
	public Assets assets;
	public LatoStage backStage;
	public LatoStage stage;
	public LatoStage frontStage;
	private RayHandler rayHandler;
	public FlarePlane flarePlane;

	public GameScreen(GameManager gm) {
		this.gm = gm;
		this.am = gm.am;
		this.assets = gm.assets;
	}

	@Override
	public void show() {
		Texture ball = am.get(Assets.ball); // Assets.ball is a String

		weather = new WeatherProvider();
		backStage = new LatoStage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT), this);
		stage = new LatoStage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT), this);
		frontStage = new LatoStage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT), this);
		CURRENT_WORLD_WIDTH = backStage.getViewport().getWorldWidth();
//		stage.setDebugAll(true);
//		backStage.setDebugAll(true);
//		frontStage.setDebugAll(true);
//		Gdx.input.setInputProcessor(backStage);

		// additive flare plane (must be done first, so actors can spawn the flares)
		flarePlane = new FlarePlane(MAX_WORLD_WIDTH*2f,WORLD_HEIGHT);
		frontStage.addActor(flarePlane);
		flarePlane.init();

		
		final SkyPlane skyPlane = new SkyPlane(MAX_WORLD_WIDTH*2f,WORLD_HEIGHT);
		backStage.addActor(skyPlane);
		skyPlane.init();

		for (int i = 0; i<4 ; i++)
		{
			final int numMountains = 10;
			MountainRange mr = new MountainRange(numMountains);
			backStage.addActor(mr);
			mr.init();
			mr.setName("MountainRange"+i);
//			mr.moveBy(50, 60);
//			mr.setSpeed(20f);
			
			// fog layer
			final Image fog = new Image(assets.getSTexture(SceneTexture.FOG_PIX));
			fog.setSize(204, 204); // TODO reduce to display size
			backStage.addActor(fog);
//			fog.setColor(1f, 0.9f, 0.9f, 0.125f);
			fog.setColor(EnvColors.DAY.fog);
			fog.getColor().a = 0.225f;
			Actors.centerActor(fog);
			fog.setName("fog"+i);
			fog.setTouchable(Touchable.disabled);
//			fog.setVisible(false);
			
			// subscribe the fog layers to fog colour change
			weather.addFogColourChangeListener(new ColorChangeListener() {
				
				@Override
				public void onColorChangeTriggered(Color target, float seconds) {
					Color t = target.cpy();
					t.a = 0.225f;	
					fog.addAction(Actions.color(t, seconds));
				}
			});
			
			
			
			// range offset
			mr.moveBy(-MountainRange.MOUNT_SIZE*(i+1), -2f*i-4);
			mr.setSpeed((i*i+1)*0.2f);
			
			// the collection a bit higher
			mr.moveBy(0,-1f);
			
			// move to center on 0,0
			mr.moveBy(numMountains/2 * -MountainRange.MOUNT_SIZE,0);
			
		}
		
//		backStage.addActor(skyPlane);
//		skyPlane.init();
		
		// test to add shaperenderers
		WaveDrawer hl = new WaveDrawer(EnvColors.DAY.ambient);
		stage.addActor(hl);
		stage.setWaveDrawer(hl);
		
		// add performer
		Performer p = new Performer();
		stage.addActor(p);
		p.init();
//		p.moveBy(4*1.8f, 10f);
		p.moveBy(8*1.8f, 10f); // tmp becasue no starting groudn yet
		stage.setPerformer(p); // attach the camera to him
		
//		// add ambient light overlay
//		Image fog = new Image(assets.getSTexture(SceneTexture.FOG_PIX));
//		fog.setSize(204, 204); // TODO reduce to display size
//		stage.addActor(fog);
////		fog.setColor(1f, 0.9f, 0.9f, 0.125f);
//		fog.setColor(EnvColors.DAY.ambient.cpy());
//		fog.getColor().a = 0.7225f;
//		Actors.centerActor(fog);

//		Image fog = new Image(assets.getSTexture(SceneTexture.FLARE));
//		fog.setSize(40, 40); // TODO reduce to display size
//		stage.addActor(fog);
////		fog.setColor(1f, 0.9f, 0.9f, 0.125f);
////		fog.setColor(EnvColors.DAY.ambient.cpy());
////		fog.getColor().a = 0.7225f;
//		Actors.centerActor(fog);
		
		// flare
//		final FlarePlane flarePlane = new FlarePlane(MAX_WORLD_WIDTH*2f,WORLD_HEIGHT);
//		frontStage.addActor(flarePlane);
//		flarePlane.init();


		
	}

	@Override
	public void render(float delta) {
		//draw something nice to look at
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    	Gdx.gl20.glBlendFunc(GL20.GL_ONE_MINUS_DST_COLOR, GL20.GL_ONE);
    	Gdx.gl20.glBlendFunc(GL20.GL_ZERO, GL20.GL_ZERO);

    	weather.act(delta);
    	backStage.act(delta);
    	stage.act(delta);
    	frontStage.act(delta);
    	
    	// move camera before drawing
//    	stage.getCamera().translate(0.1f, -0.15f, 0);
    	
    	backStage.draw();
    	stage.draw();
    	frontStage.draw();
    	
//    	World world = new World(new Vector2(), false);
//		// add light
//    	rayHandler = new RayHandler(world );
//    	rayHandler.setShadows(false);
//    	new PointLight(rayHandler, 50, new Color(1,1,1,1),35f, 10, 10);
////    	rayHandler.poi
//    	rayHandler.setAmbientLight(1, 0, 0, 0.5f); 
//    	rayHandler.setBlur(true);
//    	rayHandler.setBlurNum(30);
//    	rayHandler.setCombinedMatrix(backStage.getCamera().combined);
//    	rayHandler.updateAndRender();

	}

	@Override
	public void resize(int width, int height) {
		backStage.getViewport().update(width, height, false);
		backStage.getCamera().position.set(0f, 0f, 0f); // this cam is centered so we can zoom in/out without moving the sun away from center
		stage.getViewport().update(width, height, false);
		frontStage.getViewport().update(width, height, false);
		frontStage.getCamera().position.set(0f, 0f, 0f); // this cam is centered so we can zoom in/out without moving the sun away from center
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
