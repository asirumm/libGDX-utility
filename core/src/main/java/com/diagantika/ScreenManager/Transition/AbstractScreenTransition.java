package com.diagantika.ScreenManager.Transition;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Untuk setiap kustom transisi anda harus extends kelas ini,
 * karena beberapa screen transisi membutuhkan `transitionTime`
 * dan method itu dipanggil pada ApplicationScreen
 *
 */
public abstract class AbstractScreenTransition implements ScreenTransition{
    protected float transitionTime=0;
    protected float duration=1f;// default

    public  AbstractScreenTransition(float duration){
        this.duration = duration;
    }

    public void resetTransitionTime(){
        transitionTime =0;
    }

    @Override
    public float getDuration() {
        return duration;
    }

    @Override
    public void render (SpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha){


    };

}
