package com.example.jeufun;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Comparator;

public class Fin extends AppCompatActivity {
    private TextView victoryDefeat;
    private AccessDonnees dataAccess;
    private Button btnReplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin);

        // Attribution des containers pour les fragments
        btnReplay = findViewById(R.id.replay);
        victoryDefeat = findViewById(R.id.victoireDefaite);
        SharedPreferences prefs = getSharedPreferences("scores", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        dataAccess = new AccessDonnees(prefs, editor);                                  // Objet qui permet de communiquer avec les SharedPreferences
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        boolean victory = false; // Indique si l'on a gagné
        if (bundle != null) {
            victory = true; // Avoir un bundle est signe de victoire
            Score s = (Score) bundle.getSerializable("SCORE");
            dataAccess.writingScore(s); // On sauvegarde le score issu du bundle
        }

        // Mise en forme
        if (victory) victoryDefeat.setText(R.string.gg);
        else victoryDefeat.setText(R.string.pasdpo);

        // Fragments
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // Pour chaque difficulté (0 à 2)
        for (int i = 0; i < 3; i++) {
            ArrayList<Score> list = dataAccess.getScores(i);
            // Attribution d'un container selon la difficulté
            int id = R.id.ez;
            if (i == 1) {
                id = R.id.med;
            } else if (i == 2) {
                id = R.id.hard;
            }
            // Tri en fonction du temps + vérification
            if (list != null) {
                list.sort(Comparator.comparingLong(Score::getSeconds));
                for (Score score : list) {
                    fragment_frag_scores frag = fragment_frag_scores.newInstance(score.getPseudo(), score.getSeconds());
                    ft.add(id, frag);
                }
            }
        }
        ft.commit(); // Commit final une fois que tout a été ajouté

        // Bouton retour
        btnReplay.setOnClickListener(v -> {
            Intent monIntent = new Intent(this, Menu.class);
            startActivity(monIntent);
        });

    }





}