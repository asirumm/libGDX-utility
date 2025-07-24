package com.diagantika.Util;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

public class AnimationHelper {
    private Logger<AnimationHelper> logger = new Logger<>(AnimationHelper.class,Bean.getLogConfigInstance());

    public AnimationHelper() {
    }

    /**
     * Membuat daftar frame animasi dari sebuah {@link TextureAtlas} berdasarkan prefix nama region.
     * <p>
     * Fungsi ini akan mencari region pada atlas dengan format penamaan berurutan seperti:
     * <pre>
     *     prefix1, prefix2, prefix3, ...
     * </pre>
     * Jika tidak ditemukan frame berurutan, fungsi akan mencoba mencari satu region dengan nama persis seperti prefix.
     * <p>
     * Contoh penggunaan:
     * <pre>
     *     Array&lt;TextureRegion&gt; frames = createAnimationFromAtlas("run", atlas);
     *     Animation&lt;TextureRegion&gt; runAnimation = new Animation(0.1f, frames);
     * </pre>
     *
     * @param prefix prefix nama frame yang digunakan dalam atlas (misalnya "run", maka akan dicari "run1", "run2", dst).
     * @param atlas objek {@link TextureAtlas} yang menyimpan semua region gambar.
     * @return {@link Array} dari {@link TextureRegion} yang bisa digunakan untuk animasi.
     * @throws RuntimeException jika tidak ada frame ditemukan dengan prefix yang diberikan.
     */
    public Array<TextureRegion> getAnimationTextureRegion(String prefix, TextureAtlas atlas) {
        Array<TextureRegion> animationFrames = new Array<>();

        // Coba cari frame dengan index (prefix1, prefix2, ...)
        for (int i = 1; ; i++) {
            TextureRegion region = atlas.findRegion(prefix + i);
            if (region == null) {
                // Jika tidak ditemukan apapun sejak awal (i == 1), coba cari prefix saja (tanpa angka)
                if (i == 1) {
                    TextureRegion singleRegion = atlas.findRegion(prefix);
                    if (singleRegion != null) {

                        logger.debug("menambahkan image %s tanpa index number",prefix);

                        animationFrames.add(singleRegion);
                    }
                }
                break;
            }

            logger.debug("menambahkan image %s ",prefix+i);

            animationFrames.add(region);
        }

        // Validasi hasil
        if (animationFrames.isEmpty()) {
            throw new RuntimeException("Gagal memuat animasi dari atlas. Periksa kembali nama prefix pada atlas.");
        }

        return animationFrames;
    }

    /**
     * Untuk animasi scene2d
     * @param size jumlah animasi frame
     */
    public Array<Drawable> createAnimationDrawable(String prefix, Skin skin, byte size) {
        Array<Drawable> animationFrames = new Array<>();

        for (int i = 1; i<= size; i++) {
            String name = prefix + i;

            Drawable drawable = skin.getDrawable(name);

            if (drawable ==null){
                break;
            }

            logger.debug("Menambahkan image: %s", name);

            animationFrames.add(drawable);
        }

        return animationFrames;
    }
}
