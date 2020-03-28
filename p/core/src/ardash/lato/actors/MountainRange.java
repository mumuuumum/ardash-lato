package ardash.lato.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Logger;

import ardash.lato.Assets;
import ardash.lato.Assets.SceneTexture;

public class MountainRange extends Group implements StageAccessor{


	/**
	 * speed in units per sec
	 */
	private float speed = 0f;
	
	/**
	 * number of mountains in the mountain range
	 */
	private final int numPieces;
	private final float MOUNT_SIZE = 22f;
	private final float distanceBetweenPieces = 17f;
	private final float VARIANCE = 5f;

	public MountainRange(int numPieces) {
		this.numPieces =numPieces;
	}

	@Override
	public void init() {
		for (int i=0; i< numPieces; i++)
		{
			Image img = new Image(getAssets().getSTexture(SceneTexture.MOUNT_PIX));
			img.rotateBy(45f);
//			img.scaleBy(MOUNT_SIZE);
			img.setScale(MOUNT_SIZE);
			img.moveBy(distanceBetweenPieces*i + MathUtils.random(-VARIANCE, VARIANCE), MathUtils.random(-VARIANCE, VARIANCE));
			addActor(img);
		}
		scaleBy(0.5f,0f);  // stretch all sidewards: long mountains
		
		// add gradiant fog at bottom
		Image img = new Image(getAssets().getSTexture(SceneTexture.MOUNTAINFOG));
		img.setSize(MOUNT_SIZE*numPieces, MOUNT_SIZE*1.4f);
//		img.moveBy(distanceBetweenPieces*i + MathUtils.random(-VARIANCE, VARIANCE), MathUtils.random(-VARIANCE, VARIANCE));
		addActor(img);
//		img.setColor(1f, 0.9f, 0.9f, 1.0f);
//		img.moveBy(0, 1f);
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
//		int i = 0;
		for (int i=0; i< numPieces; i++)
		{
			final Actor child = getChild(i);
			child.moveBy(-speed*delta, 0);
			
			// all mountains will eventually move beyond -0. If that happens too far, send them back to the end.
			if (child.getX() < -child.getWidth()/2f )
			{
				child.moveBy(distanceBetweenPieces*numPieces , 0);
			}
			
		}
		
//		System.out.println(child.getX());
//		System.out.println(child.getWidth());
	}
}
