package com.example.jeufun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private LinearLayout container;
    private ArrayList<FragLigne> listeLigne;
    public static int width; // Width of the device
    public static int height; // Height of the device
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.containerLigne);
        listeLigne = new ArrayList<>();
        // Getting the global dimension of the device
        // in order to put the right size for the cells and rows
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        // Creating X rows
        createGrid(10);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (FragLigne frag: listeLigne) {
            ft.add(R.id.containerLigne,frag,null);
        }
        ft.commit();
    }

    @Override
    protected void onResume(){
        super.onResume();
        setupBombs(listeLigne,0);
        setupValues(listeLigne);
    }

    // Creates a grid of side * side size
    private void createGrid(int side){
        listeLigne.clear();
        for(int i=0; i<side;i++){
            listeLigne.add(FragLigne.newInstance(side,i));
        }
    }

    // Set up all the bombs according to the difficulty level
    // 0 : EASY / 1 : MEDIUM / 2 : HARD
    private void setupBombs(ArrayList<FragLigne> liste, int difficulty){
        int nbBombs = ((difficulty+1) *10);
        for (int i=0; i<nbBombs;i++) {
            while(!randomBombCell(liste));
        }
    }

    // Launch a new activity to select difficulty and show highest scores (5)
    private void selectDifficulty(){

    }

    private boolean randomBombCell(ArrayList<FragLigne> liste){
        Random r = new Random();
        Random r2 = new Random();
        int maxCell = liste.size();
        int randomCell = r.nextInt(maxCell);
        int randomRow = r2.nextInt(maxCell);
        FragCellule frag = liste.get(randomRow).getListCells().get(randomCell);
        Log.e("La cellule visée en ", randomRow + ","+ randomCell + " ... " + String.valueOf(frag.getState()));
        if(frag.getState()!="Bomb"){
            frag.becomeBomb();
            Log.e("gg","");
            return true;
        }else{
            Log.e("Rip","");
            //randomBombCell(liste);
            return false;
        }
    }

    private void setupValues(ArrayList<FragLigne> listeDeLignes){
        int numLigne =0;
        int numCell = 0;
        for (FragLigne ligne: listeDeLignes){
            numCell =0;
            for( FragCellule cellule: ligne.getListCells()){
                int nbBombes = bombAround(numLigne,listeDeLignes,numCell);
                if(cellule.getState()!="Bomb") cellule.attributionValeur(nbBombes);
                else Log.e("Je suis une bombinette","");
                numCell++;
            }
            numLigne++;
        }
    }

    private int bombAround(int numLignes, ArrayList<FragLigne> liste, int numCell){
        int nombreBombes = 0;
        int minRow=numLignes-1;
        if(minRow<0) minRow=0;
        int maxRow=numLignes+1;
        if(maxRow>liste.size()-1) maxRow = liste.size()-1;
        int minCol=numCell-1;
        if(minCol <0) minCol =0;
        int maxCol = numCell +1;
        if(maxCol>liste.get(0).getListCells().size()-1) maxCol = liste.get(0).getListCells().size()-1;
        for(int i=minRow;i<maxRow+1;i++){
            for(int j=minCol;j<maxCol+1;j++){
                if(liste.get(i).getListCells().get(j).getState()=="Bomb") nombreBombes++;
            }
        }
        return nombreBombes;
    }
}