package ardash.lato.actors;

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
	public interface Listener
	{
		
	}
}
