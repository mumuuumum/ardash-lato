package ardash.lato;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import net.dermetfan.gdx.assets.AnnotationAssetManager;
import net.dermetfan.gdx.assets.AnnotationAssetManager.Asset;

public class Assets {
	public final AnnotationAssetManager manager = new AnnotationAssetManager(new InternalFileHandleResolver());
	
	@Asset(Texture.class)
    public static final String ball = "marble.png";//, player = "img/player.png";
	
	public static final AssetDescriptor<TextureAtlas> uiAtlas = new AssetDescriptor<TextureAtlas>("scene.atlas",
	TextureAtlas.class);

//	public static final AssetDescriptor<Texture> someTexture = new AssetDescriptor<Texture>("images/sometexture.png",
//			Texture.class);
//
//	public static final AssetDescriptor<TextureAtlas> uiAtlas = new AssetDescriptor<TextureAtlas>("ui/uiskin.pack",
//			TextureAtlas.class);
//
//	public static final AssetDescriptor<Skin> uiSkin = new AssetDescriptor<Skin>("ui/uiskin.json", Skin.class,
//			new SkinLoader.SkinParameter("ui/uiskin.pack"));

	public void loadAll() {
		manager.load(Assets.class); // loads all static @Asset fields
//		manager.load(someTexture);
//		manager.load(uiAtlas);
//		manager.load(uiSkin);
	}

	public void dispose() {
		manager.dispose();
	}
}