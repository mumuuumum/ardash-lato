package ardash.lato.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.kiwi.util.gdx.scene2d.Actors;

import ardash.lato.Assets.SceneTexture;
import ardash.lato.weather.EnvColors;
import ardash.lato.weather.SODChangeListener;
import ardash.lato.weather.SkyColorChangeListener;
import ardash.lato.weather.SunColorChangeListener;
import ardash.lato.weather.WeatherProvider;

public class SkyPlane extends Group implements StageAccessor, Disposable, SkyColorChangeListener, SunColorChangeListener, SODChangeListener {

	private static final float SUN_WIDTH = 2;
	private ShapeRenderer sr;
	private Group sunRotor;
	private Image imgGlow, imgSun;
	private Actor imgFlare;
	private Actor topColorHolder;
	private Actor bottomColorHolder;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	public SkyPlane(float width, float height) {
		setSize(width, height);
		moveBy(getWidth()/-2f, 0f);// self center
		sr = new ShapeRenderer();
		sr.setAutoShapeType(true);
		setName("skyplane");
		this.setTouchable(Touchable.childrenOnly);
		
		sunRotor = new Group();
		addActor(sunRotor);
		sunRotor.setPosition(getWidth()/2f, getHeight()/2f); // center on plane
		
		// move a bit down, so sun moved behind mountains
		sunRotor.moveBy(0, -10);

		// add sun glow
		imgGlow = new Image(getAssets().getSTexture(SceneTexture.GLOW));
		imgGlow.setWidth(SUN_WIDTH*26);
		imgGlow.setHeight(SUN_WIDTH*26);
		sunRotor.addActor(imgGlow);
//		imgGlow.setPosition(0, -15f); // sun rotation radius
		imgGlow.setColor(new Color(1,1,1,0.1f)); // adjusting glow intensity here, changes apperence of max fog
		imgGlow.setName("sunglow");
//		imgGlow.setTouchable(Touchable.disabled);

		
		// add sun shape
		imgSun = new Image(getAssets().getSTexture(SceneTexture.SUN_SHAPE));
		imgSun.setWidth(SUN_WIDTH);
		imgSun.setHeight(SUN_WIDTH);
		sunRotor.addActor(imgSun);
		imgSun.setPosition(0, -15f); // sun rotation radius
		imgFlare = spawnFlareInForeground(imgSun, 500f);
		imgSun.setTouchable(Touchable.enabled);
		imgSun.setName("sunshape");

		// move glow to sun
		imgGlow.setPosition(imgSun.getX()-imgGlow.getWidth()/2f, imgSun.getY()-imgGlow.getHeight()/2f);
		
		// add dummy Actor to hold the color, so the color can be changed by an Action
		topColorHolder = new Actor();
		topColorHolder.setColor(EnvColors.DAY.skyTop);
		bottomColorHolder = new Actor();
		bottomColorHolder.setColor(EnvColors.DAY.skyBottom);
		addActor(topColorHolder);
		addActor(bottomColorHolder);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
//		sunRotor.rotateBy(-0.91f);
//		System.out.println("sun rot: " + sunRotor.getRotation());
	}


	@Override
	public void draw(Batch batch, float parentAlpha) {

		batch.end();

		Vector2 coords = new Vector2(getX(), getY());

		Color color = new Color(getColor());
//		sr.setProjectionMatrix(batch.getProjectionMatrix());
		sr.setColor(color.r, color.g, color.b, color.a * parentAlpha);

//		Gdx.gl.glEnable(GL20.GL_BLEND);
//		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sr.begin(ShapeRenderer.ShapeType.Filled);

//		sr.rectLine(coords.x, coords.y, coords.x + getWidth(), coords.y, getHeight());
		final Color fog = EnvColors.DAY.fog;
//		sr.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), bottomColorHolder.getColor(), bottomColorHolder.getColor(),
//				topColorHolder.getColor(), topColorHolder.getColor());
		sr.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), fog, fog, fog, fog);
		sr.end();
//		Gdx.gl.glDisable(GL20.GL_BLEND);
		Gdx.gl.glLineWidth(1f);
		sr.setColor(Color.WHITE);

//		batch.setBlendFunction(GL20.GL_ZERO, GL20.GL_ZERO);
		batch.begin();
		super.draw(batch, parentAlpha);

	}

	@Override
	public void onSkyColorChangeTriggered(Color targetTop, Color targetBottom, float seconds) {
		topColorHolder.addAction(Actions.color(targetTop, seconds));
		bottomColorHolder.addAction(Actions.color(targetBottom, seconds));
		
	}

	@Override
	public void onSunColorChangeTriggered(Color target, float seconds) {
		imgGlow.addAction(Actions.color(target, seconds));
		if (!getGameScreen().weather.getCurrentColorSchema().equals(EnvColors.NIGHT))
			imgFlare.addAction(Actions.color(target, seconds));
//		imgSun.addAction(Actions.color(target, seconds));
	}

	boolean fading = false;
	@Override
	public void onSODChange(float newSOD, float hourOfDay, float delta, float percentOfDayOver) {
		sunRotor.setRotation(percentOfDayOver * -360f);
		
		// fadeOut doesn't work because the flare is not alpha-blended
		if (hourOfDay < 7 || hourOfDay > 17)
			imgFlare.setVisible(false);
		else
			imgFlare.setVisible(true);
			
	}

	@Override
	public void dispose() {
		Disposables.gracefullyDisposeOf(sr);
	}

}
