package ardash.lato.actors3;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Pool.Poolable;

import ardash.gdx.scenes.scene3d.shape.Image3D;
import ardash.lato.A;
import ardash.lato.A.SpriteGroupAsset;
import ardash.lato.actors.Performer;
import ardash.lato.actors.Performer.Demise;
import ardash.lato.actors.Performer.Pose;

public class Stone extends Image3D implements TerrainItem , Poolable{
	
	boolean hasCollided;
	public Stone() {
		this(-1);
	}

	public Stone(int stoneIndex) {
		super(getTextureRegion(stoneIndex),getModelBuilder());
		setName("Stone");
		setTag(Tag.CENTER); // stones are always on center, not in background of foreground
		setScale(0.02f, 0.02f, 1);
		reset();
	}
	
	@Override
	public void reset() {
		hasCollided = false;
	}

	/**
	 * -1 for random
	 */
	private static AtlasRegion getTextureRegion(int stoneIndex) {
		if (stoneIndex == -1) {
			return A.getRandomAtlasRegion(SpriteGroupAsset.STONE);
		}
		return A.getTextureRegions("stone").get(stoneIndex);
	}

	private static ModelBuilder getModelBuilder() {
		return new ModelBuilder(); // TODO Pool or reuse a static instance
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		// by doing collision detection here we reduce the work and the code needed in Performer (ie. if there is no Stone, nothing need to be done))
		detectCollision();
	}

	private void detectCollision() {
		if (hasCollided)
			return;
		
		final float myWidth = 1.0f;
		final float pWidth = 1.0f;
		final float myX = getX()+myWidth/2f;
		System.out.println(myX);
		final Performer performer = getGameScreen().performer;
		final float pX = performer.getX() + pWidth/2f;
		
		if (Math.abs(myX - pX) < myWidth) {
			
			// check Y coords too
			final float stoneYtop = getY()+1f;
			final float pYbottom = performer.getY();
			
			if (stoneYtop > pYbottom) {
				if (performer.getState().isInAir()) {
					performer.setCauseOfDeath(Demise.LAND_ON_STONE);
				} else {
					performer.setCauseOfDeath(Demise.HIT_STONE);
				}
				performer.crash(Pose.CRASH_NOSE);
				hasCollided = true;
			}
		}
	}
}
