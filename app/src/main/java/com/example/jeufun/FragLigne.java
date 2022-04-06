package com.example.jeufun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragLigne#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragLigne extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String nbCellules = "nombreCellules";
    private static final String numLigne = "numeroLigne";

    // TODO: Rename and change types of parameters
    private int nombreCellules;
    private int numeroLigne;
    private ArrayList<FragCellule> listeCellule;

    public FragLigne() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragLigne.
     */
    // TODO: Rename and change types and number of parameters
    public static FragLigne newInstance(int nbCelluless, int numLignee) {
        FragLigne fragment = new FragLigne();
        Bundle args = new Bundle();
        args.putInt(nbCellules, nbCelluless);
        args.putInt(numLigne, numLignee);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.nombreCellules = getArguments().getInt(nbCellules);
            this.numeroLigne = getArguments().getInt(numLigne);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_ligne, container, false);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        listeCellule = new ArrayList<>();
        //Cell creations
        for (int i = 0; i < this.nombreCellules; i++) {
            // Creating there the cells with passiv state. Instanciation
            listeCellule.add(FragCellule.newInstance("Hidden", ""));
        }
        for (FragCellule frag : listeCellule) {
            ft.add(R.id.containerCellule, frag, null);
        }
        ft.commit();
        return view;
    }

    public ArrayList<FragCellule> getListCells() {
        return this.listeCellule;
    }
}