package com.example.jeufun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_frag_scores#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_frag_scores extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "pseudonym";
    private static final String ARG_PARAM2 = "time";

    // TODO: Rename and change types of parameters
    private String pseudonym;
    private long time;

    public fragment_frag_scores() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_frag_scores.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_frag_scores newInstance(String param1, long param2) {
        fragment_frag_scores fragment = new fragment_frag_scores();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putLong(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pseudonym = getArguments().getString(ARG_PARAM1);
            time = getArguments().getLong(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Rien de bien transcendant ou novateur
        View view = inflater.inflate(R.layout.fragment_frag_scores, container, false);
        TextView pseudo = view.findViewById(R.id.text_Pseudo);
        TextView time = view.findViewById(R.id.text_Time);
        pseudo.setText(pseudonym);
        time.setText(this.time + " sec");
        return view;
    }
}