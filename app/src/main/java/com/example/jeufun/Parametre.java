package com.example.jeufun;

import java.io.Serializable;

public class Parametre implements Serializable {
    public String getPseudo() {
        return pseudo;
    }

    public int getDifficulte() {
        return difficulte;
    }

    private final String pseudo;
    private final int difficulte;

    public Parametre(String pseudo, int difficult) {
        this.pseudo = pseudo;
        this.difficulte = difficult;
    }
}