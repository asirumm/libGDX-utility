package com.diagantika.ScreenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;
import com.diagantika.Constant;
import com.diagantika.Main;
import com.diagantika.Services.ApplicationContext;
import com.diagantika.Util.Bean;
import com.diagantika.Util.Logger;


import java.util.Stack;

/**
 * Use this class for all screen you have
 */
public abstract class AbstractScreen implements Screen {
    protected Logger<? extends AbstractScreen> logger;
    protected final int VIRTUAL_UI_WIDTH = 640;
    protected final int VIRTUAL_UI_HEIGHT = 320;
    protected SpriteBatch batch;
    protected ApplicationContext context;

    protected Stage stageUI;
    protected Main main;

    // UI bisa menggunakan screen viewport atau extends
    public AbstractScreen() {

        main = (Main) Gdx.app.getApplicationListener();
        logger = new Logger<>(this.getClass(), Bean.getLogConfigInstance());

        context = main.context;
        batch = main.batch;

        logger.info("start screen");


        stageUI = new Stage(new ExtendViewport(VIRTUAL_UI_WIDTH,VIRTUAL_UI_HEIGHT));

        Gdx.input.setInputProcessor(stageUI);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(getColor());

        renderStageUI(delta);
    }

    protected void renderStageUI(float delta){
        stageUI.act(delta);
        stageUI.draw();
    }

    @Override
    public void hide() {

    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(stageUI);
    }

    @Override
    // saat user menekan home atau aplikasi di latar belakang
    public void pause() {

    }

    @Override
    public void resize(int width, int height) {
        stageUI.getViewport().update(width,height,true);
    }

    protected Color getColor() {
        return Color.WHITE;
    }

    @Override
    public void dispose() {
        stageUI.dispose();
        logger.info("dispose");
    }
}
