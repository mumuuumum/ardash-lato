package ardash.lato.terrain;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import ardash.lato.actors3.Stone;

/**
 * provides new randomised terrain following different generation-strategies.
 * Supplies, shape of the ground, trees, houses, villages, forests, grind lines, ramps, stones etc.
 * @author z
 *
 */
public class TerrainManager {
	
	public interface TerrainListener {
		void onNewSectionCreated(Section s);
	}
	
	/**
	 * List of all sections of the current terrain. Will be truncated, when a new round starts.
	 * Appends new items but does not delete first items in the beginning, so we an look back of what we have passed in the current round.
	 */
	List<Section> sections = new ArrayList<Section>();
	
	List<TerrainListener> listeners = new ArrayList<TerrainManager.TerrainListener>();
	
	public TerrainManager() {
		reset();
	}

	public void reset() {
		sections.clear();
	}

	public Section getLastSection() {
		if (sections.isEmpty())
			throw new RuntimeException("there are no terrain sections yet");
		return sections.get(sections.size()-1);
	}

	public void createNewSection() {
		System.out.println("NEW SEGMENT");
		System.gc();
		Section s;
		if (sections.isEmpty())
		{
			s = new HomeHill();
			sections.add(s);
		}
		else
		{
			s = new Downer();
			addStoneRandomly((Downer)s);
			if (MathUtils.random(0, 100)<10)
				s = new Hill();
			if (MathUtils.random(0, 100)<20)
				s = new Canyon();
			final Vector2 offset = this.getLastSection().last();
			s.addOffsetToSurroundings(offset);
			s.addOffsetToSegList(offset);
			sections.add(s);
		}
		for (TerrainListener listener : listeners) {
			listener.onNewSectionCreated(s);
		}
	}
	
	private void addStoneRandomly(Downer s) {
		final int r = MathUtils.random(1);
		if (r == 0)
			s.addStone();
	}

	public void addListener(TerrainListener listener) {
		this.listeners.add(listener);
	}
	
	public List<Section> getSections() {
		return sections;
	}

}
