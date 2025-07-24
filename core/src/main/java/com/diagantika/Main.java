package com.diagantika;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.diagantika.Screen.SplashScreen;
import com.diagantika.ScreenManager.ApplicationScreen;
import com.diagantika.ScreenManager.Transition.AbstractScreenTransition;
import com.diagantika.ScreenManager.Transition.CircleTransition;
import com.diagantika.ScreenManager.Transition.FadeTransition;
import com.diagantika.Util.Bean;
import com.diagantika.Util.Logger;
import com.diagantika.Util.LoggerConfig;

import java.util.HashMap;

public class Main extends ApplicationScreen {
    public SpriteBatch batch;
    private LoggerConfig loggerConfig;
    public Logger<Main> log = new Logger<>(Main.class, Bean.getLogConfigInstance()) ;
    private HashMap<TRANSITION, AbstractScreenTransition> transitions;

    public enum TRANSITION{
        FADE_TRANSITION,
        CIRCLE_TRANSITION
    }

    @Override
    public void create() {
        batch = super.batch;
        loggerConfig = new LoggerConfig();

        loggerConfig.configure(false, LoggerConfig.LogLevel.DEBUG);

        log.info("memulai setup");

        transitions = new HashMap<>();
        transitions.put(TRANSITION.CIRCLE_TRANSITION,new CircleTransition(1f, Color.BROWN));
        transitions.put(TRANSITION.FADE_TRANSITION,new FadeTransition(1f));

        setScreen(new SplashScreen(),transitions.get(TRANSITION.CIRCLE_TRANSITION));

    }

    public HashMap<TRANSITION, AbstractScreenTransition> getTransitions() {
        return transitions;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
