package ardash.lato;

import com.badlogic.gdx.graphics.Color;

public enum EnvColors {
	/**
	 * daytime
	 */
	DAY(new Color(222f/255f, 216f/255f, 201f/255f, 1f), new Color(128f/255f, 175f/255f, 163f/255f, 1f)),

	/**
	 * sunset
	 */
	DUSK(new Color(73/255f, 48/255f, 35/255f, 1f), new Color(170/255f, 116/255f, 77/255f, 1f)),
	
	/**
	 * night time
	 */
	NIGHT(new Color(25/255f, 33/255f, 41/255f, 1f), new Color(43/255f, 56/255f, 69/255f, 1f)),

	/**
	 * sun rise
	 */
	DAWN(new Color(56/255f, 33/255f, 30/255f, 1f), new Color(150/255f, 106/255f, 98/255f, 1f));

	public Color ambient;
	public Color fog;

	private EnvColors(Color ambient, Color fog) {
		this.ambient = ambient;
		this.fog = fog;
	}

}
