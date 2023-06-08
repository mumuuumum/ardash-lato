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

import com.badlogic.gdx.math.MathUtils;

import ardash.lato.actors3.Coin;

public class CoinDistributor {
	
	static final int AVG_RANGE_SIZE=500;
	TreeMap<Integer, Coin> rangeMap = new TreeMap<>();
	private int currMaxX;
	
	public CoinDistributor() {
		reset();
	}

	public void reset() {
		// TODO return coins to object pool
		rangeMap.clear();
		currMaxX = 0;
	}

	
	public SortedMap<Integer, Coin> getCoinsInRange(int from, int to) {
		if (to > currMaxX) {
			generateNewRange();
		}
		return rangeMap.subMap(from, to);
	}

	private void generateNewRange() {
		final int from = currMaxX;
		final int to = from+AVG_RANGE_SIZE;
		final int rangeSize = to - from;
		currMaxX = to;
		
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

	private int addAFewCoins(int from, int to) {
		// get a random starting point in this range
		final int start = MathUtils.random(from, to);
		
		//get a random amount of coins
		final int amount = MathUtils.random(1, 6);
		
		// check if there already coins in this range
		SortedMap<Integer, Coin> existingRange = getCoinsInRange(start, start+amount);
		if (! existingRange.isEmpty()) {
			return 0;
		}

		for (int i = 0; i < amount ; i++) {
			addCoin (start+i);
		}
		return amount;
	}

	private void addCoin(int i) {
		final Coin coin = new Coin();
		coin.setX(i);
		rangeMap.put(i, coin);
	}

}
