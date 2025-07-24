package com.diagantika;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.diagantika.Screen.SplashScreen;
import com.diagantika.ScreenManager.ApplicationScreen;
import com.diagantika.ScreenManager.Transition.AbstractScreenTransition;
import com.diagantika.ScreenManager.Transition.CircleTransition;
import com.diagantika.ScreenManager.Transition.FadeTransition;
import com.diagantika.Util.Bean;
import com.diagantika.Util.Logger;
import java.util.HashMap;

public class Main extends ApplicationScreen {
    public SpriteBatch batch;
    public Logger<Main> log ;
    public HashMap<TRANSITION, AbstractScreenTransition> transitions;

    public enum TRANSITION{
        FADE_TRANSITION,
        CIRCLE_TRANSITION
    }

    @Override
    public void create() {
        super.create();

        log =  new Logger<>(Main.class, Bean.getLogConfigInstance());
        batch = super.batch;

        log.info("memulai setup");

        transitions = new HashMap<>();
        transitions.put(TRANSITION.CIRCLE_TRANSITION,new CircleTransition(1f, Color.BROWN));
        transitions.put(TRANSITION.FADE_TRANSITION,new FadeTransition(1f));

        setScreen(new SplashScreen(),transitions.get(TRANSITION.CIRCLE_TRANSITION));

    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
