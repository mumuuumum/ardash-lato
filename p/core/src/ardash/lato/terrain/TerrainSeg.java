package ardash.lato.terrain;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

public class TerrainSeg {
	public Vector2 fromPoint;
	public Vector2 toPoint;
	public Interpolation transistion;
	public TerrainSeg(Vector2 from, Vector2 to, Interpolation transistion) {
		this.fromPoint = from;
		this.toPoint = to;
		this.transistion = transistion;
	}
}
