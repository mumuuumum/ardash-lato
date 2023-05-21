package ardash.lato.actors3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool.Poolable;

import ardash.gdx.scenes.scene3d.pooling.PoolableActor3D;
import ardash.lato.Assets;

public class Farmhouse extends PoolableActor3D implements TerrainItem{
	
//	private Farmhouse(float rotation) {
//		super(getModel());
//		setName("farmhouse");
//		setScale(2.17f); // farmhouse
//        translate(0, -0.6f, 0);
//        setColor(Color.WHITE);
//	}
	public Farmhouse() {
		super(getModel());
		setName("farmhouse");
		setScale(2.17f); // farmhouse
        translate(0, -0.6f, 0);
        setColor(Color.WHITE);
	}
	@Override
	public void draw(ModelBatch modelBatch, Environment environment) {
		// TODO Auto-generated method stub
		super.draw(modelBatch, getStage().dirLightenvironment);
	}
	private static Model getModel() {
		Model m = getAssetManager().get(Assets.FARMHOUSE); 
		return m;
	}
	@Override
	public void reset() {
		super.reset();
	}
	
	@Override
	public void init() {
		super.init();
		setPitch(MathUtils.random(360f));
	}
	public void init(float rotation) {
		this.init();
        setPitch(rotation);
	}
}
