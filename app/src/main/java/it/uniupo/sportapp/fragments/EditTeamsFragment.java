package it.uniupo.sportapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.adapters.PlayersAdapter;
import it.uniupo.sportapp.adapters.TeamsAdapter;
import it.uniupo.sportapp.models.Player;

public class EditTeamsFragment extends Fragment {

    private Button saveTeamsButton;
    private String matchIndex, seasonIndex;
    private TeamsAdapter mAdapter;
    private ArrayList<Player> allPlayers;


    public EditTeamsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allPlayers = new ArrayList<>();
        if(getArguments()!=null){
            matchIndex = getArguments().getString("index");
            seasonIndex = getArguments().getString("season");
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(String k : Singleton.getCurrentRoom().getActivePlayers()){
                        Player p = dataSnapshot.child(k).getValue(Player.class);
                        allPlayers.add(p);
                        mAdapter.notifyDataSetChanged();
                    }
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
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_edit_teams, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rvPlayers = view.findViewById(R.id.players_rv);
        mAdapter = new TeamsAdapter(allPlayers, getContext());
        rvPlayers.setAdapter(mAdapter);
        rvPlayers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPlayers.setItemAnimator(new DefaultItemAnimator());
        saveTeamsButton = view.findViewById(R.id.button_save_teams);
        saveTeamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchDetailFragment fragment = new MatchDetailFragment();
                Bundle b = new Bundle();
                b.putString("season", seasonIndex);
                b.putString("index", matchIndex);
                fragment.setArguments(b);
                ((MainActivity)getActivity()).addFragment(fragment);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_profile:
                ((MainActivity)getActivity()).addFragment(new ProfileFragment());
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }


    /*


     */

}
