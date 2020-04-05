package ardash.lato.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.kiwi.util.gdx.scene2d.Actors;

import ardash.lato.EnvColors;
import ardash.lato.Assets.SceneTexture;

public class SkyPlane extends Group implements StageAccessor, Disposable {

	private static final float SUN_WIDTH = 2;
	private ShapeRenderer sr;
	private Group sunRotor;

	public SkyPlane(float width, float height) {
		setSize(width, height);
		moveBy(getWidth()/-2f, 0f);// self center
		sr = new ShapeRenderer();
		sr.setAutoShapeType(true);
		setName("skyplane");
		this.setTouchable(Touchable.childrenOnly);
	}

	@Override
	public void init() {
		
		sunRotor = new Group();
		addActor(sunRotor);
		sunRotor.setPosition(getWidth()/2f, getHeight()/2f); // center on plane
		
		// move a bit down, so sun moved behind mountains
		sunRotor.moveBy(0, -10);

		// add sun glow
		Image imgGlow = new Image(getAssets().getSTexture(SceneTexture.GLOW));
		imgGlow.setWidth(SUN_WIDTH*26);
		imgGlow.setHeight(SUN_WIDTH*26);
		sunRotor.addActor(imgGlow);
//		imgGlow.setPosition(0, -15f); // sun rotation radius
		imgGlow.setColor(new Color(1,1,1,0.5f));
		imgGlow.setName("sunglow");
//		imgGlow.setTouchable(Touchable.disabled);

		
		// add sun shape
		Image imgSun = new Image(getAssets().getSTexture(SceneTexture.SUN_SHAPE));
		imgSun.setWidth(SUN_WIDTH);
		imgSun.setHeight(SUN_WIDTH);
		sunRotor.addActor(imgSun);
		imgSun.setPosition(0, -15f); // sun rotation radius
		spawnFlareInForeground(imgSun, 500f);
		imgSun.setTouchable(Touchable.enabled);
		imgSun.setName("sunshape");

		// move glow to sun
		imgGlow.setPosition(imgSun.getX()-imgGlow.getWidth()/2f, imgSun.getY()-imgGlow.getHeight()/2f);
		
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		sunRotor.rotateBy(-0.91f);
//		System.out.println("sun rot: " + sunRotor.getRotation());
	}


	@Override
	public void draw(Batch batch, float parentAlpha) {

		batch.end();

		Vector2 coords = new Vector2(getX(), getY());

		Color color = new Color(getColor());
		sr.setProjectionMatrix(batch.getProjectionMatrix());
		sr.setColor(color.r, color.g, color.b, color.a * parentAlpha);

//		Gdx.gl.glEnable(GL20.GL_BLEND);
//		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sr.begin(ShapeRenderer.ShapeType.Filled);

		sr.rectLine(coords.x, coords.y, coords.x + getWidth(), coords.y, getHeight());
		sr.rect(coords.x, coords.y, getWidth(), getHeight(), EnvColors.DAY.skyBottom, EnvColors.DAY.skyBottom,
				EnvColors.DAY.skyTop, EnvColors.DAY.skyTop);
		sr.end();
//		Gdx.gl.glDisable(GL20.GL_BLEND);
		Gdx.gl.glLineWidth(1f);
		sr.setColor(Color.WHITE);

//		batch.setBlendFunction(GL20.GL_ZERO, GL20.GL_ZERO);
		batch.begin();
		super.draw(batch, parentAlpha);

	}

	@Override
	public void dispose() {
		Disposables.gracefullyDisposeOf(sr);
	}

}
