package it.uniupo.sportapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.adapters.AverageChartAdapter;
import it.uniupo.sportapp.adapters.GoalsChartAdapter;
import it.uniupo.sportapp.models.Match;
import it.uniupo.sportapp.models.Player;

public class SeasonAverageChartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "season";
    private static final String ARG_PARAM2 = "type";
    AverageChartAdapter averageChartAdapter;

    // TODO: Rename and change types of parameters
    private String seasonIndex, roomKey;
    private String chartType;
    private GoalsChartAdapter goalsChartAdapter;
    private ArrayList<String> averageArrayList;

    public SeasonAverageChartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            seasonIndex = getArguments().getString(ARG_PARAM1);
            chartType = getArguments().getString(ARG_PARAM2);
            roomKey = getArguments().getString("room");
        }
        Singleton.setCurrentFragment("chart");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_season_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rvPlayers = view.findViewById(R.id.players_rv);
        averageArrayList = new ArrayList<>();
        averageChartAdapter = new AverageChartAdapter(averageArrayList, getContext());
        rvPlayers.setAdapter(averageChartAdapter);
        rvPlayers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPlayers.setItemAnimator(new DefaultItemAnimator());
        rvPlayers.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        createGoalsChart();
    }

    private void createGoalsChart() {
        Singleton.getCurrentSeason().setSeasonPlayerPresencesChart(new HashMap<String, String>());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomKey).child("existingSeasons").child(seasonIndex).child("seasonMatches");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Match match = dataSnapshot1.getValue(Match.class);
                    if(match.getPlayerGoals()!=null) {
                        for (Map.Entry<String, String> entry : match.getPlayerGoals().entrySet()) {
                            int n = 0;
                            //n = Integer.parseInt(Singleton.getCurrentSeason().getSeasonPlayerGoalsChart().get(entry.getKey()));
                            n = n + Integer.parseInt(entry.getValue());
                            Singleton.getCurrentSeason().getSeasonPlayerGoalsChart().put(entry.getKey(), String.valueOf(n));
                        }
                        createPresencesChart();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void createPresencesChart() {
        Singleton.getCurrentSeason().setSeasonPlayerPresencesChart(new HashMap<String, String>());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomKey).child("existingSeasons").child(seasonIndex).child("seasonMatches");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Match match = dataSnapshot1.getValue(Match.class);
                    for(Player player : match.getTeamA().getTeamPlayers()){
                        if(Singleton.getCurrentSeason().getSeasonPlayerPresencesChart().get(player.getPlayerKey())==null)
                            Singleton.getCurrentSeason().getSeasonPlayerPresencesChart().put(player.getPlayerKey(), "1");
                        else{
                            int n= Integer.parseInt(Singleton.getCurrentSeason().getSeasonPlayerPresencesChart().get(player.getPlayerKey()));
                            n=n+1;
                            Singleton.getCurrentSeason().getSeasonPlayerPresencesChart().put(player.getPlayerKey(), String.valueOf(n));
                        }
                    }
                    for(Player player : match.getTeamB().getTeamPlayers()){
                        if(Singleton.getCurrentSeason().getSeasonPlayerPresencesChart().get(player.getPlayerKey())==null)
                            Singleton.getCurrentSeason().getSeasonPlayerPresencesChart().put(player.getPlayerKey(), "1");
                        else{
                            int n= Integer.parseInt(Singleton.getCurrentSeason().getSeasonPlayerPresencesChart().get(player.getPlayerKey()));
                            n=n+1;
                            Singleton.getCurrentSeason().getSeasonPlayerPresencesChart().put(player.getPlayerKey(), String.valueOf(n));
                        }
                    }
                }
                getArrayListFromMap();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getArrayListFromMap() {
        //goal/partite
        ArrayList<String> stringArrayList = new ArrayList<>();
        ArrayList<Integer> integerArrayList = new ArrayList<>();

        for(Map.Entry<String, String> entry : Singleton.getCurrentSeason().getSeasonPlayerGoalsChart().entrySet()) {
            float n = Float.parseFloat(Singleton.getCurrentSeason().getSeasonPlayerPresencesChart().get(entry.getKey()).replace(",","."));
            float l = Float.parseFloat(entry.getValue().replace(",","."));
            if(l!=0 && n!=0) {
                Log.d("l/n", String.valueOf(l/n));
                averageArrayList.add(entry.getKey() + "-" + String.format("%.2f", (l/n)));
            }
            else{
                averageArrayList.add(entry.getKey() + "-0");
            }
        }
        Collections.sort(averageArrayList, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                if (Float.parseFloat(s.split("-")[1].replace(",", "."))< Float.parseFloat(t1.split("-")[1].replace(",", ".")))
                    return -1;
                if (Float.parseFloat(s.split("-")[1].replace(",", "."))> Float.parseFloat(t1.split("-")[1].replace(",", ".")))
                    return 1;
                return 0;
            }
        });
        Collections.reverse(averageArrayList);
        Log.d("Average", String.valueOf(averageArrayList));
        averageChartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
