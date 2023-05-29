package ardash.lato.actors;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.utils.Disposable;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;

import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.gdx.scenes.scene3d.Camera3D;
import ardash.gdx.scenes.scene3d.Group3D;
import ardash.gdx.scenes.scene3d.actions.MoveByAction;
import ardash.gdx.scenes.scene3d.actions.ParallelAction;
import ardash.gdx.scenes.scene3d.shape.Image3D;
import ardash.lato.Assets;
import ardash.lato.Assets.SceneTexture;
import ardash.lato.LatoStage3D;
import ardash.lato.actions.Actions;
import ardash.lato.actions.GravityAction;
import ardash.lato.actors3.Physical;
import ardash.lato.weather.AmbientColorChangeListener;

public class Performer extends Group3D implements Disposable, AmbientColorChangeListener {
	
	private enum Pose {
		RIDE, DUCK, JUMP//, ROLL, FLY, CRASHED, GRIND
	}

	private static final float ROTATION_SPEED = 180f; // TODO (deg/sec) this could be different for different performers or boards
	private static final float PERFORMER_WIDTH = 1.85f;
	public static final float MIN_SPEED = 9.3f;
	private static final float MAX_SPEED = 29.3f;
	private static final float MIN_CAM_SPOT_X = 10f;
	private static final float MAX_CAM_SPOT_X = 24f;
	private static final float JUMP_FORCE = PERFORMER_WIDTH*1.9f;

	private float speed = 0f; // speed in m/s
	private float runtime = 0f; // lifetime starting after game started
//	private boolean isInAir = false;
	private boolean isUserInputDown = false;
//	private float direction = 0f; // current rotation (direction) in degrees
	private Vector2 velocity = new Vector2(); // this is only here to safe new-calls
//	private float gravity = 9.807f; // m/s/s
	ParticleEffect snowSpray = new ParticleEffect();
	private Image3D ambientColorContainer = new Image3D(1, 1, new Color(), new ModelBuilder());
	private List<PerformerListener> listeners = new ArrayList<Performer.PerformerListener>();
	private Map<Pose,Image3D> poses = new EnumMap<Pose, Image3D>(Pose.class);
	protected Pose pose = Pose.RIDE;
	protected PlayerState state = PlayerState.INIT;
	
	/**
	 * vertical speed is intentionally not in a vector with 'speed' because the velocity is handled differently
	 * depending on if actor is in air or on ground. Physics on ground are not realistic to improve gameplay.
	 */
	private float vspeed = 0f; // speed in m/s
	
	/**
	 * A spot in front of the actor, where he wants the camera to look at. Usually a few meters in front of the actor.
	 */
	private Vector2 camSpot = new Vector2();
//	public int currentContacts = 0;
	private float airTime;
	private float timeInState;
	
	public interface PerformerListener{
		void onPositionChange(float newX, float newY);
		void onSpeedChanged(float newSpeed, float percentage);
	}
	
	public Performer() {
		ModelBuilder mb = new ModelBuilder();
		setName("Performer");
		setTag(Tag.CENTER);
		for (Pose pose : Pose.values()) {
			String performer = "P1";
			final String posename = performer+"_"+pose.name().toUpperCase();
			SceneTexture sprite = SceneTexture.valueOf(posename);
			Image3D img = new Image3D(PERFORMER_WIDTH,PERFORMER_WIDTH,getAssets().getSTexture(sprite),mb);
			img.setName(posename);
			addActor(img);
			poses.put(pose, img);
//			setDebug(true, true);
//			img.setDebug(true);
		}
		setPose(Pose.RIDE);
		setOriginX(-PERFORMER_WIDTH/2f);
		camSpot.set(getX(), getY()+10f);
		setSpeed(MIN_SPEED);
		
		addActor(ambientColorContainer);
		ambientColorContainer.setVisible(false);
		
		TextureAtlas ta = getAssetManager().get(Assets.SCENE_ATLAS);
		snowSpray.load( Gdx.files.internal("spray.p"), ta);
		snowSpray.scaleEffect(0.09f);
		snowSpray.setPosition(-22f, 20f);
		snowSpray.start();
		

	}
	
	private float accum = 0;
	private float step = 1/60f;
	
