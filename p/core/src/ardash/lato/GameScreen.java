package ardash.lato;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.kiwi.util.gdx.scene2d.Actors;
import com.github.czyzby.kiwi.util.gdx.viewport.Viewports;

import ardash.gdx.scenes.scene3d.Stage3D;
import ardash.lato.Assets.SceneTexture;
import ardash.lato.actions.MoreActions;
import ardash.lato.actors.FlarePlane;
import ardash.lato.actors.MountainRange;
import ardash.lato.actors.ParticlePlane;
import ardash.lato.actors.Performer;
import ardash.lato.actors.SkyPlane;
import ardash.lato.actors.WaveDrawer;
import ardash.lato.actors3.MountainRange3;
import ardash.lato.weather.EnvColors;
import ardash.lato.weather.FogColorChangeListener;
import ardash.lato.weather.FogIntensityChangeListener;
import ardash.lato.weather.WeatherProvider;
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
	public LatoStage guiStage;
	public Stage3D stage3d;
	private RayHandler rayHandler;
	public FlarePlane flarePlane;
	private Performer performer;

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
		guiStage = new LatoStage(Viewports.getDensityAwareViewport(), this);
		stage3d = new Stage3D();
//		backStage.setDebugAll(true);
		stage.setDebugAll(true);
//		frontStage.setDebugAll(true);
//		Gdx.input.setInputProcessor(backStage);
		
		backStage.addActor(weather); // weather can be on any stage

		// particle plane must be drawn under flare plane
		final ParticlePlane particlePlane = new ParticlePlane(MAX_WORLD_WIDTH*2f,WORLD_HEIGHT);
		frontStage.addActor(particlePlane);
		particlePlane.init();
		weather.addPrecipChangeListener(particlePlane);
		
		// additive flare plane (must be done first, so actors can spawn the flares)
		flarePlane = new FlarePlane(MAX_WORLD_WIDTH*2f,WORLD_HEIGHT);
		frontStage.addActor(flarePlane);
		flarePlane.init();
		weather.addSunColourChangeListener(flarePlane);

		
		final SkyPlane skyPlane = new SkyPlane(MAX_WORLD_WIDTH*2f,WORLD_HEIGHT);
		backStage.addActor(skyPlane);
		skyPlane.init();
		weather.addSunColourChangeListener(skyPlane);

		// subscribe sky to skycolors
		weather.addSkyColourChangeListener(skyPlane);
		weather.addSODChangeListener(skyPlane);
		
		for (int i = 0; i<4 ; i++)
		{
			final int numMountains = 20;
			final MountainRange3 mr = new MountainRange3(numMountains);
			stage3d.addActor(mr);
			mr.setName("MountainRange"+i);
//			mr.moveBy(50, 60);
//			mr.setSpeed(20f);
			
//			// fog layer
//			final Image fog = new Image(assets.getSTexture(SceneTexture.FOG_PIX));
//			fog.setSize(204, 204); // TODO reduce to display size
//			backStage.addActor(fog);
////			fog.setColor(1f, 0.9f, 0.9f, 0.125f);
//			fog.setColor(EnvColors.DAY.fog);
//			fog.getColor().a = 0.525f; // orig 0.225f
//			Actors.centerActor(fog);
//			fog.setName("fog"+i);
//			fog.setTouchable(Touchable.disabled);
////			fog.setVisible(false);
			
			// range offset
			mr.translate(-MountainRange3.MOUNT_SIZE*(i+1), -30f*i-4, 0+i*10);
			mr.setSpeed((i*i+1)*02.2f);
			
			// the collection a bit higher
//			mr.moveBy(0,-1f);
			
			// move to center on 0,0
			mr.translate(numMountains/2 * -MountainRange.MOUNT_SIZE,0,0);
//			mr.translate(-600, 0, 0);
			
//			// subscribe the fog layers to fog colour change
//			weather.addFogColourChangeListener(new FogColorChangeListener() {
//				@Override
//				public void onFogColorChangeTriggered(Color target, float seconds) {
//					Color t = target.cpy();
//					t.a = 0.225f;	
//					fog.addAction(MoreActions.noAlphaColor(t, seconds));
//				}
//			});
//			// and to fog intensity
//			weather.addFogIntensityChangeListener(new FogIntensityChangeListener() {
//				@Override
//				public void onFogIntensityChanged(float newIntensity, float duration) {
//					fog.addAction(Actions.alpha(newIntensity, duration));
//				}
//			});

			// subscribe the mountains layers to ambient colour change
//			weather.addAmbientColourChangeListener(mr);
//			weather.addFogColourChangeListener(mr);
		}
		
//		backStage.addActor(skyPlane);
//		skyPlane.init();
		
		// test to add shaperenderers
		WaveDrawer hl = new WaveDrawer(EnvColors.DAY.ambient);
		stage.addActor(hl);
		stage.setWaveDrawer(hl);
		weather.addAmbientColourChangeListener(hl);
		
		performer = new Performer();
		stage.addActor(performer);
		performer.init();
