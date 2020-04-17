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

	public void addOffset(Vector2 offsetOfLastSection) {
		for (TerrainItem actor3d : surroundingItems) {
			actor3d.moveBy(offsetOfLastSection.x, offsetOfLastSection.y);
		}
		
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
	
}
