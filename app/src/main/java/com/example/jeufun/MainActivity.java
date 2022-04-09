package com.example.jeufun;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
/**
 *
 * Démineur composé par Elliot Clerice, Simon Meinard et Tony Pasquier
 * Github disponible ici : https://github.com/Deux-Ailes/LeDemineur
 *
 */
public class MainActivity extends AppCompatActivity implements Serializable {
    private ArrayList<FragLigne> listLine;
    private int difficulty;
    private long tStart;
    private String pseudo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Récupération du bundle
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Parameters p = (Parameters) bundle.getSerializable("PARAM");
        this.pseudo = p.getPseudo();
        this.difficulty = p.getDifficulty();
        //
        tStart = System.currentTimeMillis(); // On récupère le temps auquel l'activité est lancée
        // Attribution des views aux vars
        Button play = findViewById(R.id.play);
        // Fragments à gogo
        listLine = new ArrayList<>();
        createGrid((difficulty + 1) * 4); // Création d'une grille selon la difficulté
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
        int nbBombs = ((difficulty + 1)*3);
        for (int i = 0; i < nbBombs; i++) {
            while (!randomBombCell()) ;
        }
    }

    /**
     * Permet de transformer une cellule en bombe.
     * @return Boolean si la cellule est devenue une bombe
     */
    private boolean randomBombCell() {
        Random r = new Random();
        Random r2 = new Random();
        int maxCell = listLine.size();
        int randomCell = r.nextInt(maxCell);
        int randomRow = r2.nextInt(maxCell);
        FragCellule frag;
        frag = listLine.get(randomRow).getListCells().get(randomCell);
        if (frag.getState().equals("Bomb")) {
            Log.e("Rip", "");
            return false;
        } else {
            frag.becomeBomb();
            Log.e("gg", "");
            return true;
        }
    }

    /**
     * Met en place la valeur des bombes en fonction du nombre de bombes autour
     */
    private void setupValues() {
        int numLigne = 0;
        int numCell;
        for (FragLigne ligne : listLine) {
            numCell = 0;
            for (FragCellule cellule : ligne.getListCells()) {
                int nbBombes = bombAround(numLigne, numCell);
                if (!cellule.getState().equals("Bomb")) cellule.setValue(String.valueOf(nbBombes));
                numCell++;
            }
            numLigne++;
        }
    }

    /**
     *  Renseigne le nombre de bombes autour d'une cellule
     * @param numLignes Numéro de ligne sur lequel est positionné la cellule
     * @param numCell Numéro de colonne sur lequel est positionné la cellule
     * @return Le nombre de bombes autour
     */
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
        // Algorithme ennuyant pour savoir si l'on est au bord de la grille ou non
        int minRow = ligne - 1;
        if (minRow < 0) minRow = 0;
        int maxRow = ligne + 1;
        if (maxRow > listLine.size() - 1) maxRow = listLine.size() - 1;
        int minCol = colonne - 1;
        if (minCol < 0) minCol = 0;
        int maxCol = colonne + 1;
        if (maxCol > listLine.get(0).getListCells().size() - 1) maxCol = listLine.get(0).getListCells().size() - 1;
        // Fin de l'algorithme ennuyant
        for (int i = minRow; i < maxRow + 1; i++) {
            for (int j = minCol; j < maxCol + 1; j++) {
                cell = listLine.get(i).getListCells().get(j);
                if (!cell.getState().equals("Bomb") && !cell.getState().equals("Show")) { // Si la cellule n'est pas une bombe ou affichée
                    cell.setState("Show");  // Alors on change son état à "Show"
                    cell.affichageValeur(); // Et on affiche sa valeur
                    if (isNullCell(cell)) { // Si la cellule est nulle
                        emptyAround(cell);  // Alors on effectue la méthode sur cette nouvelle cellule
                        Log.e("CELLULE", "VIDE");
                    } else {
                        Log.e("CELLULE", "PAS VIDE");
                    }
                }
            }
        }
    }


    /**
     * Renseigne la position d'une cellule fournie en paramètre
     * @param cellule Cellule à trouver
     * @return [ligne, colonne]
     */
    private int[] findCell(FragCellule cellule) {
        int[] positions;
        positions = new int[2];
        // Parcours d'un tableau de tableau
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

    /**
     * Appelée lorsqu'une bombe est touchée
     * Lance l'activité de fin
     */
    public void gameOver() {
        for (FragLigne ligne : this.listLine) {
            for (FragCellule cellule : ligne.getListCells()) {
                if (!cellule.getValue().equals("-1") || !cellule.getState().equals("Bomb")) {
                } else {
                    cellule.displayBomb();
                    //Délai pour que l'utilisateur voit les bombes
                    // On retourne sur l'écran de fin
                    Intent intent = new Intent(MainActivity.this, Fin.class);
                    startActivity(intent);
                }
            }
        }
    }

    /**
     * Indique si une cellule est nulle ou non
     * @param cell Cellule à étudier
     * @return boolean si la cellule est nulle
     */
    private boolean isNullCell(FragCellule cell) {
        return cell.getValue().equals("0");
    }


    /**
     * Vérifie si toutes les cellules qui ne sont pas des bombes ont été affichées
     * Si oui, lance l'écran de fin
     * @param cell Cellule qui a trigger le endGame
     */
    public void endGame(FragCellule cell) {
        boolean finito = true;
        // Parcours du tableau
        for (FragLigne line : this.listLine) {
            for (FragCellule cellular : line.getListCells()) {
                if (Integer.parseInt(cellular.getValue()) < 0 || Objects.equals(cellular.getState(), "Show") || cell == cellular) {
                } else finito = false; // Si l'une des cases non-bombe est cachée, alors ce n'est pas fini
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