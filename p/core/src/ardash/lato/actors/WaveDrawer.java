package ardash.lato.actors;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;

import ardash.lato.GameScreen;
import ardash.lato.terrain.Downer;

public class WaveDrawer extends Actor implements Disposable {

	private ShapeRenderer sr;
	private CatmullRomSpline<Vector2> path1;
	Vector2[] controlPoints;
	ArrayList<Vector2> cpList;
	List<Downer> terrainSegmentList;
	Vector2 tmpVector = new Vector2(); // can be used by one method atomically
	private float posOfLastQuery;

	// create vectors to store start and end points of this section of the curve
	Vector2 st = new Vector2();
	Vector2 end = new Vector2();

	public WaveDrawer(Color color) {
		setColor(color);
//		rotateBy(45f);
//		moveBy(30, -30);
		sr = new ShapeRenderer();
		sr.setAutoShapeType(true); // TODO check what types we draw

		// path setup
		// set up random control points
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		int points = 8;
		controlPoints = new Vector2[points];
		cpList = new ArrayList<Vector2>();
		terrainSegmentList = new ArrayList<Downer>();
		terrainSegmentList.add(new Downer()); // TODO init stating area of terrain
		for (int i = 0; i < points; i++) {
			int x = (int) (Math.random() * width);
			int y = (int) (Math.random() * height);
			Vector2 point = new Vector2(x, y);
			controlPoints[i] = point;
		}

		controlPoints[0].set(5, 50);
		controlPoints[1].set(10, 50);
		controlPoints[2].set(15, 31);
		controlPoints[3].set(20, 28);
		controlPoints[4].set(25, 27);
		controlPoints[5].set(30, 27);
		controlPoints[6].set(35, 27);
		controlPoints[7].set(40, 30);

		// set up the curves
		path1 = new CatmullRomSpline<Vector2>(controlPoints, false);

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		batch.end();

//		Vector2 coords = new Vector2(getX(), getY());
//
//		Color color = new Color(getColor());
//		sr.setProjectionMatrix(batch.getProjectionMatrix());
//		sr.setColor(color.r, color.g, color.b, color.a * parentAlpha);
//
//		Gdx.gl.glEnable(GL20.GL_BLEND);
//		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//		sr.begin(ShapeRenderer.ShapeType.Filled);
//
//		sr.rectLine(coords.x, coords.y, coords.x + getWidth(), coords.y, getHeight());
//		sr.end();
//		Gdx.gl.glDisable(GL20.GL_BLEND);
//		Gdx.gl.glLineWidth(1f);
//		sr.setColor(Color.WHITE);

		sr.begin();
		sr.setColor(getColor());
		sr.setProjectionMatrix(batch.getProjectionMatrix());

		final int fragments = 400;
		final float fragmentLength = 1 / (float) fragments;
		float lastEnd = fragmentLength;
		path1.valueAt(st, 0); // get first position

		// draw path1
		for (int i = 0; i < fragments; ++i) {
			path1.valueAt(end, lastEnd);

			// draw body below curve
//			sr.line(st.x, st.y, end.x, end.y);
			sr.set(ShapeType.Filled);
//			sr.setColor(Color.RED);
			sr.rect(st.x, st.y, end.x - st.x, -100f);

			// draw the curve
//			sr.set(ShapeType.Line);
//			sr.setColor(Color.LIME);
//			sr.line(st.x, st.y, end.x, end.y);
			sr.rectLine(st.x, st.y, end.x, end.y, 0.2f);

			// prepare next round
			st.set(end);
			lastEnd += fragmentLength;
		}

		// draw control points
//		sr.setColor(Color.LIME);
//		for (Vector2 v : controlPoints) {
//			sr.line(v.x, v.y, v.x + 1, v.y + 1);
//		}

		sr.end();

		batch.begin();

	}

	@Override
	public void act(float delta) {
		super.act(delta);
		updateTerrainSegments();
//		moveBy(-0.1f, 0f); // TODO add speed
	}
	
	public float getHeightAt(final float x)
	{
		float lx = x, ly = 0;
		float pos = 0.0f; // value between 0 and 1 that indicated the current seeker position
		float offset = 0.1f; // the value to move next, can be negative to move back
		
		while (true)
		{
			pos += offset;
			path1.valueAt(tmpVector, pos);
			lx = tmpVector.x;
			ly = tmpVector.y;
			if (MathUtils.isEqual(lx, x, 0.001f))
			{
				posOfLastQuery=pos;
				return ly;
			}
			boolean movingForward = offset >= 0;
			if (lx>x)
			{
				// went too far, go back
				offset = -1f * Math.abs(offset);
				// if we just changed direction (moved forward before), reduce stepsize
				if (movingForward)
					offset *= 0.1f;
			}
			else {
				// didn't go far enough yet, go forward
				offset = Math.abs(offset);
				// if we just changed direction (moved backward before), reduce stepsize
				if (! movingForward)
					offset *= 0.1f;
			}
		}
		
//		return ly;
	}
	
	/**
	 * for performance reasons this one returns the angle at the x of the last call of getHeightAt(x)
	 */
	public float getAngleAtLatQuery() {
		path1.derivativeAt(tmpVector, posOfLastQuery);
		return tmpVector.angle();
	}

	private void updateTerrainSegments() {
		final float currentX = getX();
		System.out.println("terrain x: " + currentX);
		System.out.println("terrain cp1: " + controlPoints[1]);

		// check if last available control point is before max required X terrain
		final Downer lastSeg = terrainSegmentList.get(terrainSegmentList.size()-1);
		if (lastSeg.last().x+currentX < GameScreen.MAX_WORLD_WIDTH)
		{
			// we need a new segment
			Downer ts = new Downer();

			// the the TerrainSegment is still in relative coords, the coords of the last point must be added to it
			for (Vector2 v : ts.getPoints()) {
				v.add(lastSeg.last()); // add last point of last segment
			}
			
			// now it can be appended
			terrainSegmentList.add(ts);
			
			// now update the control point list
			updateControlPoints();
		}
		
		// check if a segment is way in front of 0 so we don't need it any more (won't be rendered any more)
		final Downer firstSeg = terrainSegmentList.get(0);
		if (firstSeg.last().x+currentX < -5f)
		{
			terrainSegmentList.remove(0);
			updateControlPoints();
		}		
	}

	private void updateControlPoints() {
		ArrayList<Vector2> tmpList = new ArrayList<Vector2>();
		for (Downer t : terrainSegmentList) {
			for (Vector2 v : t.getPoints()) {
				tmpList.add(v);
			}
		}
		controlPoints = new Vector2[tmpList.size()];
		tmpList.toArray(controlPoints);
		path1 = new CatmullRomSpline<Vector2>(controlPoints, false);
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
}