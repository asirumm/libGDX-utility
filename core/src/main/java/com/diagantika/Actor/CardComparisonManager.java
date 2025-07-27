package com.diagantika.Actor;

import org.tinylog.Logger;

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
        if (selectedCard1 == null) {
            selectedCard1 = card;
            Logger.debug("First card selected: {}", card);

        } else if (selectedCard2 == null && card != selectedCard1) {
            selectedCard2 = card;
            Logger.debug("Second card selected: {}", card);

        }else {
            // Reset jika card sudah dipilih atau pilih card ketiga
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
        if (selectedCard1 == null || selectedCard2 == null) {
            Logger.warn("Gagal membandingkan kartu: salah satu kartu masih null");
            return;
        }

        Logger.debug("Comparing cards: {} vs {}", selectedCard1, selectedCard2);

        int result = selectedCard1.compareTo(selectedCard2);

        if (result != 0) {
            Logger.debug("gak matches");

            // Tangkap kartu ke variabel final lokal agar tidak null saat Timer jalan
            final Card card1 = selectedCard1;
            final Card card2 = selectedCard2;

            card1.setClicked(false);
            card2.setClicked(false);

            card2.triggerFlipForAnimation();
            card1.triggerFlipForAnimation();


        } else {
            Logger.debug("MATCH ditemukan");
            resetSelection(); // langsung reset jika cocok
        }

        comparisionFinished = true;
    }

    public void setComparisionFinished(boolean comparisionFinished) {
        this.comparisionFinished = comparisionFinished;
    }

    public boolean isComparisionFinished() {
        return comparisionFinished;
    }

    private void resetSelection() {
        if (selectedCard1 != null) {
            selectedCard1 = null;
        }
        if (selectedCard2 != null) {
            selectedCard2 = null;
        }
    }
}
