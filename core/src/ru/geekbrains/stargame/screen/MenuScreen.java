package ru.geekbrains.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    private SpriteBatch batch;
    private Texture img;
    private Sprite backgroundSprite;
    private Texture ship;
    private Vector2 touch;
    private Vector2 shipMove;
    private Vector2 shipPos;
    private Vector2 cursorPos;

    private int screenWidth;
    private int screenHeight;
    private int shipCenterX;
    private int shipCenterY;

    @Override
    public void show() {
        super.show();
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        img = new Texture("bg800x800.jpg");
        backgroundSprite = new Sprite(img);
        ship = new Texture("starfighter.png");
        touch = new Vector2();
        shipMove = new Vector2();
        shipPos = new Vector2((screenWidth / 2) - (ship.getWidth() / 2), 0);
        cursorPos = new Vector2();

        shipCenterX = ship.getWidth() / 2;
        shipCenterY = ship.getHeight() / 2;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.disableBlending();
        backgroundSprite.draw(batch);
        batch.enableBlending();
        batch.draw(ship, shipPos.x, shipPos.y);
        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && shipPos.x > 0) {
            shipMove.set(-3, 0);
            shipPos.add(shipMove);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && shipPos.x < (screenWidth - ship.getWidth())) {
            shipMove.set(3, 0);
            shipPos.add(shipMove);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && shipPos.y < (screenHeight - ship.getHeight())) {
            shipMove.set(0, 3);
            shipPos.add(shipMove);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && shipPos.y > 0) {
            shipMove.set(0, -3);
            shipPos.add(shipMove);
        }

        if ((int)cursorPos.x != (int)shipPos.x + shipCenterX && (int)cursorPos.y != (int)shipPos.y + shipCenterY) {
            shipPos.add(shipMove);
        }

    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX - shipCenterX, Gdx.graphics.getHeight() - screenY - shipCenterY);
        shipMove.set(touch.sub(shipPos)).nor();

        cursorPos.x = Gdx.input.getX();
        cursorPos.y = Gdx.graphics.getHeight() - Gdx.input.getY();

        return false;
    }


}
