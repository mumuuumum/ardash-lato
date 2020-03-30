package ardash.lato.terrain;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

@Deprecated
public class TerrainSegment {

	/**
	 * A list of points describing the way. The first point on the way is always 0,0 so this list starts with the second point.
	 */
	protected List<Vector2> points;
	
	/**
	 * A list of transitions describing how to get to the next point.
	 */
	protected List<Interpolation> transistions;

	public TerrainSegment() {
		super();
		points = new ArrayList<Vector2>();
		transistions = new ArrayList<Interpolation>();
	}
	
//	public float heightAt(float x)
//	{
//		if (x < first().x)
//			throw new RuntimeException("x is below the range of this Segment: "+x);
//		if (x > last().x)
//			throw new RuntimeException("x is above the range of this Segment: "+x);
//		
//
//		float ret = Interpolation.smooth.apply(0, -10, x/100f);
//		return ret;
//	}
//	
//	private int getRangeIndex() {
//		
//	}

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

	public List<Interpolation> getTransistions() {
		return transistions;
	}
	
	public void validate ()
	{
		assert points.size() >1;
		assert points.size() == transistions.size();
		for (int i=0; i<points.size()-2; i++) {
			assert points.get(i).x < points.get(i+1).x;
			assert points.get(i).dst2(points.get(i+1)) >0;
		}
	}

}