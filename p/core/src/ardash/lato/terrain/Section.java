package ardash.lato.terrain;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

import ardash.gdx.scenes.scene3d.Actor3D;

public class Section extends TerrainSegList {
	
	/**
	 * List of 3D Actors that are rendered behind the terrain.
	 */
	List<Actor3D> backgroundItems = new ArrayList<Actor3D>();

	/**
	 * List of 3D Actors that are rendered in front of the terrain.
	 */
	List<Actor3D> foregroundItems = new ArrayList<Actor3D>();

	public List<Actor3D> getForegroundItems() {
		return foregroundItems;
	}
	
	public List<Actor3D> getBackgroundItems() {
		return backgroundItems;
	}

	public void addOffset(Vector2 offsetOfLastSection) {
		for (Actor3D actor3d : backgroundItems) {
			actor3d.moveBy(offsetOfLastSection.x, offsetOfLastSection.y);
		}
		
		for (Actor3D actor3d : foregroundItems) {
			actor3d.moveBy(offsetOfLastSection.x, offsetOfLastSection.y);
		}
		
	}
	
}
