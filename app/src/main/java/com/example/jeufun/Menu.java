package com.example.jeufun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.Comparator;

public class Menu extends AppCompatActivity {
    private EditText pseudo;
    private Button button;
    private SeekBar difficulty;
    private SeekBar screenSelector;
    private ConstraintLayout gameStart;
    private LinearLayout displayScore;
    private AccessDonnees dataAccess;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.difficulty = findViewById(R.id.seekBar3);
        this.pseudo = findViewById(R.id.inPutDiff2);
        this.button = findViewById(R.id.btn_Diff);
        this.screenSelector = findViewById(R.id.divScores);
        gameStart = findViewById(R.id.divGame);
        displayScore = findViewById(R.id.displayScore);
        prefs= getSharedPreferences("scores",MODE_PRIVATE);
        editor = prefs.edit();
        dataAccess = new AccessDonnees(prefs,editor);
    }

    @Override
    protected void onResume() {
        super.onResume();

        gameStart.setVisibility(View.INVISIBLE); // Nécessaire pour cacher au tout début le bloc

        attributionScore();

        this.button.setOnClickListener(v -> {
            String pseudonyme = this.pseudo.getText().toString();
            if (pseudonyme.equals("")) pseudonyme = "Sans nom";
            int difficult = this.difficulty.getProgress();
            Parametre param = new Parametre(pseudonyme, difficult);
            Intent intent = new Intent(Menu.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("PARAM", param);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        this.screenSelector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if (screenSelector.getProgress() == 0) {
                    gameStart.setVisibility(View.INVISIBLE);
                    displayScore.setVisibility(View.VISIBLE);
                } else {
                    displayScore.setVisibility(View.INVISIBLE);
                    gameStart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void attributionScore(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Les fragments initialisés
        for(int i=0;i<3;i++){
            ArrayList<Score> liste = dataAccess.getScores(i);
            int id = R.id.afficheEz;
            if(i==1){
                id = R.id.afficheMed;
            }else if(i==2){
                id = R.id.afficheHard;
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
    }
}