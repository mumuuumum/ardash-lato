package ardash.lato.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ardash.lato.LatoStage;
import ardash.lato.Assets.SceneTexture;

public class Performer extends Group implements StageAccessor {

	private static final float PERFORMER_WIDTH = 1.85f;
	private float speed = 1f; // speed in m/s

	@Override
	public void init() {
		Image img = new Image(getAssets().getSTexture(SceneTexture.PERFORMER));
//		img.rotateBy(45f);
		img.setWidth(PERFORMER_WIDTH);
		img.setHeight(PERFORMER_WIDTH);
		addActor(img);
		
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
//		moveBy(0, -0.01f); // gravity
		moveBy(speed*delta, 0); // speed
		float heightUnderActor = ((LatoStage)getStage()).getWaveDrawer().getHeightAt(getX()+(PERFORMER_WIDTH/2f));
		float heightOfMe = getY();
		moveBy(0, - (getY() - heightUnderActor));
	}

}
