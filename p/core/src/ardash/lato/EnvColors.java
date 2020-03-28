package ardash.lato;

import com.badlogic.gdx.graphics.Color;

public enum EnvColors {
	DAY(new Color(222f/255f, 216f/255f, 201f/255f, 1f), new Color(128f/255f, 175f/255f, 163f/255f, 1f)),

	DUSK(new Color(000/255f, 000/255f, 000/255f, 1f), new Color(000/255f, 000/255f, 000/255f, 1f)),
	
	NIGHT(new Color(000/255f, 000/255f, 000/255f, 1f), new Color(000/255f, 000/255f, 000/255f, 1f)),
	DAWN(new Color(000/255f, 000/255f, 000/255f, 1f), new Color(000/255f, 000/255f, 000/255f, 1f));

	public Color ambient;
	public Color fog;

	private EnvColors(Color ambient, Color fog) {
		this.ambient = ambient;
		this.fog = fog;
	}

}
