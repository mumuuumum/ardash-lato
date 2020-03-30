package ardash.lato.terrain;

import java.util.ArrayList;
import java.util.Collection;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.Segment;

public class TerrainSegList extends ArrayList<TerrainSeg>{
	
//	private Vector2 start = new Vector2();
	private RangeMap<Float, TerrainSeg> rm = new RangeMap<Float, TerrainSeg>();
//	protected final String type;
	public void add(Vector2 from, Vector2 to, Interpolation i) {
		add (new TerrainSeg(from, to, i));		
	}
	
	public float heightAt(float x)
	{
		if (x < first().x)
			throw new RuntimeException("x is below the range of this list: "+x);
		if (x > last().x)
			throw new RuntimeException("x is above the range of this list: "+x);
		
		// get segment to be applied
		final TerrainSeg segement = rm.mappedVal(x);
		float fromX = segement.fromPoint.x;
		float toX = segement.toPoint.x;
		float range = toX - fromX;
		float fromY = segement.fromPoint.y;
		float toY = segement.toPoint.y;
		
		float ret = segement.transistion.apply(fromY, toY, (x-fromX)/range);
		return ret;
	}
	
//	private Interpolation getInterpolatorForX(final float x) {
//		return rm.mappedVal(x);
//	}

//	private float getRangeStartForX(final float x) {
//		return rm.floorKey(x);
//	}
//
//	private float getRangeEndForX(final float x) {
//		return rm.ceilingKey(x);
//	}
	
//	public Vector2 getPoint(int index) {
//		return get(index).point;
//	}

	public Interpolation getTransistion(int index) {
		return get(index).transistion;
	}


	public Vector2 first() {
		return get(0).fromPoint;
	}

	public Vector2 last() {
		return get(size()-1).toPoint;
	}
	
//	public void validate ()
//	{
//		assert size() >1;
//		for (int i=0; i<size()-2; i++) {
//			assert get(i).point.x < get(i+1).point.x;
//			assert get(i).point.dst2(get(i+1).point) >0;
//		}
//	}
	
	public void removeFirst() {
		super.remove(0);
		updateSearchIndex();
	}
	
	@Override
	public void add(int index, TerrainSeg element) {
		throw new RuntimeException("cannot add to middle of this list. append only");
	}
	
	@Override
	public TerrainSeg remove(int index) {
		throw new RuntimeException("cannot remove from middle of this list. removeFirst only");
	}
	
	@Override
	public boolean add(TerrainSeg e) {
		final boolean ret = super.add(e);
		updateSearchIndex();
		return ret;
	}
	
	@Override
	public boolean addAll(Collection<? extends TerrainSeg> c) {
		
		// add current last point as offset to all of them
		final Vector2 offset = last();
		for (TerrainSeg ts : c) {
			ts.fromPoint.add(offset);
			ts.toPoint.add(offset);
		}
		return addAllNoOffset(c);
	}

	/**
	 * Adds new items without adding an offset. Can be used fo the initial terain part.
	 * @param c
	 * @return
	 */
	public boolean addAllNoOffset(Collection<? extends TerrainSeg> c) {
		final boolean ret = super.addAll(c);
		updateSearchIndex();
		return ret;
	}

	private void updateSearchIndex() {
		rm.clear();
		for (TerrainSeg ts : this) {
			rm.put(ts.fromPoint.x, ts);
		}
		rm.put(this.last().x, null);
		
	}
}
