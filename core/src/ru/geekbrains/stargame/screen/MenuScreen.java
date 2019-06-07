package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.BaseScreen;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.sprite.Background;
import ru.geekbrains.stargame.sprite.ButtonExit;
import ru.geekbrains.stargame.sprite.ButtonPlay;
import ru.geekbrains.stargame.sprite.Star;
import ru.geekbrains.stargame.sprite.Title;

public class MenuScreen extends BaseScreen {

    private static final int STAR_COUNT = 100;

    private Game game;

    private Texture bg;
    private Background background;
    private TextureAtlas atlas;

    private Title title;
    private Star[] starArray;
    private ButtonPlay buttonPlay;
    private ButtonExit buttonExit;

    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        this.music = Gdx.audio.newMusic(Gdx.files.internal("sounds/menu.mp3"));
        music.setVolume(0.3f);
        music.play();
        music.setLooping(true);
        atlas = new TextureAtlas("textures/sgStarter.pack");
        bg = new Texture("textures/blackCells2Bg800.jpg");
        background = new Background(new TextureRegion(bg));
        title = new Title(atlas);
        starArray = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++) {
            starArray[i] = new Star(atlas);
        }
        buttonPlay = new ButtonPlay(atlas, game);
        buttonExit = new ButtonExit(atlas);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    private void update(float delta) {
        for (Star star: starArray) {
            star.update(delta);
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star star: starArray) {
            star.draw(batch);
        }
        title.draw(batch);
        buttonPlay.draw(batch);
        buttonExit.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        music.dispose();
        super.dispose();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        title.resize(worldBounds);
        for (Star star: starArray) {
            star.resize(worldBounds);
        }
        buttonPlay.resize(worldBounds);
        buttonExit.resize(worldBounds);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        buttonPlay.touchDown(touch, pointer);
        buttonExit.touchDown(touch, pointer);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        buttonPlay.touchUp(touch, pointer);
        buttonExit.touchUp(touch, pointer);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

}
