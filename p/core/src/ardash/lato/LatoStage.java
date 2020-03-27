package ardash.lato;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.dermetfan.gdx.assets.AnnotationAssetManager;

public class LatoStage extends Stage {
	
	public final GameScreen screen;
//	public final LatoGame game;
	public final Assets assets;
	public final AnnotationAssetManager am;
	public final GameManager gm;

	public LatoStage(Viewport vp, GameScreen gameScreen) {
		super(vp);
		this.screen= gameScreen;
//		this.game = screen.;
		this.assets = screen.assets;
		this.am = screen.am;
		this.gm = screen.gm;
	}
}
