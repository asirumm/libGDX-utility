package com.diagantika.Screen;

import com.badlogic.gdx.graphics.Color;
import com.diagantika.Main;
import com.diagantika.ScreenManager.AbstractScreen;
import org.tinylog.Logger;

public class SplashScreen extends AbstractScreen {
    @Override
    public void show() {
        Logger.debug("start scereen");

    }

    @Override
    public void render(float delta) {
        super.render(delta);

        // apabila aset telah dimuat semua dan transisi telah usai
        if ( main.isTransitionFinished()){

            main.setScreen(
                new SecondScreen(),
                main.transitions
                    .get(Main.TRANSITION.FADE_TRANSITION));
        }
    }

    @Override
    protected Color getColor() {
        return Color.BROWN;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
