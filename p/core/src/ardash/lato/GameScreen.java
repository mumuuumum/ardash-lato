package ardash.lato;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ardash.lato.Assets.SceneTexture;
import ardash.lato.actors.MountainRange;
import net.dermetfan.gdx.assets.AnnotationAssetManager;

public class GameScreen implements Screen {

	public GameManager gm;
	public AnnotationAssetManager am;
	public Assets assets;
	public LatoStage stage;

	public GameScreen(GameManager gm) {
		this.gm = gm;
		this.am = gm.am;
		this.assets = gm.assets;
	}

	@Override
	public void show() {
		Texture ball = am.get(Assets.ball); // Assets.ball is a String
		
		stage = new LatoStage(new FitViewport(640, 480), this);
		stage = new LatoStage(new ExtendViewport(640, 480), this);
		stage.setDebugAll(true);
		Gdx.input.setInputProcessor(stage);
		
//		Image img = new Image(am.get(Assets.ball, Texture.class));
//		Image img = new Image(assets.getSTexture(SceneTexture.MOUNT));
//		img.rotateBy(45f);
//		img.moveBy(100, 0);
//		stage.addActor(img);
		MountainRange mr = new MountainRange(10);
		stage.addActor(mr);
		mr.init();
//		mr.moveBy(400, 0);
		mr.setSpeed(20f);
	}

	@Override
	public void render(float delta) {
		//draw something nice to look at
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
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
