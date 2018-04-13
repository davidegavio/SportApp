package it.uniupo.sportapp.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.adapters.GoalsAdapter;
import it.uniupo.sportapp.models.Player;
import it.uniupo.sportapp.models.Season;
import it.uniupo.sportapp.models.Team;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EditGoalsFragment extends Fragment{


    private Button saveGoalsButton;
    private String matchIndex, seasonIndex, roomIndex;
    private GoalsAdapter adapterA, adapterB;
    private int matchResult;


    public EditGoalsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            matchIndex = getArguments().getString("match");
            seasonIndex = getArguments().getString("season");
            matchResult = getArguments().getInt("result");
            roomIndex = getArguments().getString("room");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).child("existingSeasons").child(seasonIndex);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Singleton.setCurrentSeason(dataSnapshot.getValue(Season.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
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
        adapterA = new GoalsAdapter(Singleton.getCurrentMatch().getTeamA().getTeamPlayers(), matchResult, getContext());
        rvGoalsA.setAdapter(adapterA);
        rvGoalsA.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGoalsA.setItemAnimator(new DefaultItemAnimator());
        RecyclerView rvGoalsB = view.findViewById(R.id.goals_b_rv);
        adapterB = new GoalsAdapter(Singleton.getCurrentMatch().getTeamB().getTeamPlayers(), matchResult, getContext());
        rvGoalsB.setAdapter(adapterB);
        rvGoalsB.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGoalsB.setItemAnimator(new DefaultItemAnimator());
        saveGoalsButton = view.findViewById(R.id.button_save_goals);
        saveGoalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).child("existingSeasons").child(seasonIndex).child("seasonMatches").child(matchIndex).setValue(Singleton.getCurrentMatch());
                //ref.child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).child("existingSeasons").child(seasonIndex).setValue(Singleton.getCurrentSeason().getSeasonMatches().set(Integer.parseInt(matchIndex), Singleton.getCurrentMatch()));
                MatchDetailFragment fragment = new MatchDetailFragment();
                Bundle b = new Bundle();
                b.putString("pickers", "false");
                b.putString("season", seasonIndex);
                b.putString("match", matchIndex);
                b.putString("goal",  Singleton.getGoalsString());
                fragment.setArguments(b);
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
                Intent localIntent = new Intent("goals_set");
                localIntent.putExtra("goal", Singleton.getGoalsString());
                localBroadcastManager.sendBroadcast(localIntent);
                ((MainActivity)getActivity()).addFragment(fragment);

            }
        });
    }


}
