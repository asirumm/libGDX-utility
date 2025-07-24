package com.diagantika.Actor;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Null;
import com.diagantika.Animation.AnimationManager;

/**
 * Membuat actor textbutton dengan animasi idle
 * pastikan kalian membuat style di TextButtonAnimationStyle
 *
 *
 * teknik mendapatkan drawable bisa dari skin cara mudah
 * atau dari atlas kalian convert manual, gunakan animationHelper untuk membantu
 *
 */
public class TextButtonAnimation extends Container<Label> {
    private Label label;
    private ClickListener clickListener;
    private TextButtonAnimationStyle style;
    private AnimationManager<Drawable> animationManager;
    private Drawable currentDrawable;// drawable to render in delta time

    private boolean onClick=false;

    /**
     * @param skin untuk mendapatkan nilai default textButton
     * @param animationManager daftar drawable animasi yang akan dijalankan, pastikan currentAnimation sudah ada
     */
    public TextButtonAnimation(@Null String text, Skin skin, AnimationManager<Drawable> animationManager){
        String tempText;

        this.animationManager = animationManager;

        // add value to skin animation button style
        style = skin.get(TextButtonAnimationStyle.class);

        // init label, tak perlu cek text apakah null. Sudah di handle di label
        label = new Label(text,new Label.LabelStyle(style.font,style.fontColor));
        label.setAlignment(Align.center);
        setSize(getPrefWidth(), getPrefHeight());

        // add label to container
        super.setActor(label);

        // set ukuran button
        setSizeButton();

        // set gambar awal drawable
        if (style.up!=null){
            currentDrawable = style.up;
        }else if (animationManager.getFirstTexture() != null){
            currentDrawable = animationManager.getFirstTexture();
        }


        setTouchable(Touchable.enabled);
        setClickListener();
    }

    private void setSizeButton() {
        // size button
        float width = super.getPrefWidth();
        float height = super.getPrefHeight();

        if (style.up != null) width = Math.max(width, style.up.getMinWidth());
        if (style.up != null) height = Math.max(height, style.up.getMinHeight());

        setSize(width,height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        validate();

        super.draw(batch, parentAlpha);

        setBackground(currentDrawable);
    }


    @Override
    public void act(float delta) {
        animationManager.stateTime +=delta;

        if (!onClick) currentDrawable = animationManager.getCurrentFrame(true);
        if (onClick) currentDrawable = style.down;

        super.act(delta);
    }

    private void setClickListener(){
        addListener(this.clickListener = new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isVisualPressed()){
                    onClick = true;
                }
            }
        });
    }

    static public class TextButtonAnimationStyle {
        public BitmapFont font;
        public @Null Drawable up, down;
        public @Null Color fontColor;

        public TextButtonAnimationStyle() {
        }

        public TextButtonAnimationStyle(@Null Drawable up,
                                        @Null Drawable down,
                                        @Null BitmapFont font) {
            this.up = up;
            this.down = down;
            this.font = font;
        }
    }

}
