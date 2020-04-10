package ardash.lato.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.AdvShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Scarf extends Group implements StageAccessor {
	
	public static final float SEG_LEN=0.3f;
	public static final float SEG_THICK=0.1f;
	
	AdvShapeRenderer sr = new AdvShapeRenderer();
	
	public Scarf(TextureRegion sTexture) {
		setName("scarf");
		Color c = Color.RED.cpy();
		for (int i = 0; i < 20; i++) {
			Image img = new Image(sTexture);
			img.setWidth(1);
			img.setHeight(0.5f);
			addActor(img);
			c.add(0.1f, 0.01f, 0.02f, 0);
			img.setColor(c);
		}

		
	}

	@Override
	public void init() {
	}

	float[] x = new float[20];
	float[] y = new float[20];
	float segLength = SEG_LEN;

//	void setup() {
//		size(640, 360);
//		strokeWeight(9);
//		stroke(255, 100);
//	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		super.draw(batch, parentAlpha);
		
		sr.setAutoShapeType(true);
		
		sr.begin();
		sr.set(ShapeType.Filled);
		sr.setProjectionMatrix(batch.getProjectionMatrix());
//		sr.line(0, 0, 100, 100);
		dragSegment(0, getX(), getY());
		for (int i = 0; i < x.length - 1; i++) {
			dragSegment(i + 1, x[i], y[i]);
		}
		sr.rectLine(0, 0, 100, 100, 5);
		sr.end();
//		background(0);
//		}
	}

	void dragSegment(int i, float xin, float yin) {
		float dx = xin - x[i];
		float dy = yin - y[i];
		float angle = MathUtils.atan2(dy, dx);
		x[i] = xin - MathUtils.cos(angle) * segLength;
		y[i] = yin - MathUtils.sin(angle) * segLength;
		segment(x[i], y[i], angle);
	}

	void segment(float x, float y, float a) {
//		pushMatrix();
//		translate(x, y);
//		rotate(a);
//		sr.rotate(0, 0, 1, 10f);
//		sr.line(x, y, x+segLength, y);
		a *= MathUtils.radiansToDegrees;
		sr.rect(x-segLength/2, y-SEG_THICK/2, 
				segLength/2, SEG_THICK/2, 
				segLength, SEG_THICK, 
	              1.0f, 1.0f, 
	              a);
		
		
//		sr.rotate(0, 0, 1, -10f);
//		popMatrix();
	}

}