	@SuppressWarnings("deprecation")
	public void act(float delta) {
		final float previousX = getX();
		final float previousY = getY();
		accum += delta; 
//		while (accum >= step) {
//			System.out.println("d : "+state);
			super.act(delta);
			if (state.isStarted())
			{
				runtime += delta;
				timeInState += delta;
			}
	
			// rotation for the forward movement (ignored when in air)
			final float rotation = getRotation() < 0f ? getRotation() + 360f : getRotation();
	
			if (state.isInAir()) {
				// apply the speed into a direction of movement, which is the direction of the terrain, or straight forward (angle 0) when in air
				moveBy(speed*delta, 0); // movement is product of time-delta and speed-delta
				
				setOriginY(-PERFORMER_WIDTH/2f);
				
	//			//register landing
				final float heightUnderActorBeforeForwardMovement = getGameScreen().waveDrawer.getHeightAt(getX()+(PERFORMER_WIDTH/2f));
				if (getY()+getOriginY()<heightUnderActorBeforeForwardMovement)
				{
					System.out.println("hua: " +heightUnderActorBeforeForwardMovement+ " , Y: "+getY());
					
					// land only if in air since longer time
					if (timeInState >= 0.1f)
					{
						land();
					}
				} else 
	//			if (heightUnderActor > heightOfMe) // check if hit the ground
	//			{
	//				land();
	//				getActions().clear();
	//			}
				{
					// if input touch down rotate counter clockwise, otherwise rotate towards ground
					if (isUserInputDown)
					{
						setPose(Pose.RIDE); // TODO set to roll
						rotateBy(ROTATION_SPEED*delta);
					}
					else
					{
						setPose(Pose.JUMP);
						float direction = rotation > 180 ? 1 : -1;
						rotateBy(ROTATION_SPEED*0.3f*direction*delta);
					}
					
				}
			} else {
				// apply the speed into a direction of movement, which is the direction of the terrain, or straight forward (angle 0) when in air
				velocity.set(1,1).setLength(speed).setAngle(state.isInAir() ? 0f : rotation);
				final float deltaX = velocity.x;
				moveBy(deltaX*delta, 0); // movement is product of time-delta and speed-delta
	
				// rotation point is at the feet when actor is on the ground
				setOriginY(0);
	
				// we use setPosition instead of setY(), so setPosition can be overwritten for smoother movement
				// set the height of the terrain under the actor if not in air
				float heightUnderActor = getGameScreen().waveDrawer.getHeightAt(getX()+(PERFORMER_WIDTH/2f));
				setPosition(getX(), heightUnderActor);
	
				// set rotation to what the ground is under the actor
				setRotation( getGameScreen().waveDrawer.getAngleAtX(getX()+(PERFORMER_WIDTH/2f)));
	
				// accelerate on ground
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
						setSpeed(speed+(5.1f*delta));
					}
					else
					{
						setSpeed(speed-(1.1f*delta));
					}
				}
	
