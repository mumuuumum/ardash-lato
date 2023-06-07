package ardash.lato.actors3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.math.MathUtils;

import ardash.gdx.scenes.scene3d.pooling.PoolableActor3D;
import ardash.lato.A;
import ardash.lato.A.ModelAsset;

public class Coin extends PoolableActor3D implements TerrainItem{
	private static Color emissiveLightColour = Color.GOLD.cpy();
	static {
		emissiveLightColour.lerp(Color.BLACK, 0.65f);
	}
//	private Farmhouse(float rotation) {
//		super(getModel());
//		setName("farmhouse");
//		setScale(2.17f); // farmhouse
//        translate(0, -0.6f, 0);
//        setColor(Color.WHITE);
//	}
	public Coin() {
		super(getModel());
		setName("coin");
		setScale(0.00417f); // farmhouse
        translate(0, 0.6f, 0);
        setColor(Color.GOLD);
        materials.get(0).set(ColorAttribute.createSpecular(Color.WHITE));
//        Color emissiveLightColour = Color.GOLD.cpy();
//        emissiveLightColour.a = 0.0f;
		materials.get(0).set(ColorAttribute.createEmissive(emissiveLightColour));
		materials.get(0).set(FloatAttribute.createShininess(1.0f));
        setRoll(90f);
        setTag(Tag.CENTER);
        init();
	}
	
	int i = 0;
	@Override
		public void act(float delta) {
			super.act(delta);
			setYaw(getYaw()-3f);
//			if (i++ %60 == 0) {
//				materials.get(0).set(ColorAttribute.createEmissive(Color.GOLDENROD));
//			} else {
//				materials.get(0).remove(ColorAttribute.Emissive);
//			}
		}
	
	@Override
	public void draw(ModelBatch modelBatch, Environment environment) {
		// TODO Auto-generated method stub
		super.draw(modelBatch, getStage().dirLightenvironment);
	}
	private static Model getModel() {
		Model m = A.getModel(ModelAsset.YCOIN); 
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
