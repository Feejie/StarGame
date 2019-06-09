package ru.geekbrains.stargame.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.stargame.base.ScaledTouchButton;
import ru.geekbrains.stargame.math.Rect;
import ru.geekbrains.stargame.screen.GameScreen;

public class ButtonRetry extends ScaledTouchButton {

    private GameScreen gameScreen;

    public ButtonRetry(TextureAtlas atlas, GameScreen gameScreen) {
        super(atlas.findRegion("btRepeat"));
        this.gameScreen = gameScreen;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(0.17f);
        setBottom(-0.3f);
    }

    @Override
    public void action() {
        gameScreen.setState(GameScreen.State.NEW_GAME);
    }
}
