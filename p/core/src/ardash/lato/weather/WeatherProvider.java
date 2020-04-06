package ardash.lato.weather;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class WeatherProvider extends Actor{
	public static final float DAYTIME_HOURS = 16f;
	public static final float NIGHT_HOURS = 8f;
	public static final float DAY_HOURS = DAYTIME_HOURS + NIGHT_HOURS;
	public static final float DUSK_HOURS = 0.5f;
	public static final float DAWN_HOURS = 0.5f;
	public static final float SECONDS_PER_DAY = 7f * 60f; // one 24 hours cycle shall have 7 minutes
	public static final float SECONDS_PER_HOUR = SECONDS_PER_DAY / DAY_HOURS;
	public static final float DAYTIME_SECONDS = DAYTIME_HOURS * SECONDS_PER_HOUR;
	public static final float NIGHT_SECONDS = NIGHT_HOURS * SECONDS_PER_HOUR;
	public static final float DUSK_SECONDS = DUSK_HOURS * SECONDS_PER_HOUR;
	public static final float DAWN_SECONDS = DAWN_HOURS * SECONDS_PER_HOUR;

	/**
	 * current Second Of Day. A value from 0 to 24 * SECONDS_PER_HOUR
	 */
	float currentSOD = SECONDS_PER_HOUR * 10.5f; // 10.5 = 10:30 am 
	EnvColors currentColorSchema = EnvColors.DAY;
	
	private LinkedList<AmbientColorChangeListener> ambientColourChangeListeners = new LinkedList<AmbientColorChangeListener>();
	private LinkedList<FogColorChangeListener> fogColourChangeListeners = new LinkedList<FogColorChangeListener>();
	private LinkedList<SkyColorChangeListener> skyColourChangeListeners = new LinkedList<SkyColorChangeListener>();
	private LinkedList<SunColorChangeListener> sunColourChangeListeners = new LinkedList<SunColorChangeListener>();
	
	public WeatherProvider() {
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		currentSOD +=delta;
		if (currentSOD >= SECONDS_PER_DAY)
			currentSOD =0f; // start new day
		if (currentSOD > (SECONDS_PER_HOUR * 10.5f)+ 10f)
		{
//			triggerFogColorChange(EnvColors.DUSK.fog, 5f);
//			triggerAmbientColorChange(EnvColors.DUSK.ambient, 5f);
//			triggerSkyColorChange(EnvColors.DUSK.skyTop, EnvColors.DUSK.skyBottom, 5f);
		}
	}
	
	/**
	 * for keyboard inputs
	 */
	@Override
	public void draw(Batch batch, float parentAlpha) {
		 // input key T
		if (Gdx.input.isKeyJustPressed(Keys.T))
		{
			currentColorSchema = currentColorSchema.next();
			triggerAmbientColorChange(currentColorSchema.ambient, 5f);
			triggerFogColorChange(currentColorSchema.fog, 5f);
			triggerSkyColorChange(currentColorSchema.skyTop, currentColorSchema.skyBottom, 5f);
			triggerSunColorChange(currentColorSchema.sun, 5f);
		}
	}

	private void triggerAmbientColorChange(Color target, float duration) {
		for (AmbientColorChangeListener colorChangeListener : ambientColourChangeListeners) {
			colorChangeListener.onAmbientColorChangeTriggered(target, duration);
		}
	}
	
	private void triggerFogColorChange(Color target, float duration) {
		for (FogColorChangeListener colorChangeListener : fogColourChangeListeners) {
			colorChangeListener.onFogColorChangeTriggered(target, duration);
		}
	}
	
	private void triggerSkyColorChange(Color targetTop, Color targetBottom, float duration) {
		for (SkyColorChangeListener colorChangeListener : skyColourChangeListeners) {
			colorChangeListener.onSkyColorChangeTriggered(targetTop, targetBottom, duration);
		}
	}
	
	private void triggerSunColorChange(Color target, float duration) {
		for (SunColorChangeListener colorChangeListener : sunColourChangeListeners) {
			colorChangeListener.onSunColorChangeTriggered(target, duration);
		}
	}
	
	public void addAmbientColourChangeListener(AmbientColorChangeListener ambientColourChangeListener) {
		this.ambientColourChangeListeners.add(ambientColourChangeListener);
	}
	public void addFogColourChangeListener(FogColorChangeListener fogColourChangeListener) {
		this.fogColourChangeListeners.add(fogColourChangeListener);
	}
	public void addSkyColourChangeListener(SkyColorChangeListener skyColourChangeListener) {
		this.skyColourChangeListeners.add(skyColourChangeListener);
	}
	public void addSunColourChangeListener(SunColorChangeListener sunColourChangeListener) {
		this.sunColourChangeListeners.add(sunColourChangeListener);
	}

	
}
