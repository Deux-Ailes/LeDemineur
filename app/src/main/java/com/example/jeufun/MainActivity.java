package com.example.jeufun;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements Serializable {
    private ArrayList<FragLigne> listLine;
    private int difficulty;
    private long tStart;
    private Button play;
    private String pseudo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Récupération du bundle
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Parametre p = (Parametre) bundle.getSerializable("PARAM");
        this.pseudo = p.getPseudo();
        this.difficulty = p.getDifficulte();

        // Attribution des views aux vars
        play = findViewById(R.id.play);

        LinearLayout container = findViewById(R.id.containerLigne);
        listLine = new ArrayList<>();
        tStart = System.currentTimeMillis();
        createGrid((difficulty + 1) * 4);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (FragLigne frag : listLine) {
            ft.add(R.id.containerLigne, frag, null);
        }
        ft.commit();

        play.setOnClickListener(view -> {
            if (ServiceDeMusique.maMusique.isPlaying()) {
                ServiceDeMusique.maMusique.pause();
            }
            else{
                ServiceDeMusique.maMusique.start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBombs(difficulty);
        setupValues();
    }


    // Creates a grid of side * side size
    private void createGrid(int side) {
        listLine.clear();
        for (int i = 0; i < side; i++) {
            listLine.add(FragLigne.newInstance(side, i));
        }
    }

    // Set up all the bombs according to the difficulty level
    // 0 : EASY / 1 : MEDIUM / 2 : HARD
    private void setupBombs(int difficulty) {
        int nbBombs = ((difficulty + 1));
        for (int i = 0; i < nbBombs; i++) {
            while (!randomBombCell()) ;
        }
    }

    private boolean randomBombCell() {
        Random r = new Random();
        Random r2 = new Random();
        int maxCell = listLine.size();
        int randomCell = r.nextInt(maxCell);
        int randomRow = r2.nextInt(maxCell);
        FragCellule frag;
        frag = listLine.get(randomRow).getListCells().get(randomCell);
        Log.e("La cellule visée en ", randomRow + "," + randomCell + " ... " + frag.getState());
        if (frag.getState().equals("Bomb")) {
            Log.e("Rip", "");
            //randomBombCell(liste);
            return false;
        } else {
            frag.becomeBomb();
            Log.e("gg", "");
            return true;
        }
    }

    private void setupValues() {
        int numLigne = 0;
        int numCell;
        for (FragLigne ligne : listLine) {
            numCell = 0;
            for (FragCellule cellule : ligne.getListCells()) {
                int nbBombes = bombAround(numLigne, numCell);
                Log.e("NbBombes", String.valueOf(nbBombes));
                if (!cellule.getState().equals("Bomb")) cellule.setValue(String.valueOf(nbBombes));
                else Log.e("Je suis une bombinette", "");
                numCell++;
            }
            numLigne++;
        }
    }

    private int bombAround(int numLignes, int numCell) {
        int nombreBombes = 0;
        int minRow = numLignes - 1;
        if (minRow < 0) minRow = 0;
        int maxRow = numLignes + 1;
        if (maxRow > listLine.size() - 1) maxRow = listLine.size() - 1;
        int minCol = numCell - 1;
        if (minCol < 0) minCol = 0;
        int maxCol = numCell + 1;
        if (maxCol > listLine.get(0).getListCells().size() - 1)
            maxCol = listLine.get(0).getListCells().size() - 1;
        for (int i = minRow; i < maxRow + 1; i++) {
            for (int j = minCol; j < maxCol + 1; j++) {
                if (listLine.get(i).getListCells().get(j).getState().equals("Bomb")) nombreBombes++;
            }
        }
        return nombreBombes;
    }

    public void emptyAround(FragCellule cellule) {
        int ligne;
        int colonne;
        int[] positionCellule = findCell(cellule);
        ligne = positionCellule[0];
        colonne = positionCellule[1];
        FragCellule cell;
        int minRow = ligne - 1;
        if (minRow < 0) minRow = 0;
        int maxRow = ligne + 1;
        if (maxRow > listLine.size() - 1) maxRow = listLine.size() - 1;
        int minCol = colonne - 1;
        if (minCol < 0) minCol = 0;
        int maxCol = colonne + 1;
        if (maxCol > listLine.get(0).getListCells().size() - 1)
            maxCol = listLine.get(0).getListCells().size() - 1;
        //Log.e("Position", String.valueOf(positionCellule[0]) + "," + String.valueOf(positionCellule[1]));
        for (int i = minRow; i < maxRow + 1; i++) {
            for (int j = minCol; j < maxCol + 1; j++) {
                cell = listLine.get(i).getListCells().get(j);
                if (!cell.getState().equals("Bomb") && !cell.getState().equals("Show")) {
                    cell.setState("Show");
                    cell.affichageValeur();
                    if (isNullCell(cell)) {
                        emptyAround(cell);
                        Log.e("CELLULE", "VIDE");
                    } else {
                        Log.e("CELLULE", "PAS VIDE");
                    }
                }
            }
        }
    }

    private int[] findCell(FragCellule cellule) {
        int[] positions;
        positions = new int[2];
        for (FragLigne ligne : listLine) {
            positions[1] = 0;
            for (FragCellule cell : ligne.getListCells()) {
                if (cell == cellule) {
                    Log.e("Cellule trouvée !!", positions[0] + "," + positions[1]);
                    return positions;
                }
                positions[1]++;
            }
            positions[0]++;
        }

        return positions;
    }

    public void gameOver() {
        for (FragLigne ligne : this.listLine) {
            for (FragCellule cellule : ligne.getListCells()) {
                if (!cellule.getValue().equals("-1") || !cellule.getState().equals("Bomb")) {
                } else {
                    cellule.displayBomb();
                    //Délai pour que l'utilisateur voit les bombes

                    //SystemClock.sleep(3000);
                    // On retourne sur l'écran de fin
                    Intent intent = new Intent(MainActivity.this, Fin.class);
                    startActivity(intent);
                }
            }
        }
    }

    private boolean isNullCell(FragCellule cell) {
        return cell.getValue().equals("0");
    }


    public void endGame(FragCellule cell) {
        boolean finito = true;
        for (FragLigne ligne : this.listLine) {
            for (FragCellule cellule : ligne.getListCells()) {
                if (Integer.parseInt(cellule.getValue()) < 0 || Objects.equals(cellule.getState(), "Show") || cell == cellule) {
                } else finito = false;
            }
        }
        if (finito) {
            // Acquisition du temps
            long secondElapsed = (System.currentTimeMillis() - tStart) / 1000;

            // Préparation du bundle
            Score monScore = new Score(secondElapsed, this.difficulty, this.pseudo);
            Intent intent = new Intent(MainActivity.this, Fin.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("SCORE", monScore);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}