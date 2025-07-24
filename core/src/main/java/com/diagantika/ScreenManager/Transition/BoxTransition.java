package com.diagantika.ScreenManager.Transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class BoxTransition extends AbstractScreenTransition{
    private int centerX;
    private int centerY;

    public BoxTransition(float duration) {
        super(duration);

        centerX = Gdx.graphics.getWidth()/2;
        centerY = Gdx.graphics.getHeight()/2;
    }

    @Override
    public float getDuration () {
        return duration;
    }

    @Override
    public void render(SpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha) {

        ScreenUtils.clear(Color.CLEAR);

        float percent = transitionTime / duration;

        float width = Gdx.graphics.getWidth() * percent;
        float height = Gdx.graphics.getHeight() * percent;

        batch.begin();
        batch.draw(nextScreen, centerX - width/2, centerY - height/2, width, height);
        batch.end();

        transitionTime += Gdx.graphics.getDeltaTime();
    }
}
