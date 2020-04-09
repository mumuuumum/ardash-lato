package ardash.lato.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ardash.lato.Assets.SceneTexture;
import ardash.lato.weather.AmbientColorChangeListener;
import ardash.lato.LatoStage;

public class Performer extends Group implements StageAccessor, AmbientColorChangeListener {

	private static final float PERFORMER_WIDTH = 1.85f;
	private static final float MIN_SPEED = 9.3f;
	private static final float MAX_SPEED = 19.3f;
	private float speed = 0f; // speed in m/s
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
		setSpeed(MIN_SPEED);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
//		moveBy(0, -0.01f); // gravity
		
		// accelerate
		// TODO only if on ground
		final float angleToGround = 360f - velocity.angle(); // 0 or 360 is horizontal, 90 is downward, 45 is ramp down forward
//		System.out.println(angleToGround);
		if (angleToGround > 0)
		{
			if (angleToGround < 20f) // TODO adjust here. everything above this angle speeds up
			{
				setSpeed(speed-(1.1f*delta));
			}
			else if (angleToGround < 90f)
			{
				setSpeed(speed+(1.1f*delta));
			}
			else
			{
				setSpeed(speed-(1.1f*delta));
			}
		}
		
//		System.out.println(Gdx.graphics.getFramesPerSecond());
//		System.out.println(speed);
		
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
		if (speed < MIN_SPEED)
			speed = MIN_SPEED;
		if (speed > MAX_SPEED)
			speed = MAX_SPEED;
		this.speed = speed;
	}

	public Vector2 getCamSpot() {
		return camSpot;
	}

	@Override
	public void onAmbientColorChangeTriggered(Color target, float seconds) {
		// performer is also emitting ambient light in ambient color
		target = target.cpy();
		target.mul(4f); // mul == brighter ==> so the actor doesn't become black but just a bit darker
		getChild(0).addAction(Actions.color(target, seconds));
		
	}
	
	

}
