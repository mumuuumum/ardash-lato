package ardash.lato.terrain;

import java.util.List;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import net.dermetfan.gdx.math.InterpolationUtils;

public class TerrainSegment {

	protected List<Vector2> points;
	protected List<Interpolation> transistions;

	public TerrainSegment() {
		super();
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