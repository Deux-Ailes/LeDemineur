package com.example.jeufun;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "state";
    private static final String ARG_PARAM2 = "value";

    // TODO: Rename and change types of parameters
    private String state; // State of the cell
    private String value; // Number of bombs around or if is bomb
    private boolean isDiscovered;
    private ImageView cellImage; // Is the cell image

    public FragCellule() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment FragCellule.
     */
    // TODO: Rename and change types and number of parameters
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
        this.cellImage.setOnClickListener(v->{
            switch (this.value){
                case "b":
                    // Game over
                    break;
                case "0":
                    // Check if other cells are equals to zero around it
                    break;
                case "1":
                    // Display a 1
                    break;
                case "2":
                    break;
                case "3":
                    break;
                case "4":
                    break;
                case "5":
                    break;

                default:
                    // That's shit.
                    this.cellImage.setImageResource(R.drawable.ok);
            }
        });
        return view;
    }
}