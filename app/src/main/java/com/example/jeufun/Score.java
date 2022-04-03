package com.example.jeufun;

import java.io.Serializable;

public class Score implements Serializable {
    public long getSeconds() {
        return seconds;
    }

    public int getDifficulty() {
        return difficulty;
    }

    private final long seconds;
    private final int difficulty;
    private final String pseudo;

    public Score(long secondsElapsed, int difficulty, String pseudonyme){
        this.difficulty=difficulty;
        this.seconds=secondsElapsed;
        this.pseudo = pseudonyme;
    }
}
