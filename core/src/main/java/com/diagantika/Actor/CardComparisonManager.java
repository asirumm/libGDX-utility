package com.diagantika.Actor;

import org.tinylog.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CardComparisonManager {
    private static CardComparisonManager instance;
    private Card selectedCard1 = null;
    private Card selectedCard2 = null;

    private boolean comparisionFinished = false;

    public static CardComparisonManager getInstance() {
        if (instance == null) {
            instance = new CardComparisonManager();
        }
        return instance;
    }

    public void selectedCard(Card card){
        // apabila variable card 1 tidak null
        // maka kita isikan datanya
        if (selectedCard1 == null) {
            selectedCard1 = card;
            Logger.debug("First card selected: {}", card);

        }
        // apabila card1 tidak null dan card2 belum diisi
        // maka kita inject
        else if (selectedCard2 == null && card != selectedCard1) {
            selectedCard2 = card;
            Logger.debug("Second card selected: {}", card);

        }else {
            // Reset jika card sudah dipilih atau pilih card ketiga
            // jika pemain
            resetSelection();
            selectedCard(card);
        }
    }

    public boolean isCardFill(){
        if (selectedCard1 != null&&selectedCard2!=null){
            return true;
        }
        return false;
    }

    public void compareCards() {
        // pengecekan apakah semua card sudah terisi
        if (selectedCard1 == null || selectedCard2 == null) {
            Logger.warn("Gagal membandingkan kartu: salah satu kartu masih null");
            return;
        }

        Logger.debug("Comparing cards: {} vs {}", selectedCard1, selectedCard2);

        // proses membandingkan
        int result = selectedCard1.compareTo(selectedCard2);

        // Tangkap kartu ke variabel final lokal agar tidak null saat Thread jalan
        final Card card1 = selectedCard1;
        final Card card2 = selectedCard2;

        // apabila tidak match
        if (result != 0) {
            Logger.debug("gak matches");

            card1.setClicked(false);
            card2.setClicked(false);

            // Gunakan Thread dengan Runnable untuk menunggu animasi selesai
            Thread animationWaitThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Loop sampai animasi selesai
                        while (card1.statusFlipAnimation() != CardFlag.ANIMATION_FLIP_NOT_RUNNING ||
                                card2.statusFlipAnimation() != CardFlag.ANIMATION_FLIP_NOT_RUNNING) {

                            Logger.debug("menunggu flip selesai");
                            Thread.sleep(100); // tunggu 100ms sebelum cek lagi
                        }

                        // Setelah animasi selesai, flip balik ke belakang backTexture
                        Logger.debug("start trigger animation flip");
                        card2.triggerFlipForAnimation();
                        card1.triggerFlipForAnimation();

                        // Reset selection (comparisionFinished sudah true dari awal)
                        resetSelection();

                    } catch (InterruptedException e) {
                        Logger.error("Thread interrupted: {}", e.getMessage());
                        Thread.currentThread().interrupt(); // restore interrupted status
                    }
                }
            });

            // Start thread
            animationWaitThread.start();

            // Set flag segera setelah start thread agar render() tidak panggil lagi
            comparisionFinished = true;

        } else {
            // Kartu match - tunggu animasi selesai sebelum reset
            Thread matchWaitThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Tunggu sebentar untuk animasi
                        Thread.sleep(100);

                        // Tunggu sampai animasi selesai
                        while (card1.statusFlipAnimation() != CardFlag.ANIMATION_FLIP_NOT_RUNNING ||
                                card2.statusFlipAnimation() != CardFlag.ANIMATION_FLIP_NOT_RUNNING) {
                            Thread.sleep(50); // cek lebih sering untuk match
                        }

                        Logger.debug("Reset setelah MATCH");
                        resetSelection();
                        // comparisionFinished sudah di-set true dari awal

                    } catch (InterruptedException e) {
                        Logger.error("Thread interrupted: {}", e.getMessage());
                        Thread.currentThread().interrupt();
                    }
                }
            });

            // Set sebagai daemon thread agar mati ketika aplikasi di-close
            matchWaitThread.setDaemon(true);
            // Start thread
            matchWaitThread.start();

            // Set flag segera setelah start thread agar render() tidak panggil lagi
            comparisionFinished = true;
        }

        // HAPUS baris ini - jangan reset langsung!
        // resetSelection();
        // comparisionFinished = true;
    }
    public void setComparisionFinished(boolean comparisionFinished) {
        this.comparisionFinished = comparisionFinished;
    }

    /**
     * @return true ketika comparison process sudah berhasil dijalankan
     */
    public boolean isComparisionFinished() {
        return comparisionFinished;
    }

    private void resetSelection() {
        Logger.debug("reset comparison card");
        if (selectedCard1 != null) {
            selectedCard1 = null;
        }
        if (selectedCard2 != null) {
            selectedCard2 = null;
        }
        comparisionFinished = false;
    }
}