				if (isUserInputDown)
				{
					jump(JUMP_FORCE);
				}
	
			}
	//		//register landing
	//		final float heightUnderActorBeforeForwardMovement = getGameScreen().waveDrawer.getHeightAt(getX()+(PERFORMER_WIDTH/2f));
	//		if (state.isInAir() && getY()<heightUnderActorBeforeForwardMovement)
	//		{
	//			land();
	//			getActions().clear();
	//		}
	//		
	//		// rotation for the forward movement (ignored when in air)
	//		final float rotation = getRotation() < 0f ? getRotation() + 360f : getRotation();
	//		
	//		// apply the speed into a direction of movement, which is the direction of the terrain, or straight forward (angle 0) when in air
	//		velocity.set(1,1).setLength(speed).setAngle(state.isInAir() ? 0f : rotation);
	//		final float deltaX = velocity.x;
	//		moveBy(deltaX*delta, 0); // movement is product of time-delta and speed-delta
	//
	//		// accelerate on ground
	//		if (! state.isInAir())
	//		{
	//			final float angleToGround = 360f - velocity.angle(); // 0 or 360 is horizontal, 90 is downward, 45 is ramp down forward
	////			System.out.println(angleToGround);
	//			if (angleToGround > 0)
	//			{
	//				if (angleToGround < 20f) // TODO adjust here. everything above this angle speeds up
	//				{
	//					setSpeed(speed-(1.1f*delta));
	//				}
	//				else if (angleToGround < 90f)
	//				{
	//					setSpeed(speed+(5.1f*delta));
	//				}
	//				else
	//				{
	//					setSpeed(speed-(1.1f*delta));
	//				}
	//			}
	//		}
	//
	//		// at this stage the actor is already moved slightly forward and the speed is adjusted
	//
	//		
	////		final float rotation = getRotation() < 0f ? getRotation() + 360f : getRotation();
	//
	//		float heightUnderActor = getGameScreen().waveDrawer.getHeightAt(getX()+(PERFORMER_WIDTH/2f));
	//		float heightOfMe = getY();
	//		if (! state.isInAir())
	//		{
	////			fake jump for sudden abyss
	////			vspeed = heightOfMe - heightUnderActor;
	////			if (vspeed > 0.2f)
	////			{
	//////				jump(0f);
	//////				return;
	////			}
	//			
	//			// rotation point is at the feet when actor is on the ground
	//			setOriginY(0);
	//
	//			// we use setPosition instead of setY(), so setPosition can be overwritten for smoother movement
	//			// set the height of the terrain under the actor if not in air
	//			setPosition(getX(), heightUnderActor);
	//
	//			
	//			// set rotation to what the ground is under the actor
	//			setRotation( getGameScreen().waveDrawer.getAngleAtX(getX()+(PERFORMER_WIDTH/2f)));
	//
	//			// TODO continue review here
	//			if (isUserInputDown)
	//			{
	//				jump(JUMP_FORCE);
	//			}
	//
	//		}
	//		else
	//		{
	//			setOriginY(-PERFORMER_WIDTH/2f);
	//			
	////			//register landing
	////			final float heightUnderActorBeforeForwardMovement = getGameScreen().waveDrawer.getHeightAt(getX()+(PERFORMER_WIDTH/2f));
	////			if (state.isInAir() && getY()<heightUnderActorBeforeForwardMovement)
	////			{
	////				land();
	////				getActions().clear();
	////			}
	//			if (heightUnderActor > heightOfMe) // check if hit the ground
	//			{
	//				land();
	//				getActions().clear();
	//			}
	//			
	//			// if input touch down rotate counter clockwise, otherwise rotate towards ground
	//			if (isUserInputDown)
	//			{
	//				setPose(Pose.RIDE); // TODO set to roll
	//				rotateBy(ROTATION_SPEED*delta);
	//			}
	//			else
	//			{
	//				setPose(Pose.JUMP);
	//				float direction = rotation > 180 ? 1 : -1;
	//				rotateBy(ROTATION_SPEED*0.3f*direction*delta);
	//			}
	//		}

			 accum -= step;
//		}// end: while (accum >= step)
		
		// calculate the interpolation alpha
	    final float alpha = accum / step;

		final float currentX = getX();
		final float currentY = getY();

		final float interpolX = MathUtils.lerp(previousX, currentX, alpha);
		final float interpolY = MathUtils.lerp(previousY, currentY, alpha);

//		setPosition(interpolX, interpolY);
		
		final float newCamSpotX= MathUtils.lerp(MIN_CAM_SPOT_X, MAX_CAM_SPOT_X, getSpeedPercentage());
		final Vector2 newCamSpot = new Vector2(getX() + newCamSpotX, getY());
		if (getSpeed() == 0)
		{
			newCamSpot.y +=5f; // initially when standing, move cam above
		}
		
		// before applying the new camspot, check if the difference is too big and go there smoothly
		final Vector2 diff = newCamSpot.cpy().sub(camSpot);
		diff.clamp(0, getMaxCamSpeed());
		camSpot.add(diff);
		
//		camSpot.set(newCamSpot);
		
		// TODO move snow spray (use listener)
//		final Pool<Vector2> vecPool = Pools.get(Vector2.class);
//		Vector2 corner = vecPool.obtain();
//		corner.set(0,0);
//		this.localToParentCoordinates(corner);
//		snowSpray.setPosition(corner.x, corner.y);
//		vecPool.free(corner);
//		snowSpray.update(delta);
//		if (isInAir)
//			snowSpray.allowCompletion();
//		else
//			snowSpray.start();
			
		// inform listeners about new position
//		System.out.println("Performer is at: "+ getX() + ","+ getY());
		for (PerformerListener listener : listeners) {
			listener.onPositionChange(getX(), getY());
		}

	}
	
	private float getMaxCamSpeed() {
		return state.isStarted() ? (runtime > 1f ? 3.3f : 03.3f) : 0.03f;
	}

	@Override
	public void draw(ModelBatch modelBatch, Environment environment) {
		// TODO Auto-generated method stub
		super.draw(modelBatch, environment);
	}
	@Override
	public void draw(ModelBatch modelBatch, Environment environment, Tag tag) {
		// TODO Auto-generated method stub
		super.draw(modelBatch, environment, tag);
	}

