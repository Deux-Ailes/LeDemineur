package com.example.jeufun;

import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements Serializable {
    private ArrayList<FragLigne> listLine;
    private final int difficulty;
    private long tStart;
    public static int width; // Width of the device
    public static int height; // Height of the device
    private Button play;
    private MediaPlayer mediaPlayer;
    private final String pseudo;

    public MainActivity(int difficulty, String pseudonym){
        this.difficulty = difficulty;
        this.pseudo=pseudonym;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.play);
        mediaPlayer = MediaPlayer.create(this, R.raw.music);

        LinearLayout container = findViewById(R.id.containerLigne);
        listLine = new ArrayList<>();
        // Getting the global dimension of the device
        // in order to put the right size for the cells and rows
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        Calendar dt = Calendar.getInstance();
        tStart = System.currentTimeMillis();
        // Creating X rows

        createGrid(8);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (FragLigne frag : listLine) {
            ft.add(R.id.containerLigne, frag, null);
        }
        ft.commit();
    }

    @Override
    protected void onResume(){
        super.onResume();
        setupBombs(difficulty);
        setupValues();

        play.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            else{
                mediaPlayer.start();
            }
        });

    }

    // Creates a grid of side * side size
    private void createGrid(int side){
        listLine.clear();
        for(int i=0; i<side;i++){
            listLine.add(FragLigne.newInstance(side,i));
        }
    }

    // Set up all the bombs according to the difficulty level
    // 0 : EASY / 1 : MEDIUM / 2 : HARD
    private void setupBombs(int difficulty){
        int nbBombs = ((difficulty+1) *10);
        for (int i=0; i<nbBombs;i++) {
            while(!randomBombCell());
        }
    }

    // Launch a new activity to select difficulty and show highest scores (5)
    private void selectDifficulty(){

    }

    private boolean randomBombCell(){
        Random r = new Random();
        Random r2 = new Random();
        int maxCell = listLine.size();
        int randomCell = r.nextInt(maxCell);
        int randomRow = r2.nextInt(maxCell);
        FragCellule frag;
        frag = listLine.get(randomRow).getListCells().get(randomCell);
        Log.e("La cellule visée en ", randomRow + ","+ randomCell + " ... " + frag.getState());
        if (frag.getState().equals("Bomb")) {
            Log.e("Rip","");
            //randomBombCell(liste);
            return false;
        } else {
            frag.becomeBomb();
            Log.e("gg","");
            return true;
        }
    }

    private void setupValues(){
        int numLigne =0;
        int numCell;
        for (FragLigne ligne: listLine){
            numCell =0;
            for( FragCellule cellule: ligne.getListCells()){
                int nbBombes = bombAround(numLigne,numCell);
                Log.e("NbBombes",String.valueOf(nbBombes));
                if(!cellule.getState().equals("Bomb")) cellule.setValue(String.valueOf(nbBombes));
                else Log.e("Je suis une bombinette","");
                numCell++;
            }
            numLigne++;
        }
    }

    private int bombAround(int numLignes, int numCell){
        int nombreBombes = 0;
        int minRow=numLignes-1;
        if(minRow<0) minRow=0;
        int maxRow=numLignes+1;
        if(maxRow> listLine.size()-1) maxRow = listLine.size()-1;
        int minCol=numCell-1;
        if(minCol <0) minCol =0;
        int maxCol = numCell +1;
        if(maxCol> listLine.get(0).getListCells().size()-1) maxCol = listLine.get(0).getListCells().size()-1;
        for(int i=minRow;i<maxRow+1;i++){
            for(int j=minCol;j<maxCol+1;j++){
                if(listLine.get(i).getListCells().get(j).getState().equals("Bomb")) nombreBombes++;
            }
        }
        return nombreBombes;
    }

    public void emptyAround(FragCellule cellule){
        int ligne; int colonne;
        int[] positionCellule = findCell(cellule);
        ligne = positionCellule[0];
        colonne = positionCellule[1];
        FragCellule cell;
        int minRow=ligne-1;
        if(minRow<0) minRow=0;
        int maxRow=ligne+1;
        if(maxRow> listLine.size()-1) maxRow = listLine.size()-1;
        int minCol=colonne-1;
        if(minCol <0) minCol =0;
        int maxCol = colonne +1;
        if(maxCol> listLine.get(0).getListCells().size()-1) maxCol = listLine.get(0).getListCells().size()-1;
        //Log.e("Position", String.valueOf(positionCellule[0]) + "," + String.valueOf(positionCellule[1]));
        for(int i=minRow;i<maxRow+1;i++){
            for(int j=minCol;j<maxCol+1;j++){
                cell = listLine.get(i).getListCells().get(j);
                if(!cell.getState().equals("Bomb") && !cell.getState().equals("Show")){
                    cell.setState("Show");
                    cell.affichageValeur();
                    if (isNullCell(cell)) {
                        emptyAround(cell);
                        Log.e("CELLULE","VIDE");
                    }
                    else{
                        Log.e("CELLULE","PAS VIDE");
                    }
                }
            }
        }
    }

    private int[] findCell(FragCellule cellule){
        int [] positions;
        positions = new int[2];
        for(FragLigne ligne: listLine){
            positions[1]=0;
            for(FragCellule cell: ligne.getListCells()){
                if (cell==cellule){
                    Log.e("Cellule trouvée !!", String.valueOf(positions[0] + "," + String.valueOf(positions[1])));
                    return positions;
                }
                positions[1]++;
            }
            positions[0]++;
        }

        return positions;
    }

    public void gameOver(){
        for(FragLigne ligne : this.listLine){
            for(FragCellule cellule : ligne.getListCells()){
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
    private boolean isNullCell(FragCellule cell){
        //Log.e("CELLULE VALEUR ",String.valueOf(cell.getValue()));
        return cell.getValue().equals("0");
    }


    public void endGame(FragCellule cell){
        boolean finito = true;
        for(FragLigne ligne : this.listLine){
            for(FragCellule cellule : ligne.getListCells()){
                if (Integer.parseInt(cellule.getValue()) < 0 || Objects.equals(cellule.getState(), "Show") || cell == cellule) {
                } else finito = false;
            }
        }
        if(finito){
            Log.e("FINITO","GAME IS WON");
            // Acquisition du temps
            long secondElapsed = (System.currentTimeMillis()-tStart)/1000;

            // Préparation du bundle
            Score monScore = new Score(secondElapsed,this.difficulty, this.pseudo);
            Intent intent = new Intent(MainActivity.this, Fin.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("SCORE", monScore);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else{
            Log.e("NOT FINITO","GAME IS NOT WON");
        }
    }
}