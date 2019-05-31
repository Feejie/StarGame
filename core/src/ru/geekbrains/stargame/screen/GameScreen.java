package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.BaseScreen;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletPool;
import ru.geekbrains.stargame.sprite.Background;
import ru.geekbrains.stargame.sprite.SpaceShip;
import ru.geekbrains.stargame.sprite.Star;

import static ru.geekbrains.stargame.screen.MenuScreen.music;

public class GameScreen extends BaseScreen {
    private static final int STAR_COUNT = 100;

    private TextureAtlas atlas;
    private Texture bg;
    private Background background;

    private Star[] starArray;
    private SpaceShip spaceShip;

    private BulletPool bulletPool;

    @Override
    public void show() {
        super.show();
        atlas = new TextureAtlas("textures/sgStarter.pack");
        bg = new Texture("textures/blackCells2Bg800.jpg");
        background = new Background(new TextureRegion(bg));
        starArray = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++) {
            starArray[i] = new Star(atlas);
        }
        bulletPool = new BulletPool();
        spaceShip = new SpaceShip(atlas, bulletPool);
    }

    @Override
    public void render(float delta) {
        update(delta);
        freeAllDestroyedActiveObjects();
        draw();
    }

    private void update(float delta) {
        spaceShip.update(delta);
        for (Star star: starArray) {
            star.update(delta);
        }
        bulletPool.updateActiveSprites(delta);
    }

    private void freeAllDestroyedActiveObjects() {
        bulletPool.freeAllDestroyedActiveSprites();
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        spaceShip.draw(batch);
        for (Star star: starArray) {
            star.draw(batch);
        }
        bulletPool.drawActiveSprites(batch);
        batch.end();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        spaceShip.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star: starArray) {
            star.resize(worldBounds);
        }
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        music.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        spaceShip.touchDown(touch, pointer);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        spaceShip.touchUp(touch, pointer);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        spaceShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        spaceShip.keyUp(keycode);
        return false;
    }

}
