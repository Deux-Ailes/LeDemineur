package com.example.jeufun;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import java.util.ArrayList;

public class AccesDonnees {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public AccesDonnees(SharedPreferences pref, SharedPreferences.Editor editor){
        this.prefs = pref;
        this.editor = editor;
    }

    // Permet d'écrire un score dans les shared preferences
    public void ecritureScore(String pseudo_temps, int difficulty){
        editor.putString(String.valueOf(difficulty),pseudo_temps);
        editor.apply();
    }

    // Retourne la liste des scores pour une difficulté donnée
    public ArrayList<String> getScores(int difficulty){
        ArrayList<String> liste = new ArrayList<>();


        return liste;
    }
}
