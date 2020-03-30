package ardash.lato.terrain;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

public class InterpolatedPath {

	private List<TerrainSegment> terrainSegmentList= new LinkedList<TerrainSegment>();
	private Vector2 start = new Vector2();
	private RangeMap<Float, Interpolation> rm = new RangeMap<Float, Interpolation>();
	
	public InterpolatedPath() {
		rm.put(0.0f, Interpolation.smooth);
		rm.put(50.0f, Interpolation.smooth);
		rm.put(100.0f, null);
	}

	public float heightAt(float x)
	{
		Vector2 st = new Vector2(0,0);
		Vector2 end = new Vector2(100,10);
		float ret = Interpolation.smooth.apply(0, -10, x/100f);
		rm.mappedVal(x).apply(0, -10, x/100f);
		return ret;
	}
	
	public Vector2 vectorAt(Vector2 out, float x)
	{
		out.set(x, heightAt(x));
		return out;
	}
	
	public void addSegment(TerrainSegment ts)
	{
		terrainSegmentList.add(ts);
	}
	
	public void removeFirstSegment()
	{
		terrainSegmentList.remove(0);
		start.set(terrainSegmentList.get(0).first());
	}
	
	public TerrainSegment first() {
		return terrainSegmentList.get(0);
	}

	public TerrainSegment last() {
		return terrainSegmentList.get(terrainSegmentList.size()-1);
	}
	
	public float getStartX() {
		return first().first().x;
	}

	public float getEndX() {
		return last().last().x;
	}
}
