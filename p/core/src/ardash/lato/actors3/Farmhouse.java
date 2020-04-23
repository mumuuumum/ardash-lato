package ardash.lato.actors3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.MathUtils;

import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.lato.Assets;

public class Farmhouse extends Actor3D implements TerrainItem {
	
	public Farmhouse(float rotation) {
		super(getModel());
		setName("farmhouse");
		setScale(0.017f); // fan house
//		setScale(0.02f); // fan house
//        setScale(0.004f, 0.002f, 0.002f);
        translate(0, -0.6f, 0);
//        setRoll(270f);
        setPitch(rotation);
        setColor(Color.WHITE);
		
	}
	public Farmhouse() {
		this(MathUtils.random(360f));
	}
	@Override
	public void draw(ModelBatch modelBatch, Environment environment) {
		// TODO Auto-generated method stub
		super.draw(modelBatch, getStage().dirLightenvironment);
	}
	private static Model getModel() {
		Model m = getAssetManager().get(Assets.farmhouse); 
		return m;
	}

}
