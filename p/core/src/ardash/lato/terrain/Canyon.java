package ardash.lato.terrain;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import ardash.gdx.scenes.scene3d.shape.Image3D;
import ardash.lato.actors3.AbyssCollider;
import ardash.lato.actors3.CliffLeft;
import ardash.lato.actors3.TerrainItem;
import ardash.lato.terrain.TerrainSeg.TSType;

/**
 * 
 */
public class Canyon extends Section {
	public Canyon() {
		add (new Vector2(0,0), new Vector2(10,3), Interpolation.smooth);
		add (new Vector2(10,-30), new Vector2(25,-30), Interpolation.smooth, TSType.ABYSS);
		add (new Vector2(25,-5),new Vector2(40,-7), Interpolation.smooth);
	// TODO add cliff-sides
		// TODO add fog in bottom
		
//		surroundingItems.add(new AbyssCollider(10, -40, 25, -5));
		// note: we can't use the collider to show fog
		surroundingItems.add(new AbyssCollider(9, -30, 17, 24.5f));
		
		// cliffs can't be attached perfectly to the edge, because the share renderer moves it slightly, especially when removing old items
		TerrainItem cliffLeft = new CliffLeft(4.42f,-17.95f); 
		surroundingItems.add(cliffLeft);
		// TODO don't do culling the t segments when a canyon is on the screen
	}
}
