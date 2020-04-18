package ardash.lato.actions;

import ardash.gdx.scenes.scene3d.actions.Actions3D;

public class Actions extends Actions3D {
	static public GravityAction gravity () {
		GravityAction action = action3d(GravityAction.class);
		return action;
	}

}
