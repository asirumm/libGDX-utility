package com.diagantika.Screen;

import com.badlogic.gdx.graphics.Color;
import com.diagantika.ScreenManager.AbstractScreen;

public class FirstScreen extends AbstractScreen {
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    protected Color getColor() {
        return Color.RED;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
