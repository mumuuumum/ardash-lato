package ardash.lato;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {

	private GameManager gm;
	private Stage stage;

	public GameScreen(GameManager gm) {
		this.gm = gm;
	}

	@Override
	public void show() {
		Texture ball = gm.am.get(Assets.ball); // Assets.ball is a String
		
		stage = new Stage(new FitViewport(640, 480));
		stage = new Stage(new ExtendViewport(640, 480));
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		//draw something nice to look at
        Gdx.gl.glClearColor(0f, 1f, 1f, 0f);
    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	stage.act(delta);
    	stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		
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
		stage.dispose();
		
	}

}
