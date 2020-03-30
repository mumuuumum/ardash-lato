package ardash.lato.terrain;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

/**
 * A slope downhill, followed by a short straight line.
 */
public class Downer extends TerrainSegment {
	public Downer() {
		points = new ArrayList<Vector2>(5);
//		points.add(new Vector2(0,0));
		points.add(new Vector2(15,-1));
		points.add(new Vector2(18,-10));
//		points.add(new Vector2(21,-11));
//		points.add(new Vector2(30,-25));
//		points.add(new Vector2(45,-25));
	}

}
