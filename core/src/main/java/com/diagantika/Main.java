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
import com.diagantika.Services.ApplicationContext;
import com.diagantika.Services.AssetLoader;
import com.diagantika.Util.Bean;
import com.diagantika.Util.Logger;
import com.diagantika.Util.LoggerConfig;

import java.util.HashMap;

public class Main extends ApplicationScreen {
    public SpriteBatch batch;
    private LoggerConfig loggerConfig;
    private ApplicationContext context;
    public HashMap<TRANSITION, AbstractScreenTransition> transitions;
    public Logger<Main> log = new Logger<>(Main.class, Bean.getLogConfigInstance()) ;


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

        // load aset
        AssetLoader assetLoader = new AssetLoader(loggerConfig);
        assetLoader.load();

        // context
        context = new ApplicationContext(assetLoader.getAssetManager(),loggerConfig);

        assetLoader = null;

        setScreen(new SplashScreen(),transitions.get(TRANSITION.CIRCLE_TRANSITION));

    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);


        /// KOSONGKAN SAJA, karena pada parent ada currentScreen.show();
        /// ini bisa menjadi looping tanpa batas
    }

    public ApplicationContext getContext() {
        return context;
    }

    @Override
    public void dispose() {
        context.dispose();
        super.dispose();
        log.info("dispose screen");
    }
}
