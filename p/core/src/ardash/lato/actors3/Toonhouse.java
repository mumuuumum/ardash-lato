package ardash.lato.actors3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.MathUtils;

import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.lato.Assets;

public class Toonhouse extends Actor3D implements TerrainItem {
	
	public Toonhouse() {
		super(getModel());
		setName("toonhouse");
        setScale(0.002f, 0.004f, 0.002f);
        translate(0, -2, 0);
        setPitch(MathUtils.random(360f));
        setColor(Color.RED);
	}

	private static Model getModel() {
		Model m = getAssetManager().get(Assets.toon_house); 
		return m;
	}

}
