package ardash.lato.terrain;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

/**
 * Almost straight initial terrain part.
 */
public class HomeHill extends TerrainSegment {
	public HomeHill() {
		points = new ArrayList<Vector2>(5);
		points.add(new Vector2(0,0));
		points.add(new Vector2(0.1f,0.1f));
		points.add(new Vector2(10,3));
		points.add(new Vector2(25,3));
		points.add(new Vector2(40,0));
//		points.add(new Vector2(55,3));
//		points.add(new Vector2(56,2.9f));
	}

}
