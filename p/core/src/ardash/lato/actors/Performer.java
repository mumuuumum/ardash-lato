package ardash.lato.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ardash.lato.Assets.SceneTexture;

public class Performer extends Group implements StageAccessor {

	private static final float PERFORMER_WIDTH = 1.85f;

	@Override
	public void init() {
		Image img = new Image(getAssets().getSTexture(SceneTexture.PERFORMER));
//		img.rotateBy(45f);
		img.setWidth(PERFORMER_WIDTH);
		img.setHeight(PERFORMER_WIDTH);
		addActor(img);
		
	}

}
