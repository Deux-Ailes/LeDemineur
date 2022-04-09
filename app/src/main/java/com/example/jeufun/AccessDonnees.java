package com.example.jeufun;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class AccessDonnees {
    private final Gson gson;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    /**
     * Constructeur
     * @param pref SharedPreferences créées dans l'activité
     * @param editor Editor tiré des prefs
     */
    public AccessDonnees(SharedPreferences pref, SharedPreferences.Editor editor) {
        this.prefs = pref;
        this.editor = editor;
        gson = new Gson();
    }

    /**
     * Fonction pour écrire un score dans le fichier dédié.
     * @param score Score à introduire dans les SharedPreferences
     */
    public void writingScore(Score score) {
        // Récupération de l'objet
        ArrayList<Score> scoreDifficulty =getScores(score.getDifficulty());                                                         // Traduction de la liste en une liste de Scores, à partir de la difficulté du score à ajouter
        if (scoreDifficulty == null) scoreDifficulty = new ArrayList<>();                                                           // Si pas de liste existante alors création
        scoreDifficulty.add(score);                                                                                                 // Ajout du score dans la liste
        String scoreJson = gson.toJson(scoreDifficulty);                                                                            // Conversion en json de la liste
        // Commit
        editor.putString(String.valueOf(score.getDifficulty()), scoreJson);                                                         // Ajout de la difficulté comme clef à la liste en json
        editor.apply();                                                                                                             // Commit des données dans le fichier des SharedPreferences
    }

    /**
     * Extraie la liste des scores selon la difficulté renseignée
     * @param difficulty Entre 0 et 2, de Facile à Difficile
     * @return Liste des scores
     */
    public ArrayList<Score> getScores(int difficulty) {
        String json = prefs.getString(String.valueOf(difficulty), "");
        return gson.fromJson(json, new TypeToken<ArrayList<Score>>() {}.getType());
    }
}
