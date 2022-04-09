package com.example.jeufun;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

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
    private Intent serviceDeMusique;
    private Button tuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Attribution des variables & objets privés
        this.difficulty = findViewById(R.id.seekBar3);
        this.pseudo = findViewById(R.id.inPutDiff2);
        this.button = findViewById(R.id.btn_Diff);
        this.screenSelector = findViewById(R.id.divScores);
        this.tuto = findViewById(R.id.btn_Jouer);
        this.gameStart = findViewById(R.id.divGame);
        this.displayScore = findViewById(R.id.displayScore);
        this.prefs = getSharedPreferences("scores", MODE_PRIVATE);
        this.editor = prefs.edit();
        this.dataAccess = new AccessDonnees(prefs, editor);
        this.serviceDeMusique = new Intent(this, ServiceDeMusique.class);

    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(serviceDeMusique);
        displayScore.setVisibility(View.INVISIBLE); // Nécessaire pour cacher au tout début le bloc

        attributionScore();

        // Setup du click listener pour lancer la partie
        this.button.setOnClickListener(v -> {
            String pseudonyme = this.pseudo.getText().toString();
            if (pseudonyme.equals("")) pseudonyme = "Sans nom"; // Si le champ est vide alors on met un pseudo bidon
            int difficult = this.difficulty.getProgress();      // Valeur retournée par le slider, entre 0 et 2
            Parameters param = new Parameters(pseudonyme, difficult); // Création d'un objet issue de la classe Parametre
            Intent intent = new Intent(Menu.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("PARAM", param); // Ajout de l'objet contenant toutes les valeurs pour les parametres dans le bundle
            intent.putExtras(bundle);
            startActivity(intent);
        });

        // Setup du onChangeListener du slider qui gère l'affichage
        this.screenSelector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // Si la valeur est à 1 alors on affiche les scores
                if (screenSelector.getProgress() == 1) {
                    gameStart.setVisibility(View.INVISIBLE);
                    displayScore.setVisibility(View.VISIBLE);
                } // Sinon l'affichage de création de partie
                else {
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

        this.tuto.setOnClickListener(v->{
            // Tiré d'un tutoriel stackOverflow
            onButtonShowPopupWindowClick(findViewById(R.id.originalLayout));
        });
    }

    /**
     * Tiré du tuto https://stackoverflow.com/questions/5944987/how-to-create-a-popup-window-popupwindow-in-android
     * @param view Layout sur lequel la vue s'affichera
     */
    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.tuto, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(serviceDeMusique); // A ne mettre que si l'on veut que la musique soit persistante jusqu'à ce que l'application soit terminée
    }

    @Override
    protected void onStop() {
        super.onStop();
        //  stopService(serviceDeMusique);  // A ne mettre que si l'on veut que la musique s'arrête dès que l'on quitte l'activité
    }


    /**
     * Remplie les scores en fonction des données présentes dans les sharedPreferences
     */
    private void attributionScore() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Les fragments initialisés
        for (int i = 0; i < 3; i++) {
            ArrayList<Score> liste = dataAccess.getScores(i);
            int id = R.id.afficheEz;
            if (i == 1) {
                id = R.id.afficheMed;
            } else if (i == 2) {
                id = R.id.afficheHard;
            }
            // Tri en fonction du temps
            if (liste != null) {
                liste.sort(Comparator.comparingLong(Score::getSeconds));
                for (Score score : liste) {
                    fragment_frag_scores frag = fragment_frag_scores.newInstance(score.getPseudo(), score.getSeconds());
                    ft.add(id, frag);
                }
            }
        }
        ft.commit();
    }
}