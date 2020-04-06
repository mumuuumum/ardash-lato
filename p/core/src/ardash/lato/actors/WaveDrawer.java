package ardash.lato.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.AdvShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Disposable;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;

import ardash.lato.terrain.Downer;
import ardash.lato.terrain.HomeHill;
import ardash.lato.terrain.TerrainSegList;
import ardash.lato.weather.AmbientColorChangeListener;

public class WaveDrawer extends Actor implements Disposable, AmbientColorChangeListener {

	private AdvShapeRenderer sr;
	TerrainSegList terrainSegmentList;
	Vector2 tmpVector = new Vector2(); // can be used by one method atomically

	public WaveDrawer(Color color) {
		setColor(color);
		sr = new AdvShapeRenderer();
		sr.setAutoShapeType(true); // TODO check what types we draw

		// path setup
		terrainSegmentList = new TerrainSegList();
		terrainSegmentList.addAllNoOffset(new HomeHill()); // TODO init starting area of terrain
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		batch.end();

		sr.begin();
		sr.setColor(getColor());
		sr.set(ShapeType.Filled);
		sr.setProjectionMatrix(batch.getProjectionMatrix());

		final float drawSteps=0.2f;
		for (float x = terrainSegmentList.first().x; x<terrainSegmentList.last().x-drawSteps ; x+=drawSteps)
		{
			float y = terrainSegmentList.heightAt(x);
			float toX = x+drawSteps;
			float toY = terrainSegmentList.heightAt(toX);
			float[] fa = {x,y, toX,toY, toX,y-500f, x, y-500f};
			sr.polygon(fa);
		}

		sr.end();

		batch.begin();

		if (Gdx.input.isKeyJustPressed(Keys.T))
		{
			terrainSegmentList.addAll(new Downer());
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);
//		updateTerrainSegments();
//		moveBy(-0.1f, 0f); // TODO add speed
	}
	
	public float getHeightAt(final float x)
	{
		return terrainSegmentList.heightAt(x);
	}
	
	/**
	 * for performance reasons this one returns the angle at the x of the last call of getHeightAt(x)
	 */
	public float getAngleAtX(final float x) {
		tmpVector.set(x, getHeightAt(x));
		tmpVector.sub(x+0.1f, getHeightAt(x+0.1f));
		return tmpVector.scl(-1f).angle();
	}
	
	@Override
	public void moveBy(float x, float y) {
		super.moveBy(x, y);
		sr.translate(x, y, 0);

	}

	@Override
	public void dispose() {
		Disposables.gracefullyDisposeOf(sr);
	}

	/**
	 * Remove passed terrain segments and add new terrain in front of the player.
	 * @param x The current position of the performer
	 */
	public void updateTerrainSegments(float x) {
		final float currentMin = terrainSegmentList.first().x;
		final float currentMax = terrainSegmentList.last().x;
		
		final float PASSED_TERRAIN = 200f; // longest possible terrain
		final float FUTURE_TERRAIN = 200f;
		
		if (currentMin+PASSED_TERRAIN < x)
		{
			// old terrain can be removed
			terrainSegmentList.removeFirst();
		}
		if (currentMax-FUTURE_TERRAIN < x)
		{
			// new terrain must be added
			terrainSegmentList.addAll(new Downer());
		}
		
		
	}

	@Override
	public void onAmbientColorChangeTriggered(Color target, float seconds) {
		addAction(Actions.color(target, seconds));		
	}
}