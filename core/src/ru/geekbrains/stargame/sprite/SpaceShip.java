package ru.geekbrains.stargame.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.base.Sprite;
import ru.geekbrains.stargame.math.Rect;

public class SpaceShip extends Sprite {

    private static final float LEN = 0.005f;

    private Vector2 v;
    private Vector2 touch;
    private Vector2 buf;

    private Rect worldBounds;

    private boolean isKeyPressed;
    private boolean isTouched;

    public SpaceShip(TextureRegion region) {
        super(region);
        v = new Vector2();
        touch = new Vector2();
        buf = new Vector2();
        isKeyPressed = false;
        isTouched = false;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (isKeyPressed) {
            pos.add(v);
        } else if (isTouched) {
            buf.set(touch);
            if (buf.sub(pos).len() <= LEN) {
                pos.set(touch);
            } else {
                pos.add(v);
            }
        }
        // возврат корабля в границы экрана
        shipReturn();
    }

    @Override
    public void resize(Rect wordBounds) {
        this.worldBounds = wordBounds;
        setHeightProportion(0.1f);
    }

        @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        this.touch = touch;
        if (pos != touch) {
            isTouched = true;
        }
        v.set(touch.cpy().sub(pos)).setLength(LEN);
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        isTouched = false;
        isKeyPressed = true;
        switch (keycode) {
            case 21:
                v.set(-LEN, 0f);
                break;
            case 22:
                v.set(LEN, 0f);
                break;
            case 19:
                v.set(0f, LEN);
                break;
            case 20:
                v.set(0f, -LEN);
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        isKeyPressed = false;
        return false;
    }

    private void shipReturn() {
        if (getRight() < worldBounds.getLeft()) {
            setLeft(worldBounds.getRight());
        }
        if (getLeft() > worldBounds.getRight()) {
            setRight(worldBounds.getLeft());
        }
        if (getTop() < worldBounds.getBottom()) {
            setBottom(worldBounds.getTop());
        }
        if (getBottom() > worldBounds.getTop()) {
            setTop(worldBounds.getBottom());
        }
    }
}
