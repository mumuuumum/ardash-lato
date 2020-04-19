package ardash.lato.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;

import ardash.lato.Assets.SceneTexture;
import ardash.lato.actions.MoreActions;
import ardash.lato.weather.EnvColors;
import ardash.lato.weather.FogColorChangeListener;
import ardash.lato.weather.SODChangeListener;
import ardash.lato.weather.SkyColorChangeListener;
import ardash.lato.weather.SunColorChangeListener;

public class SkyPlane extends Group implements StageAccessor, Disposable, SkyColorChangeListener, SunColorChangeListener, SODChangeListener, FogColorChangeListener {

	private static final float SUN_WIDTH = 2;
	private static final float MIN_STAR_SIZE = 0.1f;
	private static final float MAX_STAR_SIZE = 0.2f;
	private ShapeRenderer sr;
	private Group sunRotor, stars;
	private Image iSunGlow, iSun, iMoonGlow, iMoon;
	private Actor sunFlare, moonFlare;
	private Actor topColorHolder, bottomColorHolder, fogColorHolder;
	RandomXS128 rand = new RandomXS128(8793246527834L);

	public SkyPlane(float width, float height) {
		setSize(width, height);
		final float width2 = getWidth()/2f;
		final float height2 = getHeight()/2f;
		moveBy(-width2, 0f);// self center
		sr = new ShapeRenderer();
		sr.setAutoShapeType(true);
		setName("skyplane");
		this.setTouchable(Touchable.childrenOnly);
		
		sunRotor = new Group();
		addActor(sunRotor);
		sunRotor.setPosition(width2, height2); // center on plane
		
		// move a bit down, so sun moved behind mountains
		sunRotor.moveBy(0, -15);
		
		// STARS
		
		MathUtils.random = rand; // make always the same stars
		Vector2 tmpVec = new Vector2();
		for ( int i=0; i<50; i++)
		{
			final Image img = new Image(getAssets().getSTexture(SceneTexture.FOG_PIX));
			final float size = MathUtils.random(MIN_STAR_SIZE, MAX_STAR_SIZE);
			final float ch = MathUtils.randomTriangular(237f, 298f, 237f); // red to blue
//			final float ch = MathUtils.random(55f, 62f); // mostly yellow
			final float cs = MathUtils.randomTriangular(0f, 30f, 0f)/100f; // more 0
			final float cv = 100f/100f;
			
			final float ang = MathUtils.random(0f, 360f);
			final float radius = MathUtils.random(10f, 30f);
			final Group rotor = new Group();
			addActor(rotor);
			rotor.setPosition(sunRotor.getX(), sunRotor.getY());
			rotor.addActor(img);
//			
//			tmpVec.set(1, 1).nor().setAngle(ang).setLength(radius);
//			final float sx = tmpVec.x;
//			final float sy = tmpVec.y;
			
			img.setWidth(size);
			img.setHeight(size);
			img.getColor().fromHsv(ch, cs, cv);
			img.setPosition(0, radius); // moon rotation radius, stars are above and next to moon
			rotor.setRotation(ang);
//			img.setOrigin(width2-sx, height2-15-sy);
			img.setRotation(45f);
			img.setName("star");
//			img.debug();
//			img.addAction(Actions.forever(Actions.sequence(Actions.fadeOut(1),Actions.fadeIn(1))));
			
			Action ra = Actions.run(new Runnable() {
				@Override
				public void run() {
					float r = rotor.getRotation();
					if (r<0) r+=360f;
					if (r>90 && r<270)
					{
						rotor.setVisible(false);
					}
					else
					{
						rotor.setVisible(true);
						rotor.getChild(0).getColor().a = MathUtils.cosDeg(r);
						
					}
				}
			});
			rotor.addAction(Actions.forever(ra));
			rotor.addAction(Actions.forever(Actions.rotateBy(-10,1)));
//			rotor.addAction(Actions.forever(Actions.sequence(Actions.rotateBy(-10,1),ra)));
		}


		// SUN
		
		// add sun glow
		iSunGlow = new Image(getAssets().getSTexture(SceneTexture.GLOW));
		iSunGlow.setWidth(SUN_WIDTH*26);
		iSunGlow.setHeight(SUN_WIDTH*26);
		sunRotor.addActor(iSunGlow);
//		imgGlow.setPosition(0, -15f); // sun rotation radius
		iSunGlow.setColor(new Color(1,1,1,0.51f)); // adjusting glow intensity here, changes appearance of max fog
		iSunGlow.setName("sunglow");
//		imgGlow.setTouchable(Touchable.disabled);

		
		// add sun shape
		iSun = new Image(getAssets().getSTexture(SceneTexture.SUN_SHAPE));
		iSun.setWidth(SUN_WIDTH);
		iSun.setHeight(SUN_WIDTH);
		sunRotor.addActor(iSun);
		iSun.setPosition(0, -20f); // sun rotation radius
		sunFlare = spawnFlareInForeground(iSun, 500f);
		iSun.setTouchable(Touchable.enabled);
		iSun.setName("sunshape");

		// move glow to sun
		iSunGlow.setPosition(iSun.getX()-iSunGlow.getWidth()/2f, iSun.getY()-iSunGlow.getHeight()/2f);

		
		// MOON

		// add moon glow
		iMoonGlow = new Image(getAssets().getSTexture(SceneTexture.GLOW));
		iMoonGlow.setWidth(SUN_WIDTH*26);
		iMoonGlow.setHeight(SUN_WIDTH*26);
		sunRotor.addActor(iMoonGlow);
		iMoonGlow.setColor(new Color(1,1,1,0.1f)); // adjusting glow intensity here, changes appearance of max fog
		iMoonGlow.setName("moonglow");

		
		// add moon shape
		iMoon = new Image(getAssets().getSTexture(SceneTexture.MOON_SHAPE));
		iMoon.setWidth(SUN_WIDTH);
		iMoon.setHeight(SUN_WIDTH);
		sunRotor.addActor(iMoon);
		iMoon.setPosition(0, 15f); // moon rotation radius
		moonFlare = spawnFlareInForeground(iMoon, 500f);
		moonFlare.getColor().mul(0.5f);
		moonFlare.setVisible(false);
		iMoon.setTouchable(Touchable.enabled);
		iMoon.setName("moonshape");

		// move moon glow to moon
		iMoonGlow.setPosition(iMoon.getX()-iMoonGlow.getWidth()/2f, iMoon.getY()-iMoonGlow.getHeight()/2f);

		

		
		// add dummy Actor to hold the color, so the color can be changed by an Action
		topColorHolder = new Actor();
		topColorHolder.setColor(EnvColors.DAY.skyTop);
		bottomColorHolder = new Actor();
		bottomColorHolder.setColor(EnvColors.DAY.skyBottom);
		fogColorHolder = new Actor();
		fogColorHolder.setColor(EnvColors.DAY.fog);
		addActor(topColorHolder);
		addActor(bottomColorHolder);
		addActor(fogColorHolder);
		
		MathUtils.random = new RandomXS128(); // bring randomness back
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
		final Color bc = bottomColorHolder.getColor();
		final Color tc = topColorHolder.getColor();
		final Color fc = fogColorHolder.getColor();
		sr.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2f, bc, bc, fc, fc);
		sr.rect(0, Gdx.graphics.getHeight()/2f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()*10f, fc, fc, tc, tc);
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
	public void onFogColorChangeTriggered(Color target, float seconds) {
		fogColorHolder.addAction(Actions.color(target, seconds));
	}

	@Override
	public void onSunColorChangeTriggered(Color target, float seconds) {
		iSunGlow.addAction(MoreActions.noAlphaColor(target, seconds));
		if (!getGameScreen().weather.getCurrentColorSchema().equals(EnvColors.NIGHT))
			sunFlare.addAction(MoreActions.noAlphaColor(target, seconds));
//		imgSun.addAction(Actions.color(target, seconds));
	}

	boolean fading = false;
	@Override
	public void onSODChange(float newSOD, float hourOfDay, float delta, float percentOfDayOver) {
		sunRotor.setRotation(percentOfDayOver * -360f);
		
		// fadeOut doesn't work because the flare is not alpha-blended
		if (hourOfDay < 6.5f || hourOfDay > 18.1f)
			sunFlare.setVisible(false);
		else
			sunFlare.setVisible(true);
			
		if (hourOfDay < 4.9f || hourOfDay > 18.5f)
			moonFlare.setVisible(true);
		else
			moonFlare.setVisible(false);
			
	}

	@Override
	public void dispose() {
		Disposables.gracefullyDisposeOf(sr);
	}

}
