package com.example.jeufun;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Fin extends AppCompatActivity {

    private LinearLayout facile;
    private LinearLayout moyen;
    private LinearLayout hard;
    private TextView victoireDefaite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin);

        // Attribution des containers pour les fragments
        facile = findViewById(R.id.ez);
        moyen = findViewById(R.id.med);
        hard = findViewById(R.id.hard);

        victoireDefaite = findViewById(R.id.victoireDefaite);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null){

        }
    }

    /*

    SI PRESENCE D'UN BUNDLE ALORS ON SUPPOSE LA VICTOIRE
    SINON DEFAITE

     */

    private void ecritureScore(Score scoreAjout){

    }


    private void affichageScores(){

    }



}