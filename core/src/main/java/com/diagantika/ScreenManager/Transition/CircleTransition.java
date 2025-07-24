package com.diagantika.ScreenManager.Transition;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class CircleTransition extends AbstractScreenTransition{
    private Color color;
    private ShapeRenderer shapeRenderer;

    public CircleTransition(float duration,Color color) {
        super(duration);
        this.color = color;
        shapeRenderer = new ShapeRenderer();

    }

    @Override
    public float getDuration () {
        return duration;
    }

    @Override
    public void render(SpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha) {
        ScreenUtils.clear(Color.CLEAR);

        float radius = Math.max(currScreen.getWidth(), currScreen.getHeight()) * alpha;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.circle(currScreen.getWidth() / 2f, currScreen.getHeight() / 2f, radius);
        shapeRenderer.end();
    }
}
