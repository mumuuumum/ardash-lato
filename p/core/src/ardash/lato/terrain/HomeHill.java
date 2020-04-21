package ardash.lato.terrain;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

import ardash.gdx.scenes.scene3d.Actor3D.Tag;
import ardash.lato.actors3.FanHouse;

/**
 * Almost straight initial terrain part.
 */
public class HomeHill extends Section {
	public HomeHill() {
		add (new Vector2(0,0), new Vector2(10,3), Interpolation.smooth);
		add (new Vector2(10,3), new Vector2(25,3), Interpolation.smooth);
		add (new Vector2(25,3),new Vector2(40,0), Interpolation.smooth);
		
//        Toonhouse h1 = new Toonhouse(45f);
//        h1.translate(10,3, -2);
//		h1.setTag(Tag.BACK);
        FanHouse h1 = new FanHouse(-170f);
        h1.translate(10,3.1f, -2);
		h1.setTag(Tag.BACK);
		
//        Toonhouse h2 = new Toonhouse(90f);
//        h2.translate(25,3.1f, -2);
//		h2.setTag(Tag.BACK);
        FanHouse h2 = new FanHouse(-70f);
        h2.translate(25,3.1f, -2);
		h2.setTag(Tag.BACK);
		
        addSurroundingItem(h1);
        addSurroundingItem(h2);

	}


}
