package com.example.jeufun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;

public class Fin extends AppCompatActivity {

    private LinearLayout facile;
    private LinearLayout moyen;
    private LinearLayout hard;
    private TextView victoireDefaite;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private AccessDonnees dataAccess;
    private Button btnReplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin);

        // Attribution des containers pour les fragments
        facile = findViewById(R.id.ez);
        moyen = findViewById(R.id.med);
        hard = findViewById(R.id.hard);
        btnReplay = findViewById(R.id.replay);
        victoireDefaite = findViewById(R.id.victoireDefaite);

        prefs= getSharedPreferences("scores",MODE_PRIVATE);
        editor = prefs.edit();
        dataAccess = new AccessDonnees(prefs,editor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        boolean victory = false;
        if(bundle!=null){
            victory = true;
            Score s = (Score)bundle.getSerializable("SCORE");
            dataAccess.ecritureScore(s);
        }

        // Mise en forme
        if(victory) victoireDefaite.setText("Bien joué, tu as gagné !");
        else victoireDefaite.setText("Arf, bien tenté !");


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Les fragments initialisés
        for(int i=0;i<3;i++){
            ArrayList<Score> liste = dataAccess.getScores(i);
            int id = R.id.ez;
            if(i==1){
                id = R.id.med;
            }else if(i==2){
                id = R.id.hard;
            }
            // Tri en fonction du temps
            if(liste!=null) {
                liste.sort(Comparator.<Score>comparingLong(Score::getSeconds));
                for (Score score : liste) {
                    fragment_frag_scores frag = fragment_frag_scores.newInstance(score.getPseudo(),score.getSeconds());
                    ft.add(id, frag);
                }
            }
        }
        ft.commit();


        btnReplay.setOnClickListener(v->{
            Intent monIntent = new Intent(this, Menu.class);
            startActivity(monIntent);
        });

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