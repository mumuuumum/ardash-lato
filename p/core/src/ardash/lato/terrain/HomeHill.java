package ardash.lato.terrain;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

/**
 * Almost straight initial terrain part.
 */
public class HomeHill extends TerrainSegList {
	public HomeHill() {
		add (new Vector2(0,0), new Vector2(10,3), Interpolation.smooth);
		add (new Vector2(10,3), new Vector2(25,3), Interpolation.smooth);
		add (new Vector2(25,3),new Vector2(40,0), Interpolation.smooth);
	}


}
