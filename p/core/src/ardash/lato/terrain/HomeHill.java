package ardash.lato.terrain;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import ardash.gdx.scenes.scene3d.Actor3D.Tag;
import ardash.lato.actors3.Toonhouse;

/**
 * Almost straight initial terrain part.
 */
public class HomeHill extends Section {
	public HomeHill() {
		add (new Vector2(0,0), new Vector2(10,3), Interpolation.smooth);
		add (new Vector2(10,3), new Vector2(25,3), Interpolation.smooth);
		add (new Vector2(25,3),new Vector2(40,0), Interpolation.smooth);
		
        Toonhouse ma = new Toonhouse();
        ma.translate(0,0, -2);
		ma.setTag(Tag.BACK);
        addSurroundingItem(ma);

	}


}
