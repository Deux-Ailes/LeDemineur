package com.example.jeufun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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
        this.width = size.x;
        this.height = size.y;

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
            randomBombCell(liste);
        }
    }

    // Launch a new activity to select difficulty and show highest scores (5)
    private void selectDifficulty(){

    }

    private void randomBombCell(ArrayList<FragLigne> liste){
        int min = 0, maxCell = liste.size();
        int randomCell = ThreadLocalRandom.current().nextInt(min, maxCell-1);
        int randomRow = ThreadLocalRandom.current().nextInt(min, maxCell-1);
        FragCellule frag = liste.get(randomRow).getListCells().get(randomCell);
        if(frag.getState()==""){
            frag.becomeBomb();
        }else{
            randomBombCell(liste);
        }
    }
}