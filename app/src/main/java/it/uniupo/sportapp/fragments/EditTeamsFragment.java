package it.uniupo.sportapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.adapters.TeamsAdapter;
import it.uniupo.sportapp.models.ChatMessage;
import it.uniupo.sportapp.models.Match;
import it.uniupo.sportapp.models.Player;
import it.uniupo.sportapp.models.Team;

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
        Team team = Singleton.getCurrentMatch().getTeamA();
        allPlayers = new ArrayList<>();
        if(getArguments()!=null){
            matchIndex = getArguments().getString("match");
            seasonIndex = getArguments().getString("season");
            Singleton.setCurrentSeason(Singleton.getCurrentRoom().getExistingSeasons().get(Integer.parseInt(seasonIndex)));
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(String k : Singleton.getCurrentRoom().getActivePlayers()){
                        Player p = dataSnapshot.child(k).getValue(Player.class);
                        allPlayers.add(p);
                        mAdapter.notifyDataSetChanged();
                        Team team = Singleton.getCurrentMatch().getTeamA();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Singleton.getCurrentMatch().setTeamA(new Team());
            Singleton.getCurrentMatch().getTeamA().setTeamPlayers(new ArrayList<Player>());
            Singleton.getCurrentMatch().setTeamB(new Team());
            Singleton.getCurrentMatch().getTeamB().setTeamPlayers(new ArrayList<Player>());
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
        mAdapter = new TeamsAdapter(allPlayers, getContext(), seasonIndex, matchIndex);
        rvPlayers.setAdapter(mAdapter);
        rvPlayers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPlayers.setItemAnimator(new DefaultItemAnimator());
        saveTeamsButton = view.findViewById(R.id.button_save_teams);
        saveTeamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Team team = Singleton.getCurrentMatch().getTeamA();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ArrayList<Player> temp = new ArrayList<>();
                temp.addAll(Singleton.getCurrentMatch().getTeamA().getTeamPlayers());
                temp.addAll(Singleton.getCurrentMatch().getTeamB().getTeamPlayers());
                ref.child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).child("existingSeasons").child(seasonIndex).child("seasonMatches").child(matchIndex).setValue(Singleton.getCurrentMatch());
                Singleton.getCurrentSeason().getSeasonMatches().set(Integer.parseInt(matchIndex), Singleton.getCurrentMatch());
                //ref.child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).child("existingSeasons").child(seasonIndex).setValue(Singleton.getCurrentSeason());
                String convocation = "Convocazioni:\n";
                for(Player p : temp)
                    convocation += p.getPlayerName()+"\n";
                ChatMessage chatMessage = new ChatMessage(convocation.trim(), Singleton.getCurrentPlayer().getPlayerKey(), Singleton.getCurrentPlayer().getPlayerName(), Singleton.getCurrentPlayer().getPlayerImageUid());
                Singleton.getCurrentMatch().getChatMessages().add(chatMessage);
                ref.child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).child("existingSeasons").child(seasonIndex).child("seasonMatches").child(matchIndex).child("chatMessages").child(String.valueOf(Singleton.getCurrentMatch().getChatMessages().size()-1)).setValue(chatMessage).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        MatchDetailFragment fragment = new MatchDetailFragment();
                        Bundle b = new Bundle();
                        b.putString("pickers", "false");
                        b.putString("season", seasonIndex);
                        b.putString("match", matchIndex);
                        fragment.setArguments(b);
                        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
                        Intent localIntent = new Intent("teams_set");
                        localBroadcastManager.sendBroadcast(localIntent);
                        localIntent = new Intent("message");
                        localIntent.putExtra("message", "Teams set! Check them out");
                        localBroadcastManager.sendBroadcast(localIntent);
                        ((MainActivity)getActivity()).addFragment(fragment);
                    }
                });
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


}
