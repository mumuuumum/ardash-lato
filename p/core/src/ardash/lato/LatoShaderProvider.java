package ardash.lato;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;

public class LatoShaderProvider extends DefaultShaderProvider {
	DefaultShader.Config xshader;
	DefaultShader.Config fogshader;

	public LatoShaderProvider(DefaultShader.Config defaultConfig) {
		super(defaultConfig);
		xshader = new DefaultShader.Config();
		xshader.vertexShader = Gdx.files.internal("shaders/xvertex.glsl").readString();
		xshader.fragmentShader = Gdx.files.internal("shaders/xfragment.glsl").readString();
		fogshader = new DefaultShader.Config();
		fogshader.vertexShader = Gdx.files.internal("shaders/fog.vertex.glsl").readString();
		fogshader.fragmentShader = Gdx.files.internal("shaders/fog.fragment.glsl").readString();
	}
	
	@Override
	protected Shader createShader (Renderable renderable) {
//		if (renderable.material.has(CustomColorTypes.AlbedoColor))
			return new DefaultShader(renderable, fogshader);
//		else
//			return super.createShader(renderable);
	}
}