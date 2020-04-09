package ardash.lato.actors;

import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * Similar to MoveByAction, but runs infinitely and has an acceleration.
 * This doe not add a gravity to Actors. It only adds the relative movement that loos like gravity.
 * So this cannot be constantly added to and Actor but rather has the be re-added for each jump.
 *
 */
public class GravityAction extends Action{
	static final float gravity = 9.80665f; // m/s/s
	float vspeed = 0;
	
	@Override
	public void reset() {
		super.reset();
		vspeed = 0;
	}

	@Override
	public boolean act(float delta) {
		vspeed += + gravity*delta;
		target.moveBy (0, -vspeed * delta);
		return false; // infinite (until removed from actor)
	}

}
