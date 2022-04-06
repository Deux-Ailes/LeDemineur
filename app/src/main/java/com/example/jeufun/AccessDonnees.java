package com.example.jeufun;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class AccessDonnees {
    private final Gson gson;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public AccessDonnees(SharedPreferences pref, SharedPreferences.Editor editor) {
        this.prefs = pref;
        this.editor = editor;
        gson = new Gson();
    }

    // Permet d'écrire un score dans les shared preferences
    public void ecritureScore(Score score) {
        // Récupération de l'objet
        String listScoreDifficultJSON = prefs.getString(String.valueOf(score.getDifficulty()), "");
        ArrayList<Score> scoreDifficulty = gson.fromJson(listScoreDifficultJSON, new TypeToken<ArrayList<Score>>() {
        }.getType());
        if (scoreDifficulty == null) {
            scoreDifficulty = new ArrayList<>();
        }
        // Ajout dans la liste
        scoreDifficulty.add(score);
        String scoreJson = gson.toJson(scoreDifficulty);

        // Commit
        editor.putString(String.valueOf(score.getDifficulty()), scoreJson);
        editor.apply();
    }

    // Retourne la liste des scores pour une difficulté donnée
    public ArrayList<Score> getScores(int difficulty) {
        String json = prefs.getString(String.valueOf(difficulty), "");
        return gson.fromJson(json, new TypeToken<ArrayList<Score>>() {
        }.getType());
    }
}
