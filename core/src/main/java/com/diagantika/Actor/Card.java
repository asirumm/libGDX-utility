package com.diagantika.Actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.diagantika.Util.AssetChecker;
import org.tinylog.Logger;

public class Card extends Actor implements Comparable<Card>{

    private CardAnimation cardAnimation;
    private String cardID;

    private CardFlag matchesStatus = CardFlag.CARD_UNMATCHES;
    // flag stop menggambar untuk card animation
    private CardFlag animationDrawStatus = CardFlag.CARD_ANIMATION_ACTIVE;

    // flag ketika card sudah di klik maka tidak bisa di klik lagi
    // agar tidak spam klik pada kartu yang sama
    private boolean isClicked= false;


    public Card() {
        cardAnimation = new CardAnimation(this);

        setDebug(true);
        setTouchable(Touchable.enabled);

        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // ketika user sudah klik kartu X
                // maka ketika klik ke 2x tidak ada respon
                if (isClicked) return; // langsung tolak klik berikutnya

                // memulai animasi flip
                cardAnimation.startAnimation();

                // menambah instance di comparison
                CardComparisonManager.getInstance().selectedCard(Card.this); // logika lanjutan

                // kunci kartu agar tidak bisa di klik 2x
                // yang menyebabkan animasi flip berulang pada
                // kartu yang sama
                isClicked = true;

            }
        });
    }

    /**
     * status animasi flip jika Running maka
     * animasi sedang berjalan
     */
    public CardFlag statusFlipAnimation() {
        return cardAnimation.getAnimationFlipStatus();
    }

    /**
     * trigger agar animasi dijalankan
     */
    public void triggerFlipForAnimation(){
        cardAnimation.startAnimation();
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public void build(TextureRegion frontCard, TextureRegion backCard, String cardIdentity){

        //cek isi parameter
        AssetChecker.checkAsset(frontCard,"front card");
        AssetChecker.checkAsset(backCard,"back card");

        // atur ukuran gambar
        setSize(frontCard.getRegionWidth(), frontCard.getRegionHeight());

        // identitas kartu
        this.cardID = cardIdentity;

        cardAnimation.build(frontCard,backCard);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        super.draw(batch,parentAlpha);

        // jika belum ditemukan pasangannya maka jangan berhenti gambar
        if (matchesStatus==CardFlag.CARD_UNMATCHES) {
            cardAnimation.draw(batch);

        }
        // ketika status kartu sudah ditemukan pasangannya
        // kita hentikan proses menggambar di animation card
        else if (matchesStatus==CardFlag.CARD_MATCHES){

            if (animationDrawStatus==CardFlag.CARD_ANIMATION_ACTIVE){
                cardAnimation.stopDrawing();
                animationDrawStatus = CardFlag.CARD_ANIMATION_INACTIVE;
            }

        }
    }

    @Override
    public String toString() {
        return
            "frontCardIdentity='" + cardID + '\'';
    }

    @Override
    public int compareTo(Card o) {
        int result = this.cardID.compareTo(o.cardID);

        // apabila true
        if (result == 0) {
            Logger.debug("kartu berpasangan ditemukan card 1 {} dan card 2 {}",
                this.cardID,o.cardID);

            // ganti flag agar dihentikan proses menggambarnya
            this.matchesStatus = CardFlag.CARD_MATCHES;
            o.matchesStatus = CardFlag.CARD_MATCHES;
        }
        return result;
    }
}
