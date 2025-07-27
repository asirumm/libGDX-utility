package com.diagantika.Actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;


/**
 * Kode program ini merupakan hasil dari dukungan claude dan chatGPT
 * sebenarnya kode mereka buruk (chatGPT < claude),
 * jadi saya perlu melakukan perubahan sendiri
 * dan meminta mereka memperbaiki apa yang saya kerjakan
 */
public class Cardx extends Actor {
    private TextureRegion front;
    private TextureRegion back;
    private TextureRegion current;// texture saat ini yang di render
    private TextureRegion icon;// texture saat ini yang di render

    private float flipTimer = 0f;// timer flip antara 0-1
    private float flipDuration = 0.5f;// durasi animasi pengatur kecepatan flipjumat


    private boolean flipStatus = false;// flag ketika animasi akan dijalankan
    private boolean hasSwitched = false; // FLAG UNTUK MENCEGAH MULTIPLE SWITCH

    // titik pusat untuk rotasi
    private float orX;
    private float orY;

    public Cardx(TextureAtlas atlas) {
        super();

        front = atlas.findRegion("card-front");
        back = atlas.findRegion("card-back");
        icon = atlas.findRegion("attack-ico");

        current = front;

        // HITUNG POSISI UNTUK CENTER ORIGIN
        orX = current.getRegionWidth() * 0.5f;
        orY = current.getRegionHeight() * 0.5f;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        updateFlipAnimation();

        // apabila space di klik maka animasi berjalan
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            flipStatus = true;
            flipTimer = 0f;
            hasSwitched = false; // RESET FLAG SAAT MULAI FLIP BARU
        }

        drawCard(batch);
    }

    private void drawCard(Batch batch) {
        // HITUNG SCALE X BERDASARKAN PROGRESS
        float scaleX = calculateFlipScale();

        // RENDER KARTU DENGAN TRANSFORMASI
        // sederhananya kita shrink atau kecilkan gambar pada bagian X
        // maka kita hitung scaleX
        // orx dan y adalah titik pusat melakukan transform gambar
        batch.draw(current,
            100, 100,           // posisi render
            orX, orY,           // origin point (center)
            current.getRegionWidth(), current.getRegionHeight(),  // ukuran
            scaleX, 1.0f,       // scale (hanya X yang berubah)
            0f);                // rotasi gambar adalah tegak lurus

        if(current==front){
            batch.draw(
                icon,current.getRegionX()+((float)current.getRegionWidth()/2)-10, current.getRegionY()+(current.getRegionHeight()+icon.getRegionHeight()/2),           // posisi render
                orX, orY,           // origin point (center)
                icon.getRegionWidth(), icon.getRegionHeight(),  // ukuran
                scaleX, 1.0f,       // scale (hanya X yang berubah)
                0f);
        }
    }

    private float calculateFlipScale() {
        // atur waktu animasi
        // ketika belum disuruh animasi maka kita kembalikan nilai 1f
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

    /**
     * Ganti sisi kartu di tengah animasi
     */
    private void switchCardSide() {
       if (current==front){
           current = back;
       }else if (current==back){
           current= front;
       }

    }
}

