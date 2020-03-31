package ardash.lato.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ardash.lato.Assets.SceneTexture;
import ardash.lato.LatoStage;

public class Performer extends Group implements StageAccessor {

	private static final float PERFORMER_WIDTH = 1.85f;
	private float speed = 1f; // speed in m/s
//	private float direction = 0f; // current rotation (direction) in degrees
	private Vector2 velocity = new Vector2();
	
	/**
	 * A spot in front of the actor, where he wants the camera to look at. Usually a few meters in front of the actor.
	 */
	private Vector2 camSpot = new Vector2();

	@Override
	public void init() {
		Image img = new Image(getAssets().getSTexture(SceneTexture.PERFORMER));
		img.setWidth(PERFORMER_WIDTH);
		img.setHeight(PERFORMER_WIDTH);
		addActor(img);
		setOriginX(PERFORMER_WIDTH/2f);
		camSpot.set(getX(), getY());
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
//		moveBy(0, -0.01f); // gravity
		
		// apply the speed into a direction of movement
		velocity.set(1,1).setLength(speed).setAngle(getRotation());
		final float deltaX = velocity.x;
		moveBy(deltaX*delta, 0); // movement is product of time-delta and speed-delta
		
		float heightUnderActor = ((LatoStage)getStage()).getWaveDrawer().getHeightAt(getX()+(PERFORMER_WIDTH/2f));
		float heightOfMe = getY();
		moveBy(0, - (getY() - heightUnderActor));
		setRotation( ((LatoStage)getStage()).getWaveDrawer().getAngleAtX(getX()+(PERFORMER_WIDTH/2f)));
		camSpot.set(getX()+15f, getY());
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public Vector2 getCamSpot() {
		return camSpot;
	}
	
	

}
