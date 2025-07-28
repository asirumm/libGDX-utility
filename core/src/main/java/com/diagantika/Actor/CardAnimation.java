package com.diagantika.Actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import org.tinylog.Logger;

public class CardAnimation {
    private Card card;

    private TextureRegion backCard;
    private TextureRegion frontCard;
    private TextureRegion currentRenderCard;

    // flag untuk menjalankan atau mematikan animasi flip
    private CardFlag animationFlipStatus = CardFlag.ANIMATION_FLIP_NOT_RUNNING;

    // flag untuk mengetahui apakah texture sudah boleh di ubah saat flip
    private boolean canChangeTexture = false;

    private float flipTimer = 0f;// timer flip antara 0-1
    private final float MAX_FLIP_TIMER = 1f;
    private float flipDuration = 0.5f;// durasi animasi pengatur kecepatan flipjumat

    // titik pusat untuk rotasi
    private float originX;
    private float originY;

    // flag ketika card sudah matches kita stop draw
    private CardFlag drawingStatus = CardFlag.CARD_ANIMATION_ACTIVE;

    public CardAnimation(Card card) {
        this.card = card;
    }

    public CardFlag getAnimationFlipStatus() {
        return animationFlipStatus;
    }

    /**
     * membuat posisi untuk putaran texture ketika
     * di batch akan di rotasikan
     * kita set di tengah texture
     */
    private void setOriginRotationTexture(TextureRegion region){
        originX = region.getRegionWidth() * 0.5f;
        originY = region.getRegionHeight() * 0.5f;
    }

    public void build(TextureRegion frontCard, TextureRegion backCard){

        // kartu yang akan dirender pertama
        currentRenderCard = backCard;
        this.frontCard = frontCard;
        this.backCard = backCard;

        setOriginRotationTexture(currentRenderCard);
    }

    public void draw(Batch batch) {


        // apabila masih belum ditemukan pasangan kita terus menggambar
        if (drawingStatus==CardFlag.CARD_ANIMATION_ACTIVE){
            updateFlipAnimation();
            drawCard(batch);
        }
        // todo efek destroy card

    }

    /**
     * panggil ini ketika listener klik
     */
    public void startAnimation(){
        Logger.debug("animasi flip dijalankan");

        // persiapan nilai untuk run animasi
        animationFlipStatus = CardFlag.ANIMATION_FLIP_RUNNING;
        // kita memulai dari 0 - 1
        flipTimer = 0f;
        // texture belum boleh diganti
        canChangeTexture = false;
    }

    private float calculateFlipScale() {
        // atur waktu animasi
        // ketika belum disuruh animasi maka kita kembalikan nilai 1f
        // karena kita akan menghitung dari 0 sampai 1 untuk efek flip animasi
        // d2engan interpolasi
        if (animationFlipStatus==CardFlag.ANIMATION_FLIP_NOT_RUNNING)
            return MAX_FLIP_TIMER;

        float scaleX;

        if (flipTimer <= 0.5f) {
            // FASE 1: SHRINKING (1.0 → 0.0)
            float shrinkProgress = flipTimer * 2.0f;
            scaleX = 1.0f - shrinkProgress;
            scaleX = Interpolation.pow2Out.apply(scaleX);

        } else {
            // FASE 2: EXPANDING (0.0 → 1.0)
            float expandProgress = (flipTimer - 0.5f) * 2.0f;
            scaleX = expandProgress;
            scaleX = Interpolation.pow2In.apply(scaleX);
        }

        return scaleX;
    }

    private void drawCard(Batch batch) {
        // HITUNG SCALE X BERDASARKAN PROGRESS
        float scaleX = calculateFlipScale();

        float widthTexture = currentRenderCard.getRegionWidth();
        float heightTexture = currentRenderCard.getRegionHeight();

        // RENDER KARTU DENGAN TRANSFORMASI
        // sederhananya kita shrink atau kecilkan gambar pada bagian X
        // maka kita hitung scaleX
        // orx dan y adalah titik pusat melakukan transform gambar
        batch.draw(currentRenderCard,
            card.getX(), card.getY(),           // posisi render
            originX, originY,           // origin point (center)
            widthTexture,heightTexture,  // ukuran
            scaleX, 1.0f,       // scale (hanya X yang berubah)
            0f);                // rotasi gambar adalah tegak lurus

    }

    /**
     * ALGORITMA UTAMA: Update animasi flip card
     */
    private void updateFlipAnimation() {
        // apabila belum disuruh menjalankan animasi
        if (animationFlipStatus==CardFlag.ANIMATION_FLIP_NOT_RUNNING) return;

        // 1. UPDATE PROGRESS TIMER
        flipTimer += Gdx.graphics.getDeltaTime() / flipDuration;

        // 2. SWITCH KARTU SEKALI SAAT MENCAPAI 50%
        // ketika sudah 50% kartu berhasil di switch maka
        // ubah gambar ke sisi sebaliknya
        if (flipTimer >= 0.5f && !canChangeTexture) {
            switchCardSide();
            canChangeTexture = true; // TANDAI SUDAH SWITCH
        }

        // 3. SELESAIKAN ANIMASI
        // apabila sudah mencapai timer maks kita hentikan animasi
        if (flipTimer >= 1.0f) {
            flipTimer = 1.0f;
            animationFlipStatus = CardFlag.ANIMATION_FLIP_NOT_RUNNING;
        }
    }

    public void stopDrawing(){
        Logger.debug("menghentikan gambar kartu");
        drawingStatus = CardFlag.CARD_ANIMATION_INACTIVE;
    }

    /**
     * Ganti sisi kartu di tengah animasi
     */
    private void switchCardSide() {
        if (currentRenderCard==frontCard){
            currentRenderCard = backCard;
        }else if (currentRenderCard==backCard){
            currentRenderCard= frontCard;
        }
    }
}
