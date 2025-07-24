package com.diagantika;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.diagantika.Screen.SplashScreen;
import com.diagantika.ScreenManager.ApplicationScreen;
import com.diagantika.ScreenManager.Transition.AbstractScreenTransition;
import com.diagantika.ScreenManager.Transition.CircleTransition;
import com.diagantika.ScreenManager.Transition.FadeTransition;
import com.diagantika.Services.ApplicationContext;
import com.diagantika.Services.AssetLoader;
import com.diagantika.Util.Bean;
import com.diagantika.Util.Logger;

import java.util.HashMap;

public class Main extends ApplicationScreen {
    public SpriteBatch batch;
    private ApplicationContext context;
    public HashMap<TRANSITION, AbstractScreenTransition> transitions;
    public Logger<Main> log  ;

    public enum TRANSITION{
        FADE_TRANSITION,
        CIRCLE_TRANSITION
    }

    @Override
    public void create() {
        super.create();

        // ini disini karena lifecycle main class lebih dahulu dari BEan
        log  = new Logger<>(Main.class, Bean.getLogConfigInstance());
        batch = super.batch;

        log.info("memulai setup");

        transitions = new HashMap<>();
        transitions.put(TRANSITION.CIRCLE_TRANSITION,new CircleTransition(1f, Color.BROWN));
        transitions.put(TRANSITION.FADE_TRANSITION,new FadeTransition(1f));

        // load aset
        AssetLoader assetLoader = new AssetLoader();
        assetLoader.load();

        // context
        context = new ApplicationContext(assetLoader.getAssetManager());

        assetLoader = null;

        setScreen(new SplashScreen(),transitions.get(TRANSITION.CIRCLE_TRANSITION));

    }

    @Override
    public void dispose() {
        context.dispose();
        super.dispose();
        log.info("dispose screen");
    }
}
