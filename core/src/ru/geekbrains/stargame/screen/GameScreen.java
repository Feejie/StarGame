package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.BaseScreen;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletPool;
import ru.geekbrains.stargame.pool.EnemyPool;
import ru.geekbrains.stargame.pool.ExplosionPool;
import ru.geekbrains.stargame.sprite.Background;
import ru.geekbrains.stargame.sprite.Explosion;
import ru.geekbrains.stargame.sprite.SpaceShip;
import ru.geekbrains.stargame.sprite.Star;
import ru.geekbrains.stargame.utils.EnemyGenerator;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 100;

    private TextureAtlas atlas;
    private Texture bg;
    private Background background;

    private Star[] starArray;
    private SpaceShip spaceShip;

    private BulletPool bulletPool;
    private ExplosionPool explosionPool;
    private EnemyPool enemyPool;

    private Sound laserSound;
    private Sound explosionSound;
    private Sound rocketSound;

    private EnemyGenerator enemyGenerator;

    private Explosion explosion;

    @Override
    public void show() {
        super.show();
        this.music = Gdx.audio.newMusic(Gdx.files.internal("sounds/game.mp3"));
        music.setVolume(0.3f);
        music.play();
        music.setLooping(true);
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laserShot.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion1.wav"));
        rocketSound = Gdx.audio.newSound(Gdx.files.internal("sounds/rocket1.wav"));
        atlas = new TextureAtlas("textures/sgStarter.pack");
        bg = new Texture("textures/blackCells2Bg800.jpg");
        background = new Background(new TextureRegion(bg));
        starArray = new Star[STAR_COUNT];
        for (int i = 0; i < STAR_COUNT; i++) {
            starArray[i] = new Star(atlas);
        }
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas, explosionSound);
        enemyPool = new EnemyPool(bulletPool, rocketSound, worldBounds);
        spaceShip = new SpaceShip(atlas, bulletPool, laserSound);
        enemyGenerator = new EnemyGenerator(worldBounds, enemyPool, atlas);
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
        explosionPool.updateActiveSprites(delta);
        enemyPool.updateActiveSprites(delta);
        enemyGenerator.generate(delta);

        // взрыв врага при столкновении
        collision();
    }

    private void collision() {
        for (int i = 0; i < enemyPool.getActiveObjects().size(); i++) {
            if (!spaceShip.isOutside(enemyPool.getActiveObjects().get(i))) {
                explosion = explosionPool.obtain();
                explosion.set(enemyPool.getActiveObjects().get(i).getHeight(), enemyPool.getActiveObjects().get(i).pos);            //
                enemyPool.getActiveObjects().remove(enemyPool.getActiveObjects().get(i));
            }
        }
    }

    private void freeAllDestroyedActiveObjects() {
        bulletPool.freeAllDestroyedActiveSprites();
        explosionPool.freeAllDestroyedActiveSprites();
        enemyPool.freeAllDestroyedActiveSprites();
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
        explosionPool.drawActiveSprites(batch);
        enemyPool.drawActiveSprites(batch);
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
        explosionPool.dispose();
        enemyPool.dispose();
        laserSound.dispose();
        explosionSound.dispose();
        rocketSound.dispose();
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
