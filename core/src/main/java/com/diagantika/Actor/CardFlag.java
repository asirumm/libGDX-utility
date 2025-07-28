package com.diagantika.Actor;

public enum CardFlag {
    CARD_ANIMATION_ACTIVE,
    CARD_ANIMATION_INACTIVE,
    CARD_UNMATCHES,
    CARD_MATCHES,
    ANIMATION_FLIP_RUNNING,
    ANIMATION_FLIP_NOT_RUNNING
    // TODO Flag nya pakai enum aja bro
    // TODO problem, jadi ketika card ke 2 blom selesai
    // render tetapi sudah disuruh flip lagi sama comparision manager
}
