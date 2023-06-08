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

import ardash.lato.actors3.Coin;
import ardash.lato.terrain.CollidingTerrainItem;

public abstract class TerrainItemDistributor {

	static final int AVG_RANGE_SIZE = 500;

	protected abstract void addCoin(int i);

	protected abstract int addAFewCoins(int from, int to);

	protected TreeMap<Integer, CollidingTerrainItem> rangeMap = new TreeMap<>();

	public TerrainItemDistributor() {
		super();
	}

	public void reset() {
		// TODO return items to object pool
		rangeMap.clear();
	}
	
	public abstract int getCurrMaxX();
	public abstract void setCurrMaxX(int newCurrMaxX);

	public SortedMap<Integer, CollidingTerrainItem> getCoinsInRange(int from, int to) {
		if (to > getCurrMaxX()) {
			generateNewRange();
		}
		return rangeMap.subMap(from, to);
	}

	private void generateNewRange() {
		final int from = getCurrMaxX();
		final int to = from+AVG_RANGE_SIZE;
		final int rangeSize = to - from;
		setCurrMaxX(to);
		
		final int desiredAmountPer1000m = 50;		
		final int desiredAmountForThisRange = desiredAmountPer1000m / Math.min(1, (1000/rangeSize) ) ;
		
		int addedCoins = 0;
		
		for (int i = 0 ; i < desiredAmountForThisRange ; i++) {
			int coinsAddedInThisCycle = addAFewCoins(from, to);
			addedCoins += coinsAddedInThisCycle;
			
			// stop when we have enough items
			if (addedCoins >= desiredAmountForThisRange) {
				break;
			}
		}
	}

}