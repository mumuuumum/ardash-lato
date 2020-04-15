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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.kiwi.util.gdx.viewport.Viewports;

import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.gdx.scenes.scene3d.Camera3D;
import ardash.gdx.scenes.scene3d.Stage3D;
import ardash.gdx.scenes.scene3d.shape.CubeActor3D;
import ardash.lato.Assets.SceneTexture;
import ardash.lato.actors.FlarePlane;
import ardash.lato.actors.ParticlePlane;
import ardash.lato.actors.Performer;
import ardash.lato.actors.Performer.PerformerListener;
import ardash.lato.actors.SkyPlane;
import ardash.lato.actors.WaveDrawer;
import ardash.lato.actors3.MountainRange3;
import ardash.lato.weather.EnvColors;
import ardash.lato.weather.WeatherProvider;
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
	public Stage3D stage3d, backStage3d;
	public FlarePlane flarePlane;
	private Performer performer;

	public GameScreen(GameManager gm) {
		this.gm = gm;
		this.am = gm.am;
		this.assets = gm.assets;
	}

	@Override
	/**
	 * layers:
	 * skyStage (skyPlane)
	 * stage3D (MountainRange3)
	 * frontStage (ParticlePlane, Flareplane)
	 */
	public void show() {
		Texture ball = am.get(Assets.ball); // Assets.ball is a String

		weather = new WeatherProvider();
		backStage = new LatoStage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT), this);
		stage = new LatoStage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT), this);
		frontStage = new LatoStage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT), this);
//		frontStage = new LatoStage(new ScreenViewport(), this);
		CURRENT_WORLD_WIDTH = backStage.getViewport().getWorldWidth();
		guiStage = new LatoStage(Viewports.getDensityAwareViewport(), this);
		stage3d = new Stage3D(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, new Camera3D())); // perspective camera draws correctly behind each other
		backStage3d = new Stage3D(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, new Camera3D())); 

//		backStage.setDebugAll(true);
		stage.setDebugAll(true);
//		frontStage.setDebugAll(true);
//		stage3d.setDebug(true, true);
//		Gdx.input.setInputProcessor(backStage);
		
		guiStage.addActor(weather); // weather can be on any stage

		// particle plane must be drawn under flare plane
		final ParticlePlane particlePlane = new ParticlePlane(MAX_WORLD_WIDTH*2f,WORLD_HEIGHT);
		frontStage.addActor(particlePlane);
		particlePlane.init();
		weather.addPrecipChangeListener(particlePlane);
		
		// additive flare plane (must be created first, so actors can spawn the flares)
		flarePlane = new FlarePlane(MAX_WORLD_WIDTH*2f,WORLD_HEIGHT);
		frontStage.addActor(flarePlane);
