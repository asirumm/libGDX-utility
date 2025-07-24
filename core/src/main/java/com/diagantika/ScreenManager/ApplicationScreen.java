package com.diagantika.ScreenManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.diagantika.ScreenManager.Transition.AbstractScreenTransition;
import com.diagantika.ScreenManager.Transition.ScreenTransition;

/**
 * Kelas ini kustom untuk ApplicationListener
 * ApplicationListener adalah class lifecycle pada libgdx
 *
 * Kelas ini mirip seperti class Game atau ApplicationAdapter.
 * Tujuan utama kelas ini memberikan efek transisi.
 */
public abstract class ApplicationScreen implements ApplicationListener {
    private boolean init;
    private AbstractScreen currScreen;
    private AbstractScreen nextScreen;

    // FBO (Frame Buffer Object) adalah off-screen rendering target.
    //Artinya: kita menggambar ke dalam memori terlebih dahulu (bukan langsung ke layar),
    //lalu hasilnya bisa digunakan sebagai tekstur atau diproses lagi.
    private FrameBuffer currFbo;
    private FrameBuffer nextFbo;
    protected SpriteBatch batch;

    private float t;
    private ScreenTransition screenTransition;
    private boolean isTransitionFinished=false;

    @Override
    public void create() {
        batch = new SpriteBatch();
    }

    // digunakan untuk pindah screen
    public void setScreen (AbstractScreen screen) {
        setScreen(screen, null);
    }

    public void setScreen (AbstractScreen screen, AbstractScreenTransition screenTransition) {
        screenTransition.resetTransitionTime();

        // Ambil lebar dan tinggi layar saat ini
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        // Jika belum diinisialisasi, siapkan FrameBuffer dan SpriteBatch
        if (!init) {
            currFbo = new FrameBuffer(Pixmap.Format.RGB888, w, h, false);
            nextFbo = new FrameBuffer(Pixmap.Format.RGB888, w, h, false);

            init = true; // Menandai bahwa inisialisasi selesai
        }

        // Mulai proses transisi ke screen berikutnya
        nextScreen = screen;

        // Aktifkan screen berikutnya
        nextScreen.show();          // Menjalankan logika awal saat screen muncul

        nextScreen.resize(w, h);    // Menyesuaikan ukuran screen sesuai ukuran layar
        nextScreen.render(0);       // Render pertama kali agar screen bisa dipersiapkan tampil

        // Jika screen sebelumnya ada, hentikan sementara
        if (currScreen != null) currScreen.pause();

        // Pause dulu screen baru, nanti akan di-resume setelah transisi selesai
        nextScreen.pause();

        // Matikan sementara input agar tidak terjadi interaksi selama transisi
        Gdx.input.setInputProcessor(null);

        // Simpan transisi yang akan digunakan (misalnya fade, slide, dll.)
        this.screenTransition = screenTransition;

        // Reset waktu transisi ke 0
        t = 0;

        isTransitionFinished = false;
    }

    @Override
    public void render () {
        // Ambil waktu delta (selisih waktu antara frame sebelumnya dan sekarang),
        // tapi dibatasi maksimum 1/60 detik agar animasi tetap halus.
        float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1.0f / 60.0f);


        if (nextScreen == null) {
            // Tidak ada transisi yang sedang berlangsung
            if (currScreen != null)
                currScreen.render(deltaTime); // Render screen aktif saat ini


        } else {
            // Ada transisi yang sedang berlangsung
            float duration = 0;
            if (screenTransition != null)
                duration = screenTransition.getDuration(); // Ambil durasi transisi

            // Tambahkan waktu transisi secara bertahap setiap frame
            t = Math.min(t + deltaTime, duration);

            if (screenTransition == null || t >= duration) {
                // Jika tidak ada efek transisi, atau transisi sudah selesai

                if (currScreen != null){
                    currScreen.hide(); // Sembunyikan screen lama (panggil hide())
                    currScreen.dispose();
                }

                nextScreen.resume(); // Resume screen baru


                // Ganti screen aktif
                currScreen = nextScreen;
                nextScreen = null;
                screenTransition = null;

                isTransitionFinished = true;

            } else {
                // Transisi sedang berlangsung, render kedua screen ke FrameBuffer

                // Render screen saat ini ke framebuffer
                currFbo.begin();
                if (currScreen != null)
                    currScreen.render(deltaTime);
                currFbo.end();

                // Render screen berikutnya ke framebuffer
                nextFbo.begin();
                nextScreen.render(deltaTime);
                nextFbo.end();

                // Hitung nilai alpha untuk efek transisi (semakin lama, makin penuh)
                float alpha = t / duration;

                // Gambar efek transisi dari screen lama ke screen baru
                screenTransition.render(
                    batch,
                    currFbo.getColorBufferTexture(), // hasil render screen lama
                    nextFbo.getColorBufferTexture(), // hasil render screen baru
                    alpha // proporsi transisi (0.0 hingga 1.0)
                );
            }
        }
    }

    @Override
    public void resize (int width, int height) {
        if (currScreen != null) currScreen.resize(width, height);
        if (nextScreen != null) nextScreen.resize(width, height);
    }

    @Override
    public void pause () {
        if (currScreen != null) currScreen.pause();
    }

    @Override
    public void resume () {
        if (currScreen != null) currScreen.resume();
    }

    @Override
    public void dispose () {
        if (currScreen != null) currScreen.dispose();
        if (nextScreen != null) nextScreen.dispose();
        if (batch!=null){
            batch.dispose();
        }
        if (init) {
            currFbo.dispose();
            currScreen = null;
            nextFbo.dispose();
            nextScreen = null;
            init = false;
        }
    }


    public boolean isTransitionFinished() {
        return isTransitionFinished;
    }
}
