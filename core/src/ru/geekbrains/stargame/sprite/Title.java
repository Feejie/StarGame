package ru.geekbrains.stargame.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.stargame.base.Sprite;
import ru.geekbrains.stargame.math.Rect;

public class Title extends Sprite {

    public Title(TextureAtlas atlas) {
        super(atlas.findRegion("title"));
    }

    @Override
    public void resize(Rect wordBounds) {
        setTop(wordBounds.getTop() - 0.12f);
        setHeightProportion(0.45f);
    }
}