//		flarePlane.init();
		weather.addSunColourChangeListener(flarePlane);

		
		final SkyPlane skyPlane = new SkyPlane(MAX_WORLD_WIDTH,WORLD_HEIGHT);
		backStage.addActor(skyPlane);
		weather.addSkyColourChangeListener(skyPlane);
		weather.addSunColourChangeListener(skyPlane);
		weather.addSODChangeListener(skyPlane);
		
		for (int i = 0; i<4 ; i++)
		{
			final int numMountains = 20;
			final MountainRange3 mr = new MountainRange3(numMountains);
			stage3d.addActor(mr);
			mr.setName("MountainRange"+i);
			
			// range offset
			mr.translate(-MountainRange3.MOUNT_SIZE*(i+1), -2f*i+2, 0+i*2);
			mr.setSpeed((i*i+1)*0.2f);
			
			// move to center on 0,0
			mr.translate(numMountains/2 * -MountainRange3.MOUNT_SIZE,0,0);

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
		weather.addAmbientColourChangeListener(performer);
		
		// attach the zoom of some cameras to the speed of the player
		performer.addSpeedListener(backStage);
		performer.addSpeedListener(stage);
		performer.addSpeedListener(frontStage);
		
//		stage.addActor(new Scarf(assets.getSTexture(SceneTexture.FOG_PIX)));

//		final Stage3DAdapterActor stage3d = new Stage3DAdapterActor();
//		guiStage.addActor(stage3d);
		
		stage3d.getCamera().update();
		backStage3d.getCamera().update();
//		flareStage3d.getCamera().update();
		
//		stage3d.addActor(new CubeActor3D(1, 1, 1));
//		ModelBuilder mb = new ModelBuilder();
//		stage3d.addActor(new Actor3D(mb.createBox(1, 1, 1, new Material(), 1)));
		
//        ModelBuilder mb = new ModelBuilder();
//        Model model  = mb.createBox(1, 1, 1, new Material(), 1);
//        model = mb.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position
//    			| Usage.Normal);
        
//        stage3d.addActor(new Actor3D(model));
        
//        Image3D i3d = new Image3D(40, 40, assets.getSTexture(SceneTexture.PERFORMER), new ModelBuilder(), 20);
//        Image3D i3d = new Image3D(40, 40, Color.WHITE, new ModelBuilder());
        
//        Triangle3D i3d = new Triangle3D(new Vector3(0, 0, 0), Color.WHITE,new Vector3(10, 0, 0), Color.BLUE,new Vector3(0, 10, 0), Color.BLACK, new ModelBuilder());
//        Circle3D i3d = new Circle3D(10, 20, new Vector3(), new ModelBuilder());
//        stage3d.addActor(i3d);
//        i3d.scale (1f, 0.5f, 0);
//        i3d.scale (40f, 40f, 0);
//        
//        skyStage3d.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        skyStage3d.getCamera().lookAt(0, 0, 0);
//        skyStage3d.getCamera().translate(0, 0, 30);
////		skyStage3d.getCamera().moveTo(0, 0, 30, 1f);
//		skyStage3d.getCamera().near = 0.1f;
//		skyStage3d.getCamera().far = 35f;
//		skyStage3d.getCamera().update();

//		stage3d.setPosition(1, 1);
//		stage3d.setScale(10);
//		stage3d.getRoot().setVisible(true);
        stage3d.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage3d.getCamera().lookAt(0, 0, 0);
//		stage3d.getCamera().moveTo(0, 0, 30, 1f);
		stage3d.getCamera().translate(0, 0, 30);
		
        stage3d.getCamera().near = 0.1f;
        stage3d.getCamera().far = 50f; // TODO adjust fog intensity here
        stage3d.getCamera().update();
        
        weather.addFogIntensityChangeListener(stage3d);
        weather.addFogColourChangeListener(stage3d);
        weather.addAmbientColourChangeListener(stage3d);
        
        backStage3d.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backStage3d.getCamera().lookAt(0, 0, 0);
        backStage3d.getCamera().translate(0, 0, 10);
        backStage3d.getCamera().near = 0.1f;
        backStage3d.getCamera().far = 50f; // TODO adjust fog intensity here
        backStage3d.getCamera().update();
        
        CubeActor3D ca = new CubeActor3D(1, 1, 1);
//        backStage3d.addActor(ca);
//        ca.translate(55, 3, 0);
        
		Model houseModel = am.get(Assets.toon_house); 
		Actor3D ma = new Actor3D(houseModel);
        backStage3d.addActor(ma);
        backStage3d.setAmbientLightColor(Color.WHITE.cpy());
        backStage3d.setDirectionalLight(null);
        ma.setScale(1, 1, 1);
        ma.setScale(0.002f, 0.002f, 0.002f);
        ma.translate(55, 0, 0);
		

    	// connect cameras to Performer
    	performer.addListener(new PerformerListener() {
			@Override
			public void onPositionChange(float newX, float newY) {
				stage.getCamera().translate(-(stage.getCamera().position.x - performer.getCamSpot().x)
						, -(stage.getCamera().position.y - performer.getCamSpot().y), 0);
				stage.getCamera().update();

				backStage3d.getCamera().translate(-(backStage3d.getCamera().position.x - performer.getCamSpot().x)
						, -(backStage3d.getCamera().position.y - performer.getCamSpot().y), 0);
				backStage3d.getCamera().update();
			}
		});


//		flareStage3d.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		flareStage3d.getCamera().translate(0, 0, 30);
////		flareStage3d.getCamera().moveTo(0, 0, 30, 1f);
//		flareStage3d.getCamera().near = 0.1f;
//		flareStage3d.getCamera().far = 35f;
//		flareStage3d.getCamera().update();
		
		
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
        Gdx.gl.glClearColor(1, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT );
//        Gdx.gl.glDisable(Gdx.gl.GL_DEPTH_TEST);
//        Gdx.gl.glEnable (Gdx.gl.GL_DEPTH_TEST);
//        Gdx.gl.glDepthFunc(Gdx.gl.GL_GREATER);
//        Gdx.gl.glDepthFunc(Gdx.gl.GL_LESS);
        Gdx.gl.glEnable (Gdx.gl.GL_BLEND);
//        Gdx.gl20.glClearDepthf(1.0f);
//    	Gdx.gl20.glBlendFunc(GL20.GL_ONE_MINUS_DST_COLOR, GL20.GL_ONE);
//    	Gdx.gl20.glBlendFunc(GL20.GL_ZERO, GL20.GL_ZERO);
//
//    	backStage.act(delta);
//    	stage.act(delta);
    	
    	// TODO tmp:
//    	Scarf sc = stage.getRoot().findActor("scarf");
//    	sc.setPosition(performer.getX(), performer.getY());
    	
    	guiStage.act(delta); // contains weather provider

//    	frontStage.draw();
    	
    	backStage.act();
    	backStage.draw();
//    	skyStage3d.act(delta);
//    	skyStage3d.draw();
    	stage3d.act(delta);
    	stage3d.draw();


    	stage.act();
    	stage.draw();
    	
    	backStage3d.act(delta);
    	backStage3d.draw();
    	
    	frontStage.act(delta);
    	frontStage.draw();
//    	flareStage3d.act(delta);
//    	flareStage3d.draw();


    	guiStage.draw();
    	
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
