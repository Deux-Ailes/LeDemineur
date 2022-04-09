package com.example.jeufun;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

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
    private boolean gameOver = false;

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
        this.cellImage.setOnLongClickListener(v -> {
            // Lorsqu'on veut mettre un drapeau
            if (this.getState() == "Flagged") { // Si anciennement un drapeau
                this.cellImage.setImageResource(R.drawable.facingdown); // On remet l'image d'une case cachée
                if (getValue() == "-1") this.setState("Bomb");          // S'il s'agit d'une bombe, on remet son état à Bomb
                else this.setState("Hidden");                           // Sinon on met l'état à Hidden
            } else {                            // Sinon
                this.cellImage.setImageResource(R.drawable.flagged);    //On affiche un drapeau
                this.setState("Flagged");                               // Et on met l'état à "Flagged"
            }
            return true;
        });

        this.cellImage.setOnClickListener(v -> {
            if (!gameOver) { // TODO Game Over pas utilisé, à finir d'implementer
                if (this.getState() == "Bomb") {    // Si l'on clique sur une bombe
                    ((MainActivity) getActivity()).gameOver();  // Game Over
                } else {
                    affichageValeur();  // Sinon on affiche la valeur à l'écran
                    if (Integer.valueOf(this.value) == 0)   // S'il s'agit d'une case qui n'a pas de bombes autour
                        ((MainActivity) getActivity()).emptyAround(this); // On lance la méthode dans la mainActivity
                }
                ((MainActivity) getActivity()).endGame(this);   // On vérifie si à l'issue du clic il reste des cases. Un observer aurait pu être utilisé plutôt que cela
            }
        });
        return view;
    }


    public String getValue() {
        return this.value;
    }

    public void setValue(String valeur) {
        this.value = valeur;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
        if (state == "Show") {
            affichageValeur();
        }
    }

    public void becomeBomb() {
        setState("Bomb");
        setValue("-1");
    }

    public void displayBomb() {
        if (this.getState() == "Bomb") {
            gameOver = true;
            this.cellImage.setImageResource(R.drawable.bombinette);
        }
    }

    /**
     * Modifie l'état de la cellule à Show et affiche la valeur de la cellule
     */
    public void affichageValeur() {
        this.state = "Show";
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
            default:
                this.cellImage.setImageResource(R.drawable.bombinette);
                break;
        }
    }


}