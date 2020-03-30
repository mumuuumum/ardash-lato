package ardash.lato.terrain;

import java.util.ArrayList;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

/**
 * A slope downhill, followed by a short straight line.
 */
public class Downer extends TerrainSegList {
	public Downer() {
		add (new Vector2(0,0), new Vector2(10,-10), Interpolation.smooth);
		add (new Vector2(10,-10), new Vector2(20,-11), Interpolation.smooth);
		
	}


}
