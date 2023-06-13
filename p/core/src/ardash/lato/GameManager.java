/*******************************************************************************
 * Copyright (C) 2020-2023 Andreas Redmer <ar-lato@abga.be>
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

package ardash.lato;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.PerformanceCounters;

import ardash.lato.A.SoundAsset;
import ardash.lato.screens.GameScreen;
import ardash.lato.terrain.TerrainManager;
import ardash.lato.utils.SoundPlayer;
import ardash.lato.weather.EnvColors;
import ardash.lato.weather.SODChangeListener;

public class GameManager implements SODChangeListener {
	
	public static final boolean DEBUG_VIEW = false;
	public static final boolean DEBUG_GUI = false;
	public static final boolean DEBUG_RUNTIME_VALIDATION = false;
	public static final boolean DEBUG_WEATHER_FASTMODE = false;
//	public static final boolean DEBUG_WEATHER_FASTMODE = true;
	public static final boolean DEBUG_ZOOM_OUT_TO_MAX_SPEED = false;
	public static final boolean DEBUG_PRINT_PERFORMANCE_STATS = false;
	public static final boolean DEBUG_PRINT_POOL_STATS = false;
	
	public final LatoGame game;
	public TerrainManager tm;
	public PerformanceCounters performanceCounters = new PerformanceCounters();

	/**
	 * Indicates if forward movement is going on. User must tap initially to start and movement will end after crash.
	 */
	private boolean started;
	private float lastHourOfDay = -1;
	private EnvColors lastKnownColorScheme = EnvColors.DAY;
	private int coinsPickedUpThisRound;

	public GameManager(LatoGame game) {
		this.game = game;
		this.tm = new TerrainManager();
		reset();
	}
	
	public void reset() {
//		tm.reset();
		this.tm = new TerrainManager();
		started = false;
		coinsPickedUpThisRound = 0;
		System.gc();
	}

	public Screen getScreen() {
		return game.getScreen();
	}

	public GameScreen getGameScreen() {
		return (GameScreen)game.getScreen();
	}
	
	public void setStarted(boolean started) {
		this.started = started;
	}
	
	private boolean isStarted()
	{
		return started;
	}

	@Override
	public void onSODChange(float newSOD, float hourOfDay, float delta, float percentOfDayOver, EnvColors currentColorSchema) {
		this.lastHourOfDay = hourOfDay;
		this.lastKnownColorScheme = currentColorSchema;
	}
	
	public float getLastHourOfDay() {
		return lastHourOfDay;
	}

	public void pickUpCoin() {
		SoundPlayer.playSound(A.getSound(SoundAsset.COINDROP));
		coinsPickedUpThisRound++;
	}
	
	public int getCoinsPickedUpThisRound() {
		return coinsPickedUpThisRound;
	}

	public EnvColors getLastKnownColorScheme() {
		return lastKnownColorScheme;
	}

}
