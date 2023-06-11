/*******************************************************************************
 * Copyright (C) 2023 Andreas Redmer <ar-lato@abga.be>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package ardash.lato.terrain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;

import ardash.lato.actors3.Coin;
import ardash.lato.actors3.Stone;
import ardash.lato.actors3.TerrainItem;
import ardash.lato.terrain.distributors.CoinDistributor;
import ardash.lato.terrain.distributors.StoneDistributor;
import ardash.lato.terrain.distributors.TerrainItemDistributor;

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
	
	private TerrainItemDistributor cd = new CoinDistributor();
	private TerrainItemDistributor sd = new StoneDistributor();
	
	public TerrainManager() {
		reset();
	}

	// TODO dont reset, make a new one
	private void reset() {
		for (Section section : sections) {
			for (TerrainItem ti : section.surroundingItems) {
				Pools.free(ti);
			}
		}
		sections.clear();
		listeners.clear();
		cd.reset();
		sd.reset();
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
			if (MathUtils.random(0, 100)<10)
				s = new Hill();
			if (MathUtils.random(0, 100)<20)
				s = new Canyon();
			final Vector2 offset = this.getLastSection().last();
			final float offsetX = offset.x;
			
			// the type and size of the new sections is now final
			
			Set<Integer> itemsToDelete = new HashSet<>();
			// put some coins on the new section, if there are coins planned for it
			{
				SortedMap<Integer, TerrainItemType> plannedCoinsInRange = cd.getItemsInRange((int)(s.firstX()+offsetX), MathUtils.ceil(s.lastX()+offsetX));
				for (int plannedCoinX : plannedCoinsInRange.keySet()) {
					if (plannedCoinsInRange.get(plannedCoinX) != TerrainItemType.COIN) {
						continue;
					}
					itemsToDelete.add(plannedCoinX);
					final Coin cti = Pools.get(Coin.class).obtain();
					cti.init();
					// the CTI are being created with a wider view, so they already have the absolute X value correct: now move them back
					cti.setPosition(plannedCoinX, 0.7f);
					cti.moveBy(-offsetX, 0);
					cti.moveBy(0, s.heightAt(cti.getX()));
					s.surroundingItems.add(cti);
				}
			}
			
			// put some stones on the new section, if there are stones planned for it
			{
				SortedMap<Integer, TerrainItemType> plannedStonesInRange = sd.getItemsInRange((int)(s.firstX()+offsetX), MathUtils.ceil(s.lastX()+offsetX));
				for (int plannedStoneX : plannedStonesInRange.keySet()) {
					if (plannedStonesInRange.get(plannedStoneX) != TerrainItemType.STONE) {
						continue;
					}
					itemsToDelete.add(plannedStoneX);
					final Stone cti = Pools.get(Stone.class).obtain();
//					cti.init(); // TODO
					// the CTI are being created with a wider view, so they already have the absolute X value correct: now move them back
					cti.setPosition(plannedStoneX, -0.5f);
					cti.moveBy(-offsetX, 0);
					cti.moveBy(0, s.heightAt(cti.getX()));
					s.surroundingItems.add(cti);
				}				
				for (Integer integer : itemsToDelete) {
					plannedStonesInRange.remove(integer);
				}
			}
			
			s.addOffsetToSurroundings(offset);
			s.addOffsetToSegList(offset);
			sections.add(s);

		
		}
		
		
		for (TerrainListener listener : listeners) {
			listener.onNewSectionCreated(s);
			s.validate();
		}
	}
	
	public void addListener(TerrainListener listener) {
		this.listeners.add(listener);
	}
	
	public List<Section> getSections() {
		return sections;
	}

}
