package ardash.lato.terrain;

import java.util.List;

import com.badlogic.gdx.math.Path;

public class InterpolationPath<Vector2> implements Path<Vector2> {

	List<TerrainSegment> terrainSegmentList;
	
	public InterpolationPath(List<TerrainSegment> terrainSegmentList) {
		super();
		this.terrainSegmentList = terrainSegmentList;
	}

	@Override
	public Vector2 derivativeAt(Vector2 out, float t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector2 valueAt(Vector2 out, float t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float approximate(Vector2 v) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float locate(Vector2 v) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float approxLength(int samples) {
		// TODO Auto-generated method stub
		return 0;
	}


}
