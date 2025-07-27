package com.diagantika.Actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.tinylog.Logger;

public class CardAnimation {
    private Card card;

    private TextureRegion backCard;
    private TextureRegion frontCard;
    private TextureRegion currentRenderCard;

    private boolean flipStatus = false;// flag ketika animasi akan dijalankan
    private boolean hasSwitched = false; // FLAG UNTUK MENCEGAH MULTIPLE SWITCH

    private float flipTimer = 0f;// timer flip antara 0-1
    private float flipDuration = 0.5f;// durasi animasi pengatur kecepatan flipjumat

    // titik pusat untuk rotasi
    private float originX;
    private float originY;


    private boolean stopDrawing=false;// flag ketika card sudah matches kita stop draw

    public CardAnimation(Card card) {
        this.card = card;
    }



    /**
     * membuat posisi untuk putaran texture ketika
     * di batch akan di rotasikan
     * kita set di tengah texture
     */
    private void originTexture(TextureRegion region){
        originX = region.getRegionWidth() * 0.5f;
        originY = region.getRegionHeight() * 0.5f;
    }

    public void build(TextureRegion frontCard, TextureRegion backCard){
        if (backCard==null || frontCard==null){
            throw new GdxRuntimeException("silahkan set back dan front texture terlebih dahulu dengan");
        }

        // kartu yang akan dirender pertama
        currentRenderCard = backCard;
        this.frontCard = frontCard;
        this.backCard = backCard;

        originTexture(currentRenderCard);
    }

    public void draw(Batch batch) {


        if (!stopDrawing){
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

        flipStatus = true;
        flipTimer = 0f;
        hasSwitched = false; // RESET FLAG SAAT MULAI FLIP BARU
    }

    private float calculateFlipScale() {
        // atur waktu animasi
        // ketika belum disuruh animasi maka kita kembalikan nilai 1f
        // karena kita akan menghitung dari 0 sampai 1 untuk efek flip animasi
        // dengan interpolasi
        if (!flipStatus) return 1.f;

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
        if (!flipStatus) return;

        // 1. UPDATE PROGRESS TIMER
        flipTimer += Gdx.graphics.getDeltaTime() / flipDuration;

        // 2. SWITCH KARTU SEKALI SAAT MENCAPAI 50%
        // ketika sudah 50% kartu berhasil di switch maka
        // ubah gambar ke sisi sebaliknya
        if (flipTimer >= 0.5f && !hasSwitched) {
            switchCardSide();
            hasSwitched = true; // TANDAI SUDAH SWITCH
        }

        // 3. SELESAIKAN ANIMASI
        // apabila sudah mencapai timer maks kita hentikan animasi
        if (flipTimer >= 1.0f) {
            flipTimer = 1.0f;
            flipStatus = false;
        }
    }

    public void stopDrawing(){
        Logger.debug("menghentikan gambar kartu");
        stopDrawing = true;
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
