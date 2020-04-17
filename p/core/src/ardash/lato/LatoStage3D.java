package ardash.lato;

import java.util.List;

import com.badlogic.gdx.utils.viewport.Viewport;

import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.gdx.scenes.scene3d.Stage3D;
import ardash.lato.terrain.Section;
import ardash.lato.terrain.TerrainManager.TerrainListener;

public class LatoStage3D extends Stage3D implements TerrainListener {

	public LatoStage3D(Viewport v) {
		super(v);
	}

	@Override
	public void onNewSectionCreated(Section s) {
		List<Actor3D> items = s.getBackgroundItems();
		for (Actor3D a : items) {
			addActor(a);
		}
	}

	@Override
	public void draw() {
		super.draw(true);
	}

}
