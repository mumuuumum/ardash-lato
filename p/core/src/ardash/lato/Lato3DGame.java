package ardash.lato;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import ardash.gdx.scenes.scene3d.Actor3D;
import ardash.gdx.scenes.scene3d.Stage3D;

public class Lato3DGame extends Game{
    Stage3D stage3d;
    Stage stage2d;
    CameraInputController camController;
//    private Stage2D stage3dDown;

    Array<Actor3D> up_inhands, up_cards, left_inhands, left_cards, right_inhands, right_cards, down_inhands, down_cards;
    @Override
    public void create() {
        //3dstuff
        stage3d = new Stage3D();
//        stage3dDown = new Stage2d();
//        stage3dDown.getCamera().zoom = 0.017f;
//        stage3dDown.getCamera().update();
//        stage3dDown.enableHit();

        stage2d = new Stage(new StretchViewport(960, 640));

//        camController = new CameraInputController(stage3dDown.getCamera());
        InputMultiplexer im = new InputMultiplexer();
        //im.addProcessor(camController);
        im.addProcessor(stage3d);
//        im.addProcessor(stage3dDown);
        Gdx.input.setInputProcessor(im);
//        stage3d.enableHit();

//        AssetManager am = new AssetManager();
//        am.load("data/g3d/mahjong.g3dj", Model.class);
//        am.load("data/g3d/skydome.g3db", Model.class);
//        am.load("data/g3d/concrete.png", Texture.class);
//        am.finishLoading();

//        Model model = am.get("data/g3d/mahjong.g3dj", Model.class);

        ModelBuilder mb = new ModelBuilder();
        Model model  = mb.createBox(1, 1, 1, new Material(), 1);
        model = mb.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position
    			| Usage.Normal);
//        Model model = am.get("data/g3d/mahjong.g3dj", Model.class);

        up_inhands = new Array<>();
        up_cards = new Array<>();

        down_inhands = new Array<>();
        down_cards = new Array<>();

        left_inhands = new Array<>();
        left_cards = new Array<>();

        right_inhands = new Array<>();
        right_cards = new Array<>();

        Actor3D actor3d = new Actor3D(model);
//        actor3d.setPosition(actor3d.getWidth() * (i - 5), 0, -8);
//        actor3d.setPitch(-90);
        stage3d.addActor(actor3d);
        //up
//        for (int i = 0; i < 13; i++) {
//            Actor3D actor3d = new Actor3D(model, "Dragon_Blank");
//            actor3d.setPosition(actor3d.getWidth() * (i - 5), 0, -8);
//            actor3d.setPitch(-90);
//            stage3d.addActor3D(actor3d);
//            up_inhands.add(actor3d);
//            actor3d.setName("up" + i);
//
//            Actor3D actor3d2 = new Actor3D(model, "Dragon_Blank");
//            actor3d2.setPosition(actor3d.getWidth() * (i - 3), 0, -6f);
//            actor3d2.setPitch(180);
//            stage3d.addActor3d(actor3d2);
//            up_cards.add(actor3d2);
//
//            if (i > 0) {
//            	Actor3D actor3d3 = new Actor3D(model, "Dragon_Blank");
//                actor3d3.setPosition(actor3d.getWidth() * (i - 3), actor3d.getHeight(), -6f);
//                actor3d3.setPitch(180);
//                stage3d.addActor3d(actor3d3);
//            }
//        }

        //right
//        for (int i = 0; i < 13; i++) {
//        	Actor3D actor3d = new Actor3d(model, "Dragon_Blank");
//            actor3d.setPitch(-90);
//            actor3d.setRoll(-90);
//            actor3d.setPosition(9, 0, actor3d.getWidth() * (i - 9));
//            stage3d.addActor3d(actor3d);
//
//            Actor3D actor3d2 = new Actor3D(model, "Dragon_Blank");
//            actor3d2.setPosition(7, 0, actor3d2.getWidth() * (i - 6));
//            actor3d2.setPitch(180);
//            actor3d2.setYaw(-90);
//            stage3d.addActor3d(actor3d2);
//
//            Actor3d actor3d3 = new Actor3D(model, "Dragon_Blank");
//            actor3d3.setPosition(7, actor3d.getHeight(), actor3d3.getWidth() * (i - 6));
//            actor3d3.setPitch(180);
//            actor3d3.setYaw(-90);
//            stage3d.addActor3d(actor3d3);
//        }

