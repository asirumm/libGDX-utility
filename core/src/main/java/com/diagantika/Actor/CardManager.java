package com.diagantika.Actor;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.tinylog.Logger;

import java.util.Random;

public class CardManager {
    private TextureRegion backCard;
    private Array<Card> cards;// kartu sudah berpasangan dan sudah di shuffle
    private TextureAtlas atlas;
    private Array<TextureRegion> frontCards;

    private CardComparisonManager cardComparisonManager;


    public CardManager(TextureAtlas atlas) {
        this.atlas = atlas;

        cardComparisonManager = CardComparisonManager.getInstance();

        backCard = atlas.findRegion("card-back");

        if (backCard==null){
            throw new GdxRuntimeException("back card kosong");
        }

        // memuat semua front card
        frontCardGenerate();

        // membuat kartu berpasangan
        generateCardPairs();

    }

    public Array<Card> getCards() {
        return cards;
    }

    public void render(){
        if (cardComparisonManager.isCardFill()&& !cardComparisonManager.isComparisionFinished()){
            cardComparisonManager.compareCards();
        }
    }

    /**
     * Membuat id card untuk identifikasi
     * Hasil : card-7
     */
    private String randomIdCardGenerator(Random random){
        byte randomByte1 = (byte) Math.abs(random.nextInt(100));
        byte randomByte2 = (byte) Math.abs( random.nextInt(100));

        StringBuilder text = new StringBuilder();
        text.append("card-");
        text.append(randomByte1);
        text.append(randomByte2);

        return text.toString();
    }

    /**
     * Membuat card yang sudah berpasangan
     */
    private void generateCardPairs() {

        cards = new Array<>();
        Random random = new Random();

        for (TextureRegion front : frontCards) {
            CardInstance(cards, random, front);
        }

        // acak posisi kartu
        cards.shuffle();

        Logger.debug("mengisi array pairs card  selesai");

    }

    private void frontCardGenerate() {

        frontCards = new Array<>();

        frontCards.add(atlas.findRegion("card2"));
        frontCards.add(atlas.findRegion("card3"));
        frontCards.add(atlas.findRegion("card4"));

        for (byte i=0;i<=frontCards.size-1;i++){
            if(frontCards.get(i)==null){
                Logger.warn("card front tidak ditemukan pada index {} di frontCards array",i);
                throw new GdxRuntimeException("card front tidak ditemukan, periksa kembali frontCards");
            }
        }


        org.tinylog.Logger.debug("mengisi array front card selesai");

    }

    /**
     * @param pairCount berapa banyak kartu yang harus match
     *                Ex : 2 artinya akan dibuat 4 kartu
     */
    public Array<Card> generateRandomCard(byte pairCount){

        Array<Card> requestCards = new Array<>();

        Random random = new Random();

        // berapa kurang kartunya
        if (frontCards.size<pairCount){
            Logger.debug("pair count {} dan frontCards size {}",pairCount,frontCards.size);

            int howMuchCardShouldAdded =  frontCards.size - pairCount;
            for (int i=0;i<=howMuchCardShouldAdded;i++){

                TextureRegion front = frontCards.random();

                CardInstance(requestCards, random, front);
            }

            requestCards.addAll(cards);

        }


        return requestCards;
    }

    private void CardInstance(Array<Card> cards, Random random, TextureRegion front) {
        String idCard = randomIdCardGenerator(random);

        Card card1 = new Card();
        card1.build(front,backCard,idCard);

        Card card2 = new Card();
        card2.build(front,backCard,idCard);

        org.tinylog.Logger.debug("instance card telah dibuat {}",card1.toString());
        org.tinylog.Logger.debug("instance card telah dibuat {}",card1.toString());

        cards.add(card1);
        cards.add(card2);
    }
}