//	@Override
//	public void draw(Batch batch, float parentAlpha) {
//		super.draw(batch, parentAlpha);
//		final GradientColorValue tint = snowSpray.getEmitters().get(0).getTint();
//		final Color amb = ambientColorContainer.getColor();
//		tint.setColors(new float[]{amb.r, amb.g, amb.b});
//		snowSpray.draw(batch);
//	}
	
	public void setPose(Pose pose) {
		this.pose = pose;
		for (Actor3D a : getChildren()) {
			a.setVisible(false);
		}
		poses.get(pose).setVisible(true);
	}
	
	public Pose getPose() {
		return pose;
	}
	
	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		if (!state.isStarted())
			return;
		
		if (speed < MIN_SPEED)
			speed = MIN_SPEED;
		if (speed > MAX_SPEED)
			speed = MAX_SPEED;
		this.speed = speed;
		
		// inform listeners
		for (PerformerListener l : listeners) {
			l.onSpeedChanged(speed, getSpeedPercentage());
		}
	}

	public float getSpeedPercentage() {
		final float max = MAX_SPEED - MIN_SPEED;
		final float cur = speed - MIN_SPEED;
		return cur/max;
	}

	public Vector2 getCamSpot() {
		return camSpot;
	}

	@Override
	public void onAmbientColorChangeTriggered(Color target, float seconds) {
//		ambientColorContainer.addAction(Actions.color(target, seconds));		
//
//		// performer is also emitting ambient light in ambient color
//		target = target.cpy();
//		target.mul(4f); // mul == brighter ==> so the actor doesn't become black but just a bit darker
//		getChild(0).addAction(Actions.color(target, seconds));
	}

	/**
	 * handle the only possible user input (on the game stage): touch anywhere on the screen
	 * @param down touch up or touch down
	 */
	public void userInput(boolean touchDown) {
		if (!state.isStarted()) 
		{
			setState(PlayerState.SLIDING);
			return; // don't jump or rotate if game not started yet
//			setSpeed(MIN_SPEED);
//			getGameManager().setStarted(true);
		}
		
		isUserInputDown = touchDown;
//		if (!state.isInAir())
//		{
//			if (touchDown)
//			{
////				jump(2f);
//			}
//		}
		
	}
	
	@Override
	public float getRotation() {
		float rotation = super.getRotation();
		rotation %= 360f;
		if (rotation <0f)
			rotation = 360 + rotation;
		return rotation;
	}
	
	public void jump(float jumpforce) {
		System.out.println("jump : "+isUserInputDown);
		setState(PlayerState.INAIR);
		getActions().clear();
		addAction(Actions.sequence(
				Actions.moveBy(0, jumpforce, 0, 0.3f, Interpolation.exp5Out),
//				Actions.moveBy(0, jumpforce, 0, 0.3f, Interpolation.circleOut),
				new GravityAction()
//				Actions.gravity()
				));
//		setY(getY()+jumpforce);
//		setPose(Pose.JUMP);
		
//		getBody().applyForceToCenter(100f, 100f, true);
		if (jumpforce > 0f)
		{
////			getBody().applyLinearImpulse(10, jumpforce, 0, 0, true);
//			getBody().setType(BodyType.DynamicBody);
//			Vector2 imp = velocity.cpy();
//			getBody().applyLinearImpulse(imp, new Vector2(), true);
////			imp = imp.angle() < 180f ? imp.rotate(-45f):imp.rotate(45f);
//			imp = imp.rotate(90f).nor();
//			imp.scl(jumpforce);
//			getBody().applyLinearImpulse(imp, new Vector2(), true);
		}
	}
	
	/** touching down after a jump or fall*/
	private void land() {
		System.out.println("land()");
		clearActions();
		setState(PlayerState.DUCKING);
		setPose(Pose.DUCK);
	}
	
	public void setState(PlayerState state) {
		if (this.state.equals(state))
			return;
		timeInState =0f;
		this.state = state.moveTo(state);
	}
	
	public float getTimeInState() {
		return timeInState;
	}

	public void addListener (PerformerListener listener)
	{
		listeners.add(listener);
	}
	
	@Override
	public boolean isCulled(Camera3D cam) {
		return false;
	}
	
	@Override
	public void dispose() {
		Disposables.gracefullyDisposeOf(snowSpray);
	}

	@Override
	public float getWidth() {
		return PERFORMER_WIDTH;
	}
	
	@Override
	public float getHeight() {
		return PERFORMER_WIDTH;
	}


}
