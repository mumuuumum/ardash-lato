package ardash.lato.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Collections;
import com.badlogic.gdx.utils.Disposable;

public class WaveDrawer extends Actor implements Disposable {

	private ShapeRenderer sr;
	private CatmullRomSpline<Vector2> path1;
	Vector2[] controlPoints;

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

		final int fragments = 200;
		final float fragmentLength = 1 / (float) fragments;
		float lastEnd = fragmentLength;
		path1.valueAt(st, 0); // get first position

		// draw path1
		for (int i = 0; i < fragments; ++i) {
			path1.valueAt(end, lastEnd);

			// draw body below curve
//			sr.line(st.x, st.y, end.x, end.y);
			sr.set(ShapeType.Filled);
			sr.rect(st.x, st.y, end.x - st.x, -100f);

			// draw the curve
			sr.line(st.x, st.y, end.x, end.y);

			// prepare next round
			st.set(end);
			lastEnd += fragmentLength;
		}

		sr.setColor(Color.LIME);
		// draw control points
		for (Vector2 v : controlPoints) {
			sr.line(v.x, v.y, v.x + 1, v.y + 1);
		}

		sr.end();

		batch.begin();

	}

	@Override
	public void act(float delta) {
		super.act(delta);
		updateControlPoints();
		moveBy(-0.1f, 0f); // TODO add speed
	}

	private void updateControlPoints() {
		final float currentX = getX();
		System.out.println("terrain x: " + currentX);
		System.out.println("terrain cp1: " + controlPoints[1]);

		// check if the first 2 control points are beyond X
		if (controlPoints[1].x+currentX < -5f) {
			// shift array left
			for (int i = 1; i < controlPoints.length; i++) {
				controlPoints[i-1].set(controlPoints[i]);
			}
			
			// add new point in the end
			controlPoints[controlPoints.length-1].set(controlPoints[controlPoints.length-2].x+5f, controlPoints[controlPoints.length-2].y+MathUtils.random(-5f,5f));
			
			path1 = new CatmullRomSpline<Vector2>(controlPoints, false);
		}

	}

	@Override
	public void moveBy(float x, float y) {
		super.moveBy(x, y);
		sr.translate(x, y, 0);

	}

	@Override
	public void dispose() {
		sr.dispose();
	}
}