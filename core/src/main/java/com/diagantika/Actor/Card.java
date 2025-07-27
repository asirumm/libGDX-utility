package com.diagantika.Actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.tinylog.Logger;

public class Card extends Actor implements Comparable<Card>{

    private CardAnimation cardAnimation;
    private String cardIdentity;

    private boolean matches    = false;
    // flag stop menggambar untuk card animation
    private boolean hasStoppedDrawing = false;

    private boolean isClicked= false;


    public Card() {
        cardAnimation = new CardAnimation(this);

        setDebug(true);
        setTouchable(Touchable.enabled);

        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isClicked) return; // langsung tolak klik berikutnya

                cardAnimation.startAnimation(); // animasi dimulai
                CardComparisonManager.getInstance().selectedCard(Card.this); // logika lanjutan
                isClicked = true; // kunci sebelum animasi

            }
        });
    }

    public void triggerFlipForAnimation(){
        cardAnimation.startAnimation();
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public void build(TextureRegion frontCard, TextureRegion backCard, String cardIdentity){
        if (frontCard==null){
            throw new GdxRuntimeException("front card masih null");
        } else if (backCard==null) {
            throw new GdxRuntimeException("back card masih null");
        }

        setSize(frontCard.getRegionWidth(), frontCard.getRegionHeight());

        this.cardIdentity = cardIdentity;

        cardAnimation.build(frontCard,backCard);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        super.draw(batch,parentAlpha);

        // jika belum ditemukan matches nya maka jangan berhenti gambar
        if (!matches) {
            cardAnimation.draw(batch);
        } else {
            // stop drawing flag dibuat agar kita tidak selalu memanggil
            // method stopDrawing

            if (!hasStoppedDrawing) {
                cardAnimation.stopDrawing();
                hasStoppedDrawing = true; // hanya sekali!
            }
        }
    }

    public String getCardIdentity() {
        return cardIdentity;
    }

    @Override
    public String toString() {
        return
            "frontCardIdentity='" + cardIdentity + '\'';
    }

    @Override
    public int compareTo(Card o) {
        int result = this.cardIdentity.compareTo(o.cardIdentity);

        if (result == 0) {
            Logger.debug("kartu berpasangan ditemukan card 1 {} dan card 2 {}",
                this.cardIdentity,o.cardIdentity);

            this.matches = true;
            o.matches = true;
        }
        return result;
    }

    public CardAnimation getCardAnimation() {
        return cardAnimation;
    }
}
