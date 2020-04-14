package ardash.lato.actors3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Disposable;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;

import ardash.gdx.scenes.scene3d.Group3D;
import ardash.gdx.scenes.scene3d.shape.Image3D;
import ardash.lato.Assets.SceneTexture;
import ardash.lato.weather.EnvColors;
import ardash.lato.weather.SODChangeListener;
import ardash.lato.weather.SkyColorChangeListener;

public class SkyPlane3 extends Group3D implements Disposable , SODChangeListener, SkyColorChangeListener {//, SunColorChangeListener,  {

	private static final float SUN_WIDTH = 2;
	private ShapeRenderer sr;
	private Group3D sunRotor;
	private Image3D imgGlow, imgSun;
	private Image3D imgFlare;
	private Actor topColorHolder;
	private Actor bottomColorHolder;

	public SkyPlane3(float width, float height) {
//		setSize(width, height);
//		translate(width/-2f, 0f, 0f);// self center
		sr = new ShapeRenderer();
		sr.setAutoShapeType(true);
		setName("skyplane");
//		this.setTouchable(Touchable.childrenOnly);
		
		sunRotor = new Group3D();
		addActor(sunRotor);
//		sunRotor.translate(width/2f, height/2f, 0); // center on plane
		
		// move a bit down, so sun moved behind mountains
//		sunRotor.moveBy(0, -10);

		// add sun glow
//		imgGlows = new Image(getAssets().getSTexture(SceneTexture.GLOW));
		imgGlow = new Image3D(SUN_WIDTH*26,SUN_WIDTH*26,getAssets().getSTexture(SceneTexture.GLOW),new ModelBuilder());
		sunRotor.addActor(imgGlow);
		imgGlow.setColor(new Color(1,1,1,0.5f));
//		imgGlow.setColor(Color.YELLOW);
		imgGlow.setName("sunglow");

		// add sun shape
		imgSun = new Image3D(SUN_WIDTH,SUN_WIDTH,getAssets().getSTexture(SceneTexture.SUN_SHAPE),new ModelBuilder());
		imgSun.moveBy(-SUN_WIDTH/2f, -SUN_WIDTH/2f);
		sunRotor.addActor(imgSun);
		imgSun.moveBy(0, -15f); // sun rotation radius
		imgSun.setZ(1f);
//		imgFlare = (Image)spawnFlareInForeground(imgSun, 500f);
		imgFlare = spawnFlareOnFlareStage(imgSun, 20f);
//		imgSun.setTouchable(Touchable.enabled);
		imgSun.setName("sunshape");

		
		// move glow to sun
		imgGlow.setPosition(imgSun.getX()+imgSun.getWidth()/2f -imgGlow.getWidth()/2f, imgSun.getY()+imgSun.getHeight()/2f  -imgGlow.getHeight()/2f);

		// move flare to sun
		imgFlare.setPosition(imgSun.getX()+imgSun.getWidth()/2f -imgFlare.getWidth()/2f, imgSun.getY()+imgSun.getHeight()/2f  -imgFlare.getHeight()/2f);
		
		// add dummy Actor to hold the color, so the color can be changed by an Action
		topColorHolder = new Actor();
		topColorHolder.setColor(EnvColors.DAY.skyTop);
		bottomColorHolder = new Actor();
		bottomColorHolder.setColor(EnvColors.DAY.skyBottom);
//		addActor(topColorHolder);
//		addActor(bottomColorHolder);
		
		
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		topColorHolder.act(delta);
		bottomColorHolder.act(delta);
//		sunRotor.rotateBy(-360f/60f);
//		System.out.println("sun rot: " + sunRotor.getRotation());
//		getStage().getCamera().lookAt(getPosition());
//		getStage().getCamera().moveTo(10, 10, 10, 1f);
		
		// move flare to sun
		
		Vector2 tmpV = new Vector2();
		imgSun.localToScreenCoordinates(tmpV);
//		imgFlare.setPosition(imgSun.getX()+imgSun.getWidth()/2f -imgFlare.getWidth()/2f, imgSun.getY()+imgSun.getHeight()/2f  -imgFlare.getHeight()/2f);
		
		
		
		// TODO CONTINUE EHRE like this: on new stage use same z value as emitter + 100 OFFSET : but move camera away + 100 OFFSET: So Z value  is different and does not interfere with other Z deep-test values, ... but relative angle to camera is correct
		tmpV.set(2,2);
		final Vector3 gp = imgSun.getGlobalPosition();
//		gp.add (-imgFlare.getWidth()/2f, -imgFlare.getHeight()/2f, 0f);
		imgFlare.setPosition(gp.x , gp.y, gp.z);
//		imgFlare.setPosition(tmpV.x, tmpV.y);

//		getStage().getCamera().lookAt(gp);
		System.out.println("GP XXXXXXXXXXXXXXXXXXX: " + gp);
		
		
//		TODO try ortho cam
		
		
//		imgSun.lookAtMe(); 
		
		
	}

	
	@Override
		public void draw(ModelBatch modelBatch, Environment environment) {
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		modelBatch.end();

		Vector2 coords = new Vector2(getX(), getY());

//		Color color = new Color(getColor());
//		sr.setProjectionMatrix(batch.getProjectionMatrix());
		
//		sr.setProjectionMatrix(transform);
//		sr.setColor(color.r, color.g, color.b, color.a * parentAlpha);

//		Gdx.gl.glEnable(GL20.GL_BLEND);
//		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sr.begin(ShapeRenderer.ShapeType.Filled);

		final Color fog = EnvColors.DAY.fog;
//		sr.rectLine(coords.x, coords.y, coords.x + getWidth(), coords.y, getHeight());
//		sr.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()*2.5f, bottomColorHolder.getColor(), bottomColorHolder.getColor(),
//				topColorHolder.getColor(), topColorHolder.getColor());
		
		// TODO bring back
//		sr.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()*2.5f, fog, fog, fog, fog);
		
		sr.end();
//		Gdx.gl.glDisable(GL20.GL_BLEND);
//		Gdx.gl.glLineWidth(1f);
//		sr.setColor(Color.WHITE);

//		batch.setBlendFunction(GL20.GL_ZERO, GL20.GL_ZERO);
		modelBatch.begin(getStage().getCamera());
		super.draw(modelBatch, environment);

		}

	@Override
	public void onSkyColorChangeTriggered(Color targetTop, Color targetBottom, float seconds) {
		topColorHolder.addAction(Actions.color(targetTop, seconds));
		bottomColorHolder.addAction(Actions.color(targetBottom, seconds));
		
	}
//
//	@Override
//	public void onSunColorChangeTriggered(Color target, float seconds) {
//		imgGlow.addAction(Actions.color(target, seconds));
//		if (!getGameScreen().weather.getCurrentColorSchema().equals(EnvColors.NIGHT))
//			imgFlare.addAction(Actions.color(target, seconds));
////		imgSun.addAction(Actions.color(target, seconds));
//	}

	boolean fading = false;
	@Override
	public void onSODChange(float newSOD, float hourOfDay, float delta, float percentOfDayOver) {
		sunRotor.setRotation(percentOfDayOver * -360f);
		
		// fadeOut doesn't work because the flare is not alpha-blended
//		if (hourOfDay < 7 || hourOfDay > 17)
//			imgFlare.setVisible(false);
//		else
//			imgFlare.setVisible(true);
			
	}

	@Override
	public void dispose() {
		Disposables.gracefullyDisposeOf(sr);
	}

}
