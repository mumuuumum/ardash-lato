package ardash.lato;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.utils.PerformanceCounter;
import com.badlogic.gdx.utils.viewport.Viewport;

import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.gdx.scenes.scene3d.Stage3D;
import ardash.lato.actors.WaveDrawer;
import ardash.lato.actors3.TerrainItem;
import ardash.lato.terrain.Section;
import ardash.lato.terrain.TerrainManager.TerrainListener;
import ardash.lato.weather.SunColorChangeListener;

public class LatoStage3D extends Stage3D implements TerrainListener {

	private static int sc = 0;
    protected PerformanceCounter pcact = Actor3D.getGameManager().performanceCounters.add("s3d act "+sc);
    protected PerformanceCounter pcdra = Actor3D.getGameManager().performanceCounters.add("s3d dra "+sc++);
    
	public LatoStage3D(Viewport v) {
		super(v);
	}

	public LatoStage3D(Viewport v, ShaderProvider shaderProvider) {
		super(v, shaderProvider);
	}

	@Override
	public void onNewSectionCreated(Section s) {
		List<TerrainItem> items = s.getSurroundingItems();
		
		// add the new actors (itmes surrounding the terrain)
		for (TerrainItem a : items) {
			addActor(a);
		}
		
		// and remove the old stuff (use same offset as in wavedrawer)
		float border = getRoot().getGameManager().getGameScreen().performer.getX()-WaveDrawer.PASSED_TERRAIN;
		List<TerrainItem> canBeDeleted = new LinkedList<TerrainItem>();
		for (Actor3D a : getRoot().getChildren()) {
			if (a instanceof TerrainItem) {
				if (a.getX()<border)
					canBeDeleted.add((TerrainItem)a);
			}
		}
		
		for (TerrainItem a : canBeDeleted) {
			a.remove();
		}
		
	}

	private void addActor(TerrainItem a) {
		if (a.getTag() == null)
			throw new RuntimeException("surrounding item must have a tag");
		this.addActor((Actor3D)a);		
	}

	@Override
	public void draw() {
    	pcdra.start();
		super.draw();
        pcdra.stop();
	}
	
	@Override
	public void draw(boolean in3grounds) {
    	pcdra.start();
		super.draw(in3grounds);
        pcdra.stop();
	}
	
	@Override
	public void act(float delta) {
    	pcact.start();
		super.act(delta);
		pcact.stop();
	}

}