//		p.moveBy(4*1.8f, 10f);
		performer.moveBy(8*1.8f, 10f); // tmp becasue no starting groudn yet
		stage.setPerformer(performer); // attach the camera to him
		weather.addAmbientColourChangeListener(performer);
		
		// attach the zoom of some cameras to the speed of the player
		performer.addSpeedListener(backStage);
		performer.addSpeedListener(stage);
		performer.addSpeedListener(frontStage);
		
//		stage.addActor(new Scarf(assets.getSTexture(SceneTexture.FOG_PIX)));

//		final Stage3DAdapterActor stage3d = new Stage3DAdapterActor();
//		guiStage.addActor(stage3d);
		
		stage3d.getCamera().update();
		
//		stage3d.addActor(new CubeActor3D(1, 1, 1));
//		ModelBuilder mb = new ModelBuilder();
//		stage3d.addActor(new Actor3D(mb.createBox(1, 1, 1, new Material(), 1)));
		
        ModelBuilder mb = new ModelBuilder();
        Model model  = mb.createBox(1, 1, 1, new Material(), 1);
        model = mb.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position
    			| Usage.Normal);
        
//        stage3d.addActor(new Actor3D(model));
        
//        Image3D i3d = new Image3D(40, 40, assets.getSTexture(SceneTexture.PERFORMER), new ModelBuilder(), 20);
//        Image3D i3d = new Image3D(40, 40, Color.WHITE, new ModelBuilder());
        
//        Triangle3D i3d = new Triangle3D(new Vector3(0, 0, 0), Color.WHITE,new Vector3(10, 0, 0), Color.BLUE,new Vector3(0, 10, 0), Color.BLACK, new ModelBuilder());
//        Circle3D i3d = new Circle3D(10, 20, new Vector3(), new ModelBuilder());
//        stage3d.addActor(i3d);
//        i3d.scale (1f, 0.5f, 0);
//        i3d.scale (40f, 40f, 0);
        
//		stage3d.setPosition(1, 1);
//		stage3d.setScale(10);
		stage3d.getRoot().setVisible(true);
		stage3d.getCamera().moveTo(0, 0, 500, 1f);
		stage3d.getCamera().lookAt(0, 0, 0);
        stage3d.getCamera().near = 0.1f;
        stage3d.getCamera().far = 810f;
        stage3d.getCamera().update();

//		stage3d.setDebug(true, true);
		stage3d.getCamera().update();

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

		buildGui();
	}

	private void buildGui() {
		Gdx.input.setInputProcessor(guiStage);
		guiStage.setDebugAll(true);
		Image img = new Image(assets.getSTexture(SceneTexture.PERFORMER));
//		guiStage.addActor(img);
		final LabelStyle lblStyle = new LabelStyle();
		lblStyle.font = new BitmapFont();
		Label fps = new Label("fps", lblStyle) {
			@Override
			public void act(float delta) {
				super.act(delta);
				setText("fps: "+ Gdx.graphics.getFramesPerSecond());
			}
		};
		Table mainTable = new Table();
		mainTable.setTouchable(Touchable.enabled);
		mainTable.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				performer.userInput(true);
				return true;
			}
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				performer.userInput(false);
				super.touchUp(event, x, y, pointer, button);
			}
		});
		guiStage.addActor(mainTable);
		mainTable.setFillParent(true);
		mainTable.row().colspan(3).expandX().fillX();
		mainTable.add(fps);
		mainTable.row().expandY();
//		mainTable.add(img);
		mainTable.add();
		mainTable.row();
		
		Image pause = new Image(assets.getSTexture(SceneTexture.PAUSE));
		pause.setColor(Color.WHITE.cpy());
		pause.getColor().a = .8f;
//		pause.setSize(10f, 10f);
		mainTable.add(pause).height(40f).width(40f).left();
//		fps.

//		Actors.centerActor(mainTable);
	}

	@Override
	public void render(float delta) {
		//draw something nice to look at
//        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
//    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//    	Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable (Gdx.gl.GL_DEPTH_TEST);
//    	Gdx.gl20.glBlendFunc(GL20.GL_ONE_MINUS_DST_COLOR, GL20.GL_ONE);
//    	Gdx.gl20.glBlendFunc(GL20.GL_ZERO, GL20.GL_ZERO);
//
//    	backStage.act(delta);
//    	stage.act(delta);
    	
    	// TODO tmp:
//    	Scarf sc = stage.getRoot().findActor("scarf");
//    	sc.setPosition(performer.getX(), performer.getY());
    	
//    	frontStage.act(delta);
//    	guiStage.act(delta);

    	
//    	backStage.draw();
//    	stage.draw();
//    	frontStage.draw();
//    	guiStage.draw();
    	stage3d.act(delta);
//		stage3d.getCamera().update();
    	stage3d.draw();
//    	stage3d.getCamera().
//    	stage3d.getModelBatch().setCamera(stage3d.getCamera());
//    	((PerspectiveCamera)stage3d.getCamera()).
//    	Stage3D s3d = new Stage3D(50, 50);
//    	s3d.addActor(new CubeActor3D(2, 3, 4));
//    	s3d.act();
//    	s3d.dr
    	
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
		guiStage.getViewport().update(width, height, true);
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
		Disposables.gracefullyDisposeOf(backStage, stage, frontStage, guiStage);
	}

}
