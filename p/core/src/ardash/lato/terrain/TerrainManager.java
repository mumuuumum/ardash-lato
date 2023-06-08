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
import java.util.List;
import java.util.SortedMap;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import ardash.lato.actors3.Coin;
import ardash.lato.terrain.distributors.CoinDistributor;

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
	
	private CoinDistributor cd = new CoinDistributor();
	
	public TerrainManager() {
		reset();
	}

	public void reset() {
		sections.clear();
		cd.reset();
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
			final float offsetX = offset.x;
			
			// the type and size of the new sections is now final
			
			// put some coins on the new section, if there are coins planned for it
			SortedMap<Integer, Coin> plannedCoinsInRange = cd.getCoinsInRange((int)(s.firstX()+offsetX), MathUtils.ceil(s.lastX()+offsetX));
			for (int plannedCoinX : plannedCoinsInRange.keySet()) {
				final Coin coin = plannedCoinsInRange.get(plannedCoinX);
				// the coins are being created with a wider view, so they alreay have the absolute X value correct: now move them back
				System.out.println("add C at X "+ coin.getX());
				System.out.println("moved by X "+ -offsetX);
				coin.moveBy(-offsetX, 0);
				System.out.println("moved to X "+ coin.getX());
				coin.moveBy(0, s.heightAt(coin.getX()));
//				coin.
				s.surroundingItems.add(coin);
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
