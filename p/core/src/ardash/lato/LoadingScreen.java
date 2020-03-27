package ardash.lato;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class LoadingScreen implements Screen {
	GameManager gm;

	public LoadingScreen(GameManager gm) {
		this.gm = gm;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
//		Texture ball = gm.am.get(Assets.ball, Texture.class); // Assets.ball is a String
//		Texture ball = gm.am.get(Assets.ball); // Assets.ball is a String
	}

	@Override
	public void render(float delta) {

		// draw something nice to look at
		Gdx.gl.glClearColor(0f, 0f, 1f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (gm.assets.manager.update()) {
			Texture ball = gm.am.get(Assets.ball); // Assets.ball is a String
			// Assets have finished loading, change screen maybe?
			gm.game.setScreen(new GameScreen(gm)); // your screen should take a Assets object in it's constructor.
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
