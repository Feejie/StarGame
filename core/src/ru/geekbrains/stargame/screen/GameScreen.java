package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.geekbrains.stargame.base.BaseScreen;
import ru.geekbrains.stargame.base.Font;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletPool;
import ru.geekbrains.stargame.pool.EnemyPool;
import ru.geekbrains.stargame.pool.ExplosionPool;
import ru.geekbrains.stargame.sprite.Background;
import ru.geekbrains.stargame.sprite.Bullet;
import ru.geekbrains.stargame.sprite.ButtonRetry;
import ru.geekbrains.stargame.sprite.Enemy;
import ru.geekbrains.stargame.sprite.MessageGameOver;
import ru.geekbrains.stargame.sprite.SpaceShip;
import ru.geekbrains.stargame.sprite.Star;
import ru.geekbrains.stargame.utils.EnemyGenerator;

public class GameScreen extends BaseScreen {

    public enum State { PLAYING, PAUSE, GAME_OVER, NEW_GAME }

    private static final int STAR_COUNT = 100;
    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";

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

    private State state;

    private EnemyGenerator enemyGenerator;

    private MessageGameOver messageGameOver;
    private ButtonRetry buttonRetry;

    private Font font;
    private int frags;
    private StringBuilder sbFrags;
    private StringBuilder sbHp;
    private StringBuilder sbLevel;

    @Override
    public void show() {
        super.show();
        font = new Font("fonts/light.fnt", "fonts/light.png");
        font.setSize(0.03f);
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
        explosionPool = new ExplosionPool(atlas, explosionSound);
        bulletPool = new BulletPool();
        spaceShip = new SpaceShip(atlas, bulletPool, explosionPool, laserSound, worldBounds);       // worldBounds
        enemyPool = new EnemyPool(bulletPool, explosionPool, rocketSound, worldBounds, spaceShip);
        enemyGenerator = new EnemyGenerator(worldBounds, enemyPool, atlas);
        messageGameOver = new MessageGameOver(atlas);
        buttonRetry = new ButtonRetry(atlas,this);
        frags = 0;
        sbFrags = new StringBuilder();
        sbHp = new StringBuilder();
        sbLevel = new StringBuilder();
        state = State.PLAYING;
    }

    @Override
    public void pause() {
        super.pause();
        if (state == State.GAME_OVER) {
            return;
        }
        state = State.PAUSE;
        music.pause();
    }

    @Override
    public void resume() {
        super.resume();
        if (state == State.GAME_OVER) {
            return;
        }
        state = State.PLAYING;
        music.play();
    }

    @Override
    public void render(float delta) {
        update(delta);
        checkCollisions();
        freeAllDestroyedActiveObjects();
        draw();
    }

    private void update(float delta) {
        for (Star star: starArray) {
            star.update(delta);
        }
        explosionPool.updateActiveSprites(delta);
        if (state == State.NEW_GAME) {
            startNewGame();
        }
        if (state == State.PLAYING) {
            spaceShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            enemyGenerator.generate(delta, frags);
        }
    }

    private void checkCollisions() {
        if (state != State.PLAYING) {
            return;
        }
        List<Enemy> enemyList = enemyPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (Enemy enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            float minDist = enemy.getHalfWidth() + spaceShip.getHalfWidth();
            if (enemy.pos.dst(spaceShip.pos) < minDist) {
                enemy.destroy();
                spaceShip.damage(spaceShip.getHp());
                state = State.GAME_OVER;
            }
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() != spaceShip || bullet.isDestroyed()) {
                    continue;
                }
                if (enemy.isBulletCollision(bullet)) {
                    enemy.damage(bullet.getDamage());
                    if (enemy.isDestroyed()) {
                        frags++;
                    }
                    bullet.destroy();
                }
            }
        }
        for (Bullet bullet : bulletList) {
            if (bullet.getOwner() == spaceShip || bullet.isDestroyed()) {
                continue;
            }
            if (spaceShip.isBulletCollision(bullet)) {
                spaceShip.damage(bullet.getDamage());
                bullet.destroy();
                if (spaceShip.isDestroyed()) {
                    state = State.GAME_OVER;
                }
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
        for (Star star: starArray) {
            star.draw(batch);
        }
        explosionPool.drawActiveSprites(batch);
        if (state == State.PLAYING) {
            spaceShip.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
        } else if (state == State.GAME_OVER) {
            messageGameOver.draw(batch);
            buttonRetry.draw(batch);
        }
        printInfo();
        batch.end();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + 0.003f, worldBounds.getTop() - 0.003f);
        sbHp.setLength(0);
        font.draw(batch, sbHp.append(HP).append(spaceShip.getHp()), worldBounds.pos.x, worldBounds.getTop() - 0.003f, Align.center);
        sbLevel.setLength(0);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyGenerator.getLevel()), worldBounds.getRight() - 0.003f, worldBounds.getTop() - 0.003f, Align.right);
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        spaceShip.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star: starArray) {
            star.resize(worldBounds);
        }
        messageGameOver.resize(worldBounds);
        buttonRetry.resize(worldBounds);
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
        font.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if (state == State.PLAYING) {
            spaceShip.touchDown(touch, pointer);
        } else {
            buttonRetry.touchDown(touch, pointer);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (state == State.PLAYING) {
            spaceShip.touchUp(touch, pointer);
        } else {
            buttonRetry.touchUp(touch, pointer);
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAYING) {
            spaceShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            spaceShip.keyUp(keycode);
        }
        return false;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void startNewGame() {
        bulletPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();

        explosionPool = new ExplosionPool(atlas, explosionSound);
        bulletPool = new BulletPool();
        spaceShip = new SpaceShip(atlas, bulletPool, explosionPool, laserSound, worldBounds);
        spaceShip.resize(worldBounds);
        enemyPool = new EnemyPool(bulletPool, explosionPool, rocketSound, worldBounds, spaceShip);
        enemyGenerator = new EnemyGenerator(worldBounds, enemyPool, atlas);
        state = State.PLAYING;
        frags = 0;
    }
}
