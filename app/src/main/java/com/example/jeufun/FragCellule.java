package com.example.jeufun;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragCellule#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragCellule extends Fragment {


    private static final String ARG_PARAM1 = "state";
    private static final String ARG_PARAM2 = "value";

    private String state; // State of the cell (Hidden, Discovered or Bomb)
    private String value; // Number of bombs around
    private ImageView cellImage; // Is the cell image

    public FragCellule() {
        // Required empty public constructor
    }

    public static FragCellule newInstance(String state, String value) {
        FragCellule fragment = new FragCellule();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, state);
        args.putString(ARG_PARAM2, value);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            state = getArguments().getString(ARG_PARAM1);
            value = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_cellule, container, false);
        this.cellImage = view.findViewById(R.id.cell);
        this.cellImage.setOnLongClickListener(v->{
            if(this.getState()=="Flagged"){
                this.cellImage.setImageResource(R.drawable.facingdown);
                this.setState("Hidden");
                this.cellImage.getDrawable();
            }
            else{
                this.cellImage.setImageResource(R.drawable.flagged);
                this.setState("Flagged");
            }


            ///
            ///
            /// A RETRAVAILLER POUR LA GESTION DE DRAPEAU SUR UNE BOMBE OU UNE VALEUR (CREER UNE ANCIENNE VALEUR STATIQUE)
            ///
            ///

            return true;
        });
        this.cellImage.setOnClickListener(v->{
            // Check if the cell is already discovered
            // If yes nothing
            // If not :
            //  If bomb Game Over and discovered
            //  If 0 shows empty cells around
            //  Else display the number of bombs around it
            if(this.getState()=="Bomb"){
                Log.e("Game over","C fini");
                // Fonction qui permet d'avertir que c'est finito, trigger le game over ?
            }
            else{
                affichageValeur();
                if (Integer.valueOf(this.value)==0)
                    ((MainActivity)getActivity()).emptyAround(this);
            }
            ((MainActivity)getActivity()).endGame();
        });
        return view;
    }


    public String getValue(){
        return this.value;
    }

    public void setValue(String valeur){
        this.value = valeur;
    }

    public String getState(){
        return this.state;
    }

    public void setState(String state){
        this.state = state;
        if(state == "Show"){
            affichageValeur();
        }
    }

    public void becomeBomb(){
        setState("Bomb");
        //this.cellImage.setImageResource(R.drawable.bomb);
    }

    public void affichageValeur(){
        switch (Integer.valueOf(this.value)) {
            case 0:
                this.cellImage.setImageResource(R.drawable.zero);
                break;
            case 1:
                this.cellImage.setImageResource(R.drawable.one);
                break;
            case 2:
                this.cellImage.setImageResource(R.drawable.two);
                break;
            case 3:
                this.cellImage.setImageResource(R.drawable.three);
                break;
            case 4:
                this.cellImage.setImageResource(R.drawable.four);
                break;
            case 5:
                this.cellImage.setImageResource(R.drawable.five);
                break;
            case 6:
                this.cellImage.setImageResource(R.drawable.six);
                break;
            case 7:
                this.cellImage.setImageResource(R.drawable.seven);
                break;
        }
    }


}