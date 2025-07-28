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
        // penengecekan apakah semua card sudah terisi
        if (selectedCard1 == null || selectedCard2 == null) {
            Logger.warn("Gagal membandingkan kartu: salah satu kartu masih null");
            return;
        }

        Logger.debug("Comparing cards: {} vs {}", selectedCard1, selectedCard2);

        // proses membandingkan
        int result = selectedCard1.compareTo(selectedCard2);

        // Tangkap kartu ke variabel final lokal agar tidak null saat Timer jalan

        final Card card1 = selectedCard1;
        final Card card2 = selectedCard2;

        // apabila tidak match
        // maka kita akan jalankan animasi flip
        if (result != 0) {
            Logger.debug("gak matches");


            card1.setClicked(false);
            card2.setClicked(false);

            // time schedule
            // kita akan terus eksekusi kode ini dalam rentan 800 ms
            // sampai kondisi terpenuhi dan akan dimatikan oleh
            // scheduler.shoutdown
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(() -> {

                // apabila semua animasi pada kartu yang dipilih sudah selesai
                if (card1.statusFlipAnimation() == CardFlag.ANIMATION_FLIP_NOT_RUNNING &&
                    card2.statusFlipAnimation() == CardFlag.ANIMATION_FLIP_NOT_RUNNING) {

                    Logger.debug("start trigger animation flip");
                    // kita flip balik ke belakang backTexture
                    card2.triggerFlipForAnimation();
                    card1.triggerFlipForAnimation();

                    resetSelection();
                    scheduler.shutdown(); // stop proses schedule

                } else {
                    Logger.debug("menunggu flip selesai");
                }
            }, 0, 100, TimeUnit.MILLISECONDS); // cek setiap 100ms


        } else {
            // Tunggu sebentar sebelum reset
            ScheduledExecutorService resetScheduler = Executors.newSingleThreadScheduledExecutor();
            resetScheduler.schedule(() -> {
                        // apabila semua animasi pada kartu yang dipilih sudah selesai
                        if (card1.statusFlipAnimation() == CardFlag.ANIMATION_FLIP_NOT_RUNNING &&
                                card2.statusFlipAnimation() == CardFlag.ANIMATION_FLIP_NOT_RUNNING) {
                            Logger.debug("Reset setelah MATCH");
                            resetSelection();
                            comparisionFinished = true;

                            resetScheduler.shutdown();
                        }


            }, 100, TimeUnit.MILLISECONDS); // delay sesuai animasi

            comparisionFinished = true; // tetap tandai selesai agar render tidak panggil terus
        }

        resetSelection();
        comparisionFinished = true;

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
