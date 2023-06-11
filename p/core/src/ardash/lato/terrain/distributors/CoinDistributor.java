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

import com.badlogic.gdx.math.MathUtils;

import ardash.lato.terrain.TerrainItemType;

public class CoinDistributor extends ColliderDistributor {
	
	private int currMaxX;
	
	public CoinDistributor() {
		super();
		reset();
	}
	
	@Override
	public void reset() {
		super.reset();
		currMaxX = 100;// don't put anything in the first 100
	}
	
	public int getCurrMaxX() {
		return currMaxX;
	}
	
	public void setCurrMaxX(int currMaxX) {
		this.currMaxX = currMaxX;
	}

	@Override
	protected int addAFewItems(int from, int to) {
		// get a random starting point in this range
		final int start = MathUtils.random(from, to);
		
		//get a random amount of coins
		final int amount = MathUtils.random(1, 6);
		
		// check if there already coins in this range
		SortedMap<Integer, TerrainItemType> existingRange = getItemsInRange(start, start+amount);
		if (! existingRange.isEmpty()) {
			return 0;
		}

		for (int i = 0; i < amount ; i++) {
			addItem (start+i);
		}
		return amount;
	}

	@Override
	protected void addItem(int i) {
//		final Coin coin = Pools.get(Coin.class).obtain();
//		coin.init();
//		coin.setX(i);
		getRangeMap().put(i, TerrainItemType.COIN);
	}

	@Override
	protected int getDesiredAmountPer1000m() {
		return 50;
	}
}
