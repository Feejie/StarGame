package ru.geekbrains.stargame.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.utils.Regions;

public class Sprite extends Rect {

    protected float angle;
    protected float scale = 1f;
    protected TextureRegion[] regions;
    protected TextureRegion region;
    protected int frame;

    private boolean isDestroyed;

    public Sprite() {
    }

    public Sprite(TextureRegion region) {
        this.regions = new TextureRegion[1];
        this.regions[0] = region;
        this.region = region;
    }

    public Sprite(TextureRegion region, int rows, int cols, int frames) {
        this.regions = Regions.split(region, rows, cols, frames);
    }

    public void draw(SpriteBatch batch) {
        if (regions != null) {
            batch.draw(
                    regions[frame],
                    getLeft(), getBottom(),
                    halfWidth, halfHeight,
                    getWidth(), getHeight(),
                    scale, scale,
                    angle
            );
        } else {
            batch.draw(
                    region,
                    getLeft(), getBottom(),
                    halfWidth, halfHeight,
                    getWidth(), getHeight(),
                    scale, scale,
                    angle
            );
        }
    }

    public void update(float delta) {

    }

    public void resize(Rect worldBounds) {

    }

    public void setHeightProportion(float height) {
        setHeight(height);
        setWidth(height);
    }

    public boolean touchDown(Vector2 touch, int pointer) {
        return false;
    }

    public boolean touchUp(Vector2 touch, int pointer) {
        return false;
    }

    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void flushDestroy() {
        isDestroyed = false;
    }

    public void destroy() {
        isDestroyed = true;
    }

}
