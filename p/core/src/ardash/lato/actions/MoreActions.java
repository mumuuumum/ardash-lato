package ardash.lato.actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;

import ardash.lato.actors.GravityAction;

public class MoreActions extends Actions{
	
	static public GravityAction gravity () {
		GravityAction action = action(GravityAction.class);
		return action;
	}
	
	/** Transitions from the color at the time this action starts to the specified color. */
	static public NoAlphaColorAction noAlphaColor (Color color, float duration) {
		return noAlphaColor(color, duration, null);
	}

	/** Transitions from the color at the time this action starts to the specified color. */
	static public NoAlphaColorAction noAlphaColor (Color color, float duration, Interpolation interpolation) {
		NoAlphaColorAction action = action(NoAlphaColorAction.class);
		action.setNoAlphaEndColor(color);
		action.setDuration(duration);
		action.setInterpolation(interpolation);
		return action;
	}

	static public FloatAction floata (float start, float end, float duration) {
		FloatAction action = action(FloatAction.class);
		action.setStart(start);
		action.setEnd(end);
		action.setDuration(duration);
		return action;
	}

}
