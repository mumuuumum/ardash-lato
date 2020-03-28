package ardash.lato.terrain;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

/**
 * A slope downhill, followed by a short straight line.
 */
public class Downer {
	List<Vector2> points;
	
	public Downer() {
		points = new ArrayList<Vector2>(5);
//		points.add(new Vector2(0,0));
		points.add(new Vector2(5,0));
		points.add(new Vector2(10,0));
		points.add(new Vector2(15,-20));
		points.add(new Vector2(20,-25));
		points.add(new Vector2(25,-28));
	}
	
	public Vector2 first() {
		return points.get(0);
	}
	
	public Vector2 last() {
		return points.get(points.size()-1);
	}
		
	public List<Vector2> getPoints() {
		return points;
	}
	
	public int getSize() {
		return points.size();
	}

}
