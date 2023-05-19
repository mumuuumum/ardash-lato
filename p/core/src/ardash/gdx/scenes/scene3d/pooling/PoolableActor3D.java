package ardash.gdx.scenes.scene3d.pooling;

import com.badlogic.gdx.graphics.g3d.Model;

import ardash.gdx.scenes.scene3d.Actor3D;

public class PoolableActor3D extends Actor3D implements Initable{

	boolean initialised = false;

	public PoolableActor3D() {
		super();
	}

	public PoolableActor3D(Model model, float x, float y, float z) {
		super(model, x, y, z);
	}

	public PoolableActor3D(Model model) {
		super(model);
	}

	@Override
	public void reset() {
		initialised = false;
		setPosition(0f, 0f, 0f);
	}

	@Override
	public void init() {
		initialised = true;
	}

	@Override
	public boolean isInitialised() {
		return initialised;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		verify();
	}

}
