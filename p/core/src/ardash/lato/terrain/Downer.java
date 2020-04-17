package ardash.lato.terrain;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import ardash.gdx.scenes.scene3d.Actor3D.Tag;
import ardash.lato.actors3.Spruce;

/**
 * A slope downhill, followed by a short straight line.
 */
public class Downer extends Section {
	static final float MIN_WAY_FORWARD = 20f;
	static final float MAX_WAY_FORWARD = 140f;
	static final float MIN_WAY_DOWNWARD = 11f;
	static final float MAX_WAY_DOWNWARD = 70f;
	static final Vector2 currentRandomVector= new Vector2();
	static final Vector2 prevRandomVector= new Vector2(); // needed to calculate the angle
	static final float MIN_WAY_STRAIGHT = 0f;
	static final float MAX_WAY_STRAIGHT = 20f;
	static final float MAX_ANGLE = 20f;
	
	/**
	 * the not randomised version is just this:
		add (new Vector2(0,0), new Vector2(20,-11), Interpolation.smooth);
		add (new Vector2(22,-11), Interpolation.smooth);
	 * 
	 */
	public Downer() {
		makeNewRandomVector();
		add (new Vector2(0,0), new Vector2(currentRandomVector), Interpolation.exp5);
		float straight = MathUtils.randomTriangular(MIN_WAY_STRAIGHT, MAX_WAY_STRAIGHT, MIN_WAY_STRAIGHT);
//		add (new Vector2(currentRandomVector.x+straight, currentRandomVector.y), Interpolation.smooth);
		
		// add trees
		Spruce tree = new Spruce();
		tree.translate(1f, -2, -1);
		tree.setTag(Tag.BACK);
		backgroundItems.add(tree);
		Spruce tree2 = new Spruce();
		tree2.translate(-1f, 0, 1);
		tree2.setTag(Tag.FRONT);
		backgroundItems.add(tree2);

//		Spruce tree3 = new Spruce();
//		tree3.translate(1f, -2, 0);
//		foregroundItems.add(tree3);
//		Spruce tree4 = new Spruce();
//		tree4.translate(-1f, 0, -1);
//		foregroundItems.add(tree4);
}

	static private void makeNewRandomVector() {
		
//		while (true) // TODO
		{
			prevRandomVector.set(currentRandomVector);
			float x = MathUtils.random(MIN_WAY_FORWARD, MAX_WAY_FORWARD);
			float y = MathUtils.random(-MIN_WAY_DOWNWARD, -MAX_WAY_DOWNWARD);
			currentRandomVector.set(x,y);
			
			// the brute algorithm: generate anything and discard it if was shit
			float angle = currentRandomVector.cpy().sub(prevRandomVector).angle();
//			System.out.println("angle "+ angle);
			if (angle > 100f)
			{
				return;
			}
		}
		
	}

}
