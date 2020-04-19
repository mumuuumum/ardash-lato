package ardash.lato.terrain;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

/**
 * provides new randomised terrain following different generation-strategies.
 * Supplies, shape of the ground, trees, houses, villages, forrsts, grind lines, ramps etc.
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

	public void createNewSection(Vector2 offsetOfLastSection) {
		Section s = new Downer();
		s.addOffset(offsetOfLastSection);
		sections.add(s);
		for (TerrainListener listener : listeners) {
			listener.onNewSectionCreated(s);
		}
	}
	
	public void addListener(TerrainListener listener) {
		this.listeners.add(listener);
	}
	
	public List<Section> getSections() {
		return sections;
	}

}