        //left
//        for (int i = 0; i < 14; i++) {
//        	Actor3D actor3d = new Actor3D(model, "Dragon_Blank");
//            actor3d.setPitch(-90);
//            actor3d.setRoll(90);
//            if (i < 13) {
//                actor3d.setPosition(-9, 0, actor3d.getWidth() * (i - 11));
//            } else {
//                actor3d.setPosition(-9, 0, actor3d.getWidth() * (i + 0.4f - 11));
//            }
//            stage3d.addActor3d(actor3d);
//        }

        //down
//        for (int i = 0; i < 13; i++) {
//            final Actor3D actor3d = new Actor3D(model, "Dot_" + MathUtils.random(1, 9));
//            actor3d.setPosition(actor3d.getWidth() * (i - 6), 3, 8f);
//            actor3d.setPitch(60);
//            stage3dDown.addActor2d(actor3d);
//            actor3d.addListener(new Event3dListener() {
//                @Override
//                public boolean handle(Event3d event) {
//                    InputEvent3d inputEvent3d = (InputEvent3d) event;
//                    if (inputEvent3d.getType() == InputEvent3d.Type.touchUp) {
//                        LogHelper.log("------------------------>" + actor3d.getName());
//                        actor3d.addAction2d(Actions2d.moveBy(0, 1, 0, 0.2f));
//                    }
//                    return false;
//                }
//            });
//            actor3d.setName("down" + i);
//            //down_inhands.add(actor3d);
//
//            final Actor3D actor3d2 = new Actor3D(model, "Dragon_Blank");
//            actor3d2.setPosition(actor3d2.getWidth() * (i - 5), 0, 3f);
//            actor3d2.setPitch(180);
//            stage3d.addActor3d(actor3d2);
//            actor3d2.addListener(new Event3dListener() {
//                @Override
//                public boolean handle(Event3d event) {
//                    LogHelper.log("---------->" + actor3d2.getName());
//                    return false;
//                }
//            });
//            actor3d2.setName("down" + i);
//            down_cards.add(actor3d2);
//
//            final Actor3d actor3d3 = new Actor3d(model, "Dragon_Blank");
//            actor3d3.setPosition(actor3d2.getWidth() * (i - 5), actor3d2.getHeight(), 3f);
//            actor3d3.setPitch(180);
//            stage3d.addActor3d(actor3d3);
//            actor3d3.addListener(new Event3dListener() {
//                @Override
//                public boolean handle(Event3d event) {
//                    LogHelper.log("---------->" + actor3d3.getName());
//
//                    return false;
//                }
//            });
//            actor3d3.setName("down" + i);
//            down_cards.add(actor3d3);
//        }

        Image image = new Image(new TextureRegion(new Texture("marble.png")));
        image.setSize(stage2d.getWidth(), stage2d.getHeight());
        stage2d.addActor(image);
    }

	

public void drawFog(){
      float fogDensity = 0.3f;
      float fogColour[] = {0.5f, 0.5f, 0.5f, 1f};
//      gl.glEnable(Gdx.gl.fog GL_FOG);
//      gl.glFogf(Gdx.gl.GL_FOG_MODE, Gdx.gl.GL_EXP2);
//      gl.glFogfv(Gdx.gl.GL_FOG_COLOR, fogColour, 0);
//      gl.glFogf(Gdx.gl.GL_FOG_DENSITY, fogDensity);
//      gl.glHint(Gdx.gl.GL_FOG_HINT, gl.GL_NICEST);
//      Gdx.gl.glLoadIdentity();
   }

    @Override
    public void render() {
        super.render();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable (Gdx.gl.GL_DEPTH_TEST);
//        Gdx.gl.gl
//        GL30.gl_f
        stage2d.act();
        stage2d.draw();

        stage3d.act();
        stage3d.getCamera().moveTo(10, 10, 10, 1f);
        stage3d.getCamera().lookAt(0, 0, 0);
        stage3d.getCamera().near = 0.1f;
        stage3d.getCamera().far = 20f;
        stage3d.getCamera().update();
        stage3d.draw();

//        stage3dDown.act();
//        stage3dDown.draw();

//        camController.update();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
//        stage3d.setViewport(width, height, true);
//        stage3d.getCamera().vie
        stage2d.getViewport().update(width, height);
//        stage3dDown.setViewport(width, height, true);
    }
}