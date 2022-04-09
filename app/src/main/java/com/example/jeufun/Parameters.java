package com.example.jeufun;

import java.io.Serializable;

public class Parameters implements Serializable {
    private final String pseudo;
    private final int difficulty;

    public Parameters(String pseudo, int difficult) {
        this.pseudo = pseudo;
        this.difficulty = difficult;
    }

    public String getPseudo() {
        return pseudo;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
