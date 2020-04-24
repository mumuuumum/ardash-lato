package ardash.lato.terrain;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import ardash.gdx.scenes.scene3d.Actor3D.Tag;
import ardash.lato.actors3.TerrainItem;

public class Section extends TerrainSegList {
	
	/**
	 * List of 3D Actors that are rendered behind the terrain.
	 */
	List<TerrainItem> surroundingItems = new ArrayList<TerrainItem>();

	public List<TerrainItem> getSurroundingItems() {
		return surroundingItems;
	}

	public void addOffsetToSurroundings(Vector2 offsetOfLastSection) {
		for (TerrainItem actor3d : surroundingItems) {
			actor3d.moveBy(offsetOfLastSection.x, offsetOfLastSection.y);
		}
	}
	
	public void addOffsetToSegList(Vector2 offsetOfLastSection) {
		final Vector2 offset = offsetOfLastSection;
		for (TerrainSeg ts : this) {
			ts.fromPoint.add(offset);
			ts.toPoint.add(offset);
		}
		updateSearchIndex();
	}
	
	void addSurroundingItem(TerrainItem ti)
	{
		surroundingItems.add(ti);
		if (ti.getTag() == null)
		{
			// set missing tag based on Z value
			if (ti.getZ()>0)
				ti.setTag(Tag.FRONT);
			else if (ti.getZ()<0)
				ti.setTag(Tag.BACK);
			else
				ti.setTag(Tag.CENTER);
		}
	}
	
	@Override
	public String toString() {
		return "|- "+first()+" ->"+last();
	}
	
}
