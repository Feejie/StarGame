package ru.geekbrains.stargame.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.stargame.base.ScaledTouchButton;
import ru.geekbrains.stargame.math.Rect;

public class ButtonExit extends ScaledTouchButton {

    public ButtonExit(TextureAtlas atlas) {
        super(atlas.findRegion("btClose2"));
        setHeightProportion(0.06f);
    }

    @Override
    public void resize(Rect wordBounds) {
        setRight(wordBounds.getRight() - 0.02f);
        setTop(wordBounds.getTop() - 0.02f);
    }

    @Override
    public void action() {
        Gdx.app.exit();
    }
}
