package ardash.lato.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.AdvShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.gdx.scenes.scene3d.Group3D;
import ardash.gdx.scenes.scene3d.shape.Image3D;

public class Scarf extends Group3D {
	
	public static final float SEG_LEN=0.3f;
	public static final float SEG_THICK=0.1f;
	
	AdvShapeRenderer sr = new AdvShapeRenderer();
	
	public Scarf(TextureRegion sTexture) {

		ModelBuilder mb = new ModelBuilder();
		setName("scarf");
		Color c = Color.RED.cpy();
		for (int i = 0; i < 20; i++) {
			Image3D img = new Image3D(SEG_LEN,SEG_THICK, sTexture, mb);
//			img.setWidth(1);
//			img.setHeight(0.5f);
			addActor(img);
			c.add(0.1f, 0.01f, 0.02f, 0);
			img.setColor(c);
		}
		
		// PROBLEM: they dont move in the absolute cordinate system. Move them into sepatate actor next to the performer. problem is get getX ist always the same in local coodsystem
		// also it would not work becasue the rotation of the actor would be added, it mut be global and sepparate
//		for (int i = 0; i < y.length; i++) {
//			y[i] = i * segLength;
//		}
		
	}

	float[] x = new float[20];
	float[] y = new float[20];
//	float segLength = SEG_LEN;

//	void setup() {
//		size(640, 360);
//		strokeWeight(9);
//		stroke(255, 100);
//	}

//	@Override
//	public void draw(ModelBatch batch, Environment environment) {
//		// TODO Auto-generated method stub
//		super.draw(batch, environment);
////		batch.end();
////		
////		sr.setAutoShapeType(true);
////		
////		sr.begin();
////		sr.set(ShapeType.Filled);
////		sr.setProjectionMatrix(batch.getCamera().projection);
//////		sr.line(0, 0, 100, 100);
////		dragSegment(0, getX(), getY());
////		for (int i = 0; i < x.length - 1; i++) {
////			dragSegment(i + 1, x[i], y[i]);
////		}
//////		sr.rectLine(0, 0, 100, 100, 5);
////		sr.end();
//////		background(0);
//////		}
////		
////		batch.begin(batch.getCamera());
//	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		dragSegment(0, getX(), getY());
		for (int i = 0; i < x.length - 1; i++) {
			dragSegment(i + 1, x[i], y[i]);
//			System.out.print("x: " + x[i] +"y: " + y[i]);
		}
	}

	void dragSegment(int i, float xin, float yin) {
		float dx = xin - x[i];
		float dy = yin - y[i];
		float angle = MathUtils.atan2(dy, dx);
		x[i] = xin - MathUtils.cos(angle) * SEG_LEN;
		y[i] = yin - MathUtils.sin(angle) * SEG_LEN;
//		segment(x[i], y[i], angle);
		segment3d(i, x[i], y[i], angle);
	}

	void segment3d(int i, float x, float y, float a) {
		a *= MathUtils.radiansToDegrees;
//		sr.rect(x-segLength/2, y-SEG_THICK/2, 
//				segLength/2, SEG_THICK/2, 
//				segLength, SEG_THICK, 
//	              1.0f, 1.0f, 
//	              a);
		final Actor3D ch = getChild(i);
		ch.setPosition(x, y);
//		ch.setPosition(x+1, y);
		ch.setRotation(a);
		
	}
	
	@Override
	public void setPosition(float x, float y) {
		getChild(0).setPosition(x, y);
//		System.out.println("scarf x "+x + " y "+ y);
	}
	
	@Override
	public float getX() {
		return getChild(0).getX();
	}

	@Override
	public float getY() {
		return getChild(0).getY();
	}

//	void segment(float x, float y, float a) {
////		pushMatrix();
////		translate(x, y);
////		rotate(a);
////		sr.rotate(0, 0, 1, 10f);
////		sr.line(x, y, x+segLength, y);
//		a *= MathUtils.radiansToDegrees;
//		sr.rect(x-segLength/2, y-SEG_THICK/2, 
//				segLength/2, SEG_THICK/2, 
//				segLength, SEG_THICK, 
//	              1.0f, 1.0f, 
//	              a);
//		
//		
////		sr.rotate(0, 0, 1, -10f);
////		popMatrix();
//	}

}
