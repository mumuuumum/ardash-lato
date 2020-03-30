package ardash.lato.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.AdvShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;

import ardash.lato.terrain.Downer;
import ardash.lato.terrain.HomeHill;
import ardash.lato.terrain.TerrainSegList;

public class WaveDrawer extends Actor implements Disposable {

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
		for (float x = 1; x<terrainSegmentList.last().x-drawSteps ; x+=drawSteps)
		{
			float y = terrainSegmentList.heightAt(x);
			float toX = x+drawSteps;
			float toY = terrainSegmentList.heightAt(toX);
			float[] fa = {x,y, toX,toY, toX,-100f, x, -100f};
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
	
// TODO continue here: reinstate automatic creation and deletion of segemnts
//	private void updateTerrainSegments() {
//		final float currentX = getX();
//		System.out.println("terrain x: " + currentX);
//		System.out.println("terrain cp1: " + controlPoints[1]);
//
//		// check if last available control point is before max required X terrain
//		final TerrainSegment lastSeg = terrainSegmentList.get(terrainSegmentList.size()-1);
//		if (lastSeg.last().x+currentX < GameScreen.MAX_WORLD_WIDTH)
//		{
//			// we need a new segment
//			addTerrainSegment();
//		}
//		
//		// check if a segment is way in front of 0 so we don't need it any more (won't be rendered any more)
//		final TerrainSegment firstSeg = terrainSegmentList.get(0);
//		if (firstSeg.last().x+currentX < -5f)
//		{
//			terrainSegmentList.remove(0);
//			updateControlPoints();
//		}		
//	}

	@Override
	public void moveBy(float x, float y) {
		super.moveBy(x, y);
		sr.translate(x, y, 0);

	}

	@Override
	public void dispose() {
		Disposables.gracefullyDisposeOf(sr);
	}
}