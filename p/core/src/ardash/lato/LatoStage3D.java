package ardash.lato;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.utils.viewport.Viewport;

import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.gdx.scenes.scene3d.Stage3D;
import ardash.lato.actors.WaveDrawer;
import ardash.lato.actors3.TerrainItem;
import ardash.lato.terrain.Section;
import ardash.lato.terrain.TerrainManager.TerrainListener;

public class LatoStage3D extends Stage3D implements TerrainListener {

	public LatoStage3D(Viewport v) {
		super(v);
	}

	@Override
	public void onNewSectionCreated(Section s) {
		List<Actor3D> items = s.getBackgroundItems();
		
		// add the new actors (itmes surrounding the terrain)
		for (Actor3D a : items) {
			addActor(a);
		}
		
		// and remove the old stuff (use same offset as in wavedrawer)
		float border = getRoot().getGameManager().getGameScreen().performer.getX()-WaveDrawer.PASSED_TERRAIN;
		List<Actor3D> canBeDeleted = new LinkedList<Actor3D>();
		for (Actor3D a : getRoot().getChildren()) {
			if (a instanceof TerrainItem) {
				if (a.getX()<border)
					canBeDeleted.add(a);
			}
		}
		
		for (Actor3D a : canBeDeleted) {
			a.remove();
		}
		
	}

	@Override
	public void draw() {
		super.draw(true);
	}

}
