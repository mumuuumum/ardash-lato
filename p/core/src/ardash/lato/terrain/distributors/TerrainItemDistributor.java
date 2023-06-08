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
package ardash.lato.terrain.distributors;

import java.util.SortedMap;
import java.util.TreeMap;

import ardash.lato.terrain.CollidingTerrainItem;
import ardash.lato.terrain.DummyTerrainItem;

public abstract class TerrainItemDistributor {

	static final int AVG_RANGE_SIZE = 500;

	protected abstract void addItem(int i);

	protected abstract int addAFewItems(int from, int to);

	public TerrainItemDistributor() {
		super();
	}

	public void reset() {
		// TODO return items to object pool
		getRangeMap().clear();
	}
	
	public abstract int getCurrMaxX();
	public abstract void setCurrMaxX(int newCurrMaxX);
	protected abstract int getDesiredAmountPer1000m();
	
	/**
	 * Some items cannot oerlap, like stones and coins. So stones an coins share the same range map.
	 * The range map is static. For stones and coines it is the colliderRagemap.
	 * @return The Rage Map for this Type.
	 */
	protected abstract TreeMap<Integer, CollidingTerrainItem> getRangeMap();

	public SortedMap<Integer, CollidingTerrainItem> getItemsInRange(int from, int to) {
		if (to > getCurrMaxX()) {
			generateNewRange();
		}
		return getRangeMap().subMap(from, to);
	}

	private void generateNewRange() {
		final int from = getCurrMaxX();
		final int to = from+AVG_RANGE_SIZE;
		final int rangeSize = to - from;
		setCurrMaxX(to);
		
		final int desiredAmountPer1000m = getDesiredAmountPer1000m();		
		final int desiredAmountForThisRange = desiredAmountPer1000m / Math.min(1, (1000/rangeSize) ) ;
		
		int addedItems = 0;
		
		for (int i = 0 ; i < desiredAmountForThisRange ; i++) {
			final int itemsAddedInThisCycle = addAFewItems(from, to);
			addedItems += itemsAddedInThisCycle;
			
			// stop when we have enough items
			if (addedItems >= desiredAmountForThisRange) {
				break;
			}
		}
	}
	
	/**
	 * Adds a dummy item to fill the place at position i, so no other object can be palced there. Anyway the dummy won't be rendered or colliding.
	 * It is only sitting there to save the spot. Stones for example must have a minimum distance of 1. We can put dummy items next to them, so no other stones will be put there.
	 * @param i the x index
	 */
	protected void addDummyItem(int i) {
		final DummyTerrainItem di = new DummyTerrainItem();
		getRangeMap().put(i, di);
	}

	

}