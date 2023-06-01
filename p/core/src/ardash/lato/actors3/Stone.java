package ardash.lato.actors3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import ardash.gdx.scenes.scene3d.shape.Image3D;
import ardash.lato.A;
import ardash.lato.A.ARAsset;

public class Stone extends Image3D implements TerrainItem {
	
	public Stone() {
		super(getTextureRegion(),getModelBuilder());
		
//		Image3D img = new Image3D(PERFORMER_WIDTH,PERFORMER_WIDTH,poseTextureRegion,mb);
//
//		super(5,5,new Color(48 / 255f, 105 / 255f, 105 / 255f, 1f) ); //back color
//		super(5,5,new Color(32 / 255f, 69 / 255f, 69 / 255f, 1f) ); //front color
		setName("Stone");
		setTag(Tag.CENTER); // stones are always on center, not in background of foreground
//		rotateBy(45f);
		setScale(0.02f, 0.02f, 1);
//		moveBy(0, -35f);
//		scale(0.01f);
//		setScale(1,1,1);
	}

	/*
	 * Problem: region is dynamycally randomly picked here. It must be static because it is in the consttor call
	 */
	private static AtlasRegion getTextureRegion() {
//		final AtlasRegion ar = A.getTextureRegion(A.SpriteGroupAsset.STONE.getRandom());
		final AtlasRegion ar = A.getTextureRegions("stone").get(2);
		return ar;
	}

	private static ModelBuilder getModelBuilder() {
		return new ModelBuilder(); // TODO Pool or reuse a static instance
	}

}
