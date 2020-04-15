package ardash.lato.actors;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter.GradientColorValue;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;

import ardash.lato.Assets;
import ardash.lato.Assets.SceneTexture;
import ardash.lato.LatoStage;
import ardash.lato.actions.MoreActions;
import ardash.lato.weather.AmbientColorChangeListener;

public class Performer extends Group implements StageAccessor, Disposable, AmbientColorChangeListener {

	private static final float ROTATION_SPEED = 180f; // TODO (deg/sec) this could be different for different performers or boards
	private static final float PERFORMER_WIDTH = 1.85f;
	private static final float MIN_SPEED = 9.3f;
	private static final float MAX_SPEED = 29.3f;
	private float speed = 0f; // speed in m/s
	private boolean isInAir = false;
	private boolean isUserInputDown = false;
//	private float direction = 0f; // current rotation (direction) in degrees
	private Vector2 velocity = new Vector2(); // this is only here to safe new-calls
//	private float gravity = 9.807f; // m/s/s
	private ParallelAction jumpAction;
	private ArrayList<SpeedListener> speedListeners = new ArrayList<SpeedListener>();
	ParticleEffect snowSpray = new ParticleEffect();
	private Actor ambientColorContainer = new Actor();
	private List<PerformerListener> listeners = new ArrayList<Performer.PerformerListener>();

	/**
	 * vertical speed is intentionally not in a vector with 'speed' because the velocity is handled differently
	 * depending on if actor is in air or on ground. Physics on ground are not realistic to improve gameplay.
	 */
	private float vspeed = 0f; // speed in m/s
	
	/**
	 * A spot in front of the actor, where he wants the camera to look at. Usually a few meters in front of the actor.
	 */
	private Vector2 camSpot = new Vector2();
	
	public interface PerformerListener{
		void onPositionChange(float newX, float newY);
	}

//	@Override
	public void init() {
		Image img = new Image(getAssets().getSTexture(SceneTexture.PERFORMER));
		img.setWidth(PERFORMER_WIDTH);
		img.setHeight(PERFORMER_WIDTH);
		addActor(img);
		setOriginX(PERFORMER_WIDTH/2f);
		camSpot.set(getX(), getY());
		setSpeed(MIN_SPEED);
		
		addActor(ambientColorContainer);
		
		TextureAtlas ta = getAssetManager().get(Assets.uiAtlas);
		snowSpray.load( Gdx.files.internal("spray.p"), ta);
		snowSpray.scaleEffect(0.09f);
		snowSpray.setPosition(-22f, 20f);
		snowSpray.start();
		
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		// accelerate on ground
		if (! isInAir)
		{
			final float angleToGround = 360f - velocity.angle(); // 0 or 360 is horizontal, 90 is downward, 45 is ramp down forward
//			System.out.println(angleToGround);
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
		}
		
//		System.out.println(Gdx.graphics.getFramesPerSecond());
//		System.out.println(speed);
		final float rotation = getRotation() < 0f ? getRotation() + 360f : getRotation();
//		System.out.println(rotation);
		
		// apply the speed into a direction of movement, which is the direction of the terrain, or straight forward (angle 0) when in air
		velocity.set(1,1).setLength(speed).setAngle(isInAir ? 0f : rotation);
		final float deltaX = velocity.x;
		moveBy(deltaX*delta, 0); // movement is product of time-delta and speed-delta
		
		float heightUnderActor = ((LatoStage)getStage()).getWaveDrawer().getHeightAt(getX()+(PERFORMER_WIDTH/2f));
		float heightOfMe = getY();
		// set the height of the terrain under the actor if not in air
		if (! isInAir)
		{
			setOriginY(0);
			moveBy(0, - (getY() - heightUnderActor));
			setRotation( ((LatoStage)getStage()).getWaveDrawer().getAngleAtX(getX()+(PERFORMER_WIDTH/2f)));
		}
		else
		{
			setOriginY(PERFORMER_WIDTH/2f);
			// if jumping or otherwise flying just apply a bit gravity
//			moveBy(0, - 0.2f);
			if (heightUnderActor > heightOfMe) // check if hit the ground
			{
				land();
			}
			
			// if input touch down rotate counter clockwise, otherwise rotate towards ground
			if (isUserInputDown)
			{
				rotateBy(ROTATION_SPEED*delta);
			}
			else
			{
//				if (ro)
				float direction = rotation > 180 ? 1 : -1;
				rotateBy(ROTATION_SPEED*0.3f*direction*delta);
				
			}
		}
		camSpot.set(getX()+15f, getY());
		
		// move snow spray
		final Pool<Vector2> vecPool = Pools.get(Vector2.class);
		Vector2 corner = vecPool.obtain();
		corner.set(0,0);
		this.localToParentCoordinates(corner);
		snowSpray.setPosition(corner.x, corner.y);
		vecPool.free(corner);
		snowSpray.update(delta);
		if (isInAir)
			snowSpray.allowCompletion();
		else
			snowSpray.start();
			
		// inform listeners about new position
		System.out.println("Performer is at: "+ getX() + ","+ getY());
		for (PerformerListener listener : listeners) {
			listener.onPositionChange(getX(), getY());
		}

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		final GradientColorValue tint = snowSpray.getEmitters().get(0).getTint();
		final Color amb = ambientColorContainer.getColor();
		tint.setColors(new float[]{amb.r, amb.g, amb.b});
		snowSpray.draw(batch);
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
		
		// inform listeners
		for (SpeedListener speedListener : speedListeners) {
			speedListener.onSpeedChanged(speed, getSpeedPercentage());
		}
	}

