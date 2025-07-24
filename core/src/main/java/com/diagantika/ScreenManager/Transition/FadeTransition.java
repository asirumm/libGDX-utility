package com.diagantika.ScreenManager.Transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * From
 * https://github.com/andgeno/canyon-bunny
 */
public class FadeTransition extends AbstractScreenTransition{
    private float duration;

    public FadeTransition(float duration) {
        super(duration);
        this.duration = duration;
    }


    @Override
    public float getDuration () {
        return duration;
    }

    @Override
    public void render (SpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha) {

        ScreenUtils.clear(Color.CLEAR);

        float w = currScreen.getWidth();
        float h = currScreen.getHeight();

        alpha = Interpolation.fade.apply(alpha);


        batch.begin();
        batch.setColor(1, 1, 1, 1);
        batch.draw(currScreen, 0, 0, 0, 0, w, h, 1, 1, 0, 0, 0, currScreen.getWidth(), currScreen.getHeight(), false, true);
        batch.setColor(1, 1, 1, alpha);
        batch.draw(nextScreen, 0, 0, 0, 0, w, h, 1, 1, 0, 0, 0, nextScreen.getWidth(), nextScreen.getHeight(), false, true);
        batch.end();
    }

}
