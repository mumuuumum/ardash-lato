package ardash.lato.terrain;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * A slope downhill, followed by a short straight line.
 */
public class Downer extends TerrainSegList {
	static final float MIN_WAY_FORWARD = 20f;
	static final float MAX_WAY_FORWARD = 140f;
	static final float MIN_WAY_DOWNWARD = 11f;
	static final float MAX_WAY_DOWNWARD = 170f;
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
	}

	static private void makeNewRandomVector() {
		
//		while (true)
		{
			prevRandomVector.set(currentRandomVector);
			float x = MathUtils.random(MIN_WAY_FORWARD, MAX_WAY_FORWARD);
			float y = MathUtils.random(-MIN_WAY_DOWNWARD, -MAX_WAY_DOWNWARD);
			currentRandomVector.set(x,y);
			
			// the brute algorithm: generate anything and discard it if it was shit
			float angle = currentRandomVector.cpy().sub(prevRandomVector).angle();
//			System.out.println("angle "+ angle);
			if (angle > 100f)
			{
				return;
			}
		}
		
	}

}
