package ru.geekbrains.stargame.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.pool.BulletPool;
import ru.geekbrains.stargame.pool.ExplosionPool;

public class Enemy extends Ship {

    private enum State { DESCENT, FIGHT }

    private State state;

    private Vector2 descentV = new Vector2(0, -0.15f);

    private SpaceShip spaceShip;

    public Enemy(BulletPool bulletPool, ExplosionPool explosionPool, Sound bulletSound, Rect worldBounds, SpaceShip spaceShip) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.bulletSound = bulletSound;
        this.worldBounds = worldBounds;
        this.v = new Vector2();
        this.v0 = new Vector2();
        this.bulletV = new Vector2();
        this.spaceShip = spaceShip;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        switch (state) {
            case DESCENT:
                if (getTop() < worldBounds.getTop()) {
                    v.set(v0);
                    state = State.FIGHT;
                }
                break;
            case FIGHT:
                reloadTimer += delta;
                if (reloadTimer > reloadInterval) {
                    reloadTimer -= reloadInterval;
                    shoot();
                }
                if (getBottom() < worldBounds.getBottom()) {
                    destroy();
                    spaceShip.damage(damage);
                }
                break;
        }
    }

    public void set(
            TextureRegion region,
            TextureRegion regionDamage,
            Vector2 v0,
            TextureRegion bulletRegion,
            float bulletHeight,
            float bulletVY,
            int damage,
            float reloadInterval,
            float height,
            int hp
    ) {
        this.region = region;
        this.regionDamage = regionDamage;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0, bulletVY);
        this.damage = damage;
        this.reloadInterval = reloadInterval;
        this.reloadTimer = reloadInterval;
        setHeightProportion(height);
        this.hp = hp;
        this.v.set(descentV);
        this.regionBuff = region;
        this.state = State.DESCENT;
    }

    @Override
    protected void shoot() {
        super.shoot();
        bulletPos = new Vector2(pos.x, pos.y - getHalfHeight());
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, bulletPos, bulletV,
                bulletHeight, worldBounds, damage);
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(
                bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > getTop()
                || bullet.getTop() < pos.y
                );
    }
}
