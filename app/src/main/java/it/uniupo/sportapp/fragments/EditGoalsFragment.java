package it.uniupo.sportapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.adapters.GoalsAdapter;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EditGoalsFragment extends Fragment {


    private Button saveGoalsButton;
    private String matchIndex, seasonIndex;
    private GoalsAdapter adapterA, adapterB;


    public EditGoalsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            matchIndex = getArguments().getString("index");
            seasonIndex = getArguments().getString("season");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_goals, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rvGoalsA = view.findViewById(R.id.goals_a_rv);
        adapterA = new GoalsAdapter(Singleton.getCurrentMatch().getTeamA().getTeamPlayers(), getContext());
        rvGoalsA.setAdapter(adapterA);
        rvGoalsA.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGoalsA.setItemAnimator(new DefaultItemAnimator());
        RecyclerView rvGoalsB = view.findViewById(R.id.goals_b_rv);
        adapterB = new GoalsAdapter(Singleton.getCurrentMatch().getTeamB().getTeamPlayers(), getContext());
        rvGoalsB.setAdapter(adapterB);
        rvGoalsB.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGoalsB.setItemAnimator(new DefaultItemAnimator());
        saveGoalsButton = view.findViewById(R.id.button_save_goals);
        saveGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).child("existingSeasons").child(seasonIndex).setValue(Singleton.getCurrentSeason());
                MatchDetailFragment fragment = new MatchDetailFragment();
                Bundle b = new Bundle();
                b.putString("season", seasonIndex);
                b.putString("index", matchIndex);
                fragment.setArguments(b);
                ((MainActivity)getActivity()).addFragment(fragment);
            }
        });
    }
}