	public float getSpeedPercentage() {
		float max = MAX_SPEED - MIN_SPEED;
		float cur = speed - MIN_SPEED;
		return cur/max;
	}

	public Vector2 getCamSpot() {
		return camSpot;
	}

	@Override
	public void onAmbientColorChangeTriggered(Color target, float seconds) {
		ambientColorContainer.addAction(Actions.color(target, seconds));		

		// performer is also emitting ambient light in ambient color
		target = target.cpy();
		target.mul(4f); // mul == brighter ==> so the actor doesn't become black but just a bit darker
		getChild(0).addAction(Actions.color(target, seconds));
	}

	/**
	 * handle the only possible user input (on the game stage): touch anywhere on the screen
	 * @param down touch up or touch down
	 */
	public void userInput(boolean touchDown) {
		isUserInputDown = touchDown;
		if (!isInAir)
		{
			if (touchDown)
			{
				jump();
			}
		}
		
	}
	
	@Override
	public float getRotation() {
		float rotation = super.getRotation();
		rotation %= 360f;
		if (rotation <0f)
			rotation = 360 + rotation;
		return rotation;
	}
	
	private void jump() {
		isInAir  = true;
		final MoveByAction jumpForce = Actions.moveBy(0, 8, 1f, Interpolation.fastSlow);
//		final MoveByAction accelleratedFall = Actions.moveBy(0, -15f*3, 1f*3, Interpolation.slowFast); // increasing speed due to gravity
//		final RepeatAction constantFall = Actions.forever(Actions.moveBy(0, -15f*3, 1f*3));
//		final SequenceAction fall = Actions.sequence(accelleratedFall,constantFall);
		final GravityAction gravity = MoreActions.gravity();
		jumpAction = Actions.parallel(jumpForce, gravity );
		addAction(jumpAction);
		
	}
	
	/** touching down after a jump or fall*/
	private void land() {
		isInAir = false;
		removeAction(jumpAction); // ensure no more up or down (gravity) is applied
	}
	
	public void addSpeedListener (SpeedListener listener)
	{
		speedListeners.add(listener);
	}

	public void addListener (PerformerListener listener)
	{
		listeners.add(listener);
	}
	
	@Override
	public void dispose() {
		Disposables.gracefullyDisposeOf(snowSpray);
	}



}
