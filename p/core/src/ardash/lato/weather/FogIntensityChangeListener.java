package ardash.lato.weather;

public interface FogIntensityChangeListener {
	
	/**
	 * Callback for the change of fog intensity.
	 */
	void onFogIntensityChanged(float newIntensity, final float duration);

}
