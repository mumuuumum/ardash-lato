package ardash.lato.actions;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import ardash.lato.actors.GravityAction;

public class MoreActions extends Actions{
	
	static public GravityAction gravity () {
		GravityAction action = action(GravityAction.class);
		return action;
	}

}
