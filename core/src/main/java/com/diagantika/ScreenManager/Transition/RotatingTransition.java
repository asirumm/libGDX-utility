package com.diagantika.ScreenManager.Transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.ScreenUtils;


/**
 * from https://github.com/digital-thinking/libgdx-transitions
 */
public class RotatingTransition extends AbstractScreenTransition {
    private TransitionScaling scaling;
    private Interpolation interpolation;


    public enum TransitionScaling {
        NONE, IN, OUT
    }

    public RotatingTransition(float duration,TransitionScaling scaling,Interpolation interpolation) {
        super(duration);
        this.scaling = scaling;
        this.interpolation = interpolation;
    }

    @Override
    public float getDuration() {
        return duration;
    }

    @Override
    public void render(SpriteBatch batch, Texture currScreen, Texture nextScreen, float alpha) {
        // Ambil lebar dan tinggi dari layar saat ini
        float width = currScreen.getWidth();
        float height = currScreen.getHeight();

        // Hitung persentase transisi berdasarkan waktu yang telah berlalu
        float percent = transitionTime / duration;

        float scalefactor;

        // Tentukan faktor skala berdasarkan jenis transisi (scaling)
        switch (scaling) {
            case IN:
                // Transisi masuk: tidak ada perubahan skala
                scalefactor = 1;
                break;
            case OUT:
                // Transisi keluar: perlahan mengecil
                scalefactor = 1.0f - percent;
                break;
            case NONE:
            default:
                // Tidak ada efek skala
                scalefactor = 1.0f;
                break;
        }

        // Tentukan nilai rotasi (dalam satuan 0..1)
        float rotation = 1;
        if (interpolation != null) {
            // Jika interpolasi disediakan, gunakan untuk menentukan efek rotasi
            rotation = interpolation.apply(percent);
        }

        ScreenUtils.clear(Color.CLEAR);


        // Mulai proses menggambar
        batch.begin();

        // Gambar layar saat ini (currScreen) tanpa skala atau rotasi,
        // dibalik vertikal agar sesuai dengan sistem koordinat libGDX
        batch.draw(
            currScreen,
            0, 0, // posisi gambar
            width / 2, height / 2, // titik pusat transformasi (origin)
            width, height, // ukuran gambar
            1, 1, // skala X dan Y
            0, // rotasi
            0, 0, (int)width, (int)height, // bagian dari tekstur yang digambar
            false, true // tidak dibalik horizontal, dibalik vertikal
        );

        // Gambar layar berikutnya (nextScreen) dengan skala dan rotasi
        batch.draw(
            nextScreen,
            0, 0, // posisi gambar
            width / 2, height / 2, // titik pusat transformasi (origin)
            nextScreen.getWidth(), nextScreen.getHeight(), // ukuran gambar
            scalefactor, scalefactor, // skala X dan Y
            rotation * 360, // rotasi (dalam derajat)
            0, 0, nextScreen.getWidth(), nextScreen.getHeight(), // bagian dari tekstur
            false, true // tidak dibalik horizontal, dibalik vertikal
        );

        // Selesai menggambar
        batch.end();

        // Tambahkan waktu delta untuk memperbarui waktu transisi
        transitionTime += Gdx.graphics.getDeltaTime();
    }

}
