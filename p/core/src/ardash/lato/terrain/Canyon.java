package ardash.lato.terrain;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import ardash.lato.actors3.AbyssCollider;
import ardash.lato.terrain.TerrainSeg.TSType;

/**
 * 
 */
public class Canyon extends Section {
	public Canyon() {
		add (new Vector2(0,0), new Vector2(10,3), Interpolation.smooth);
		add (new Vector2(10,-30), new Vector2(25,-30), Interpolation.smooth, TSType.ABYSS);
		add (new Vector2(25,-5),new Vector2(40,-7), Interpolation.smooth);
	// TODO try physics again with polygons instead of rectangles
		
//		surroundingItems.add(new AbyssCollider(10, -40, 25, -5));
		surroundingItems.add(new AbyssCollider(10, -30, 15, 24.5f));
	}
}
