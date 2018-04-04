package it.uniupo.sportapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.adapters.GoalsChartAdapter;
import it.uniupo.sportapp.models.Match;
import it.uniupo.sportapp.models.Player;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SeasonGoalsChartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "season";
    private static final String ARG_PARAM2 = "type";

    // TODO: Rename and change types of parameters
    private String seasonIndex, roomIndex;
    private String chartType;
    private GoalsChartAdapter goalsChartAdapter;
    private ArrayList<String> goalArrayList;


    public SeasonGoalsChartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            seasonIndex = getArguments().getString(ARG_PARAM1);
            roomIndex = getArguments().getString("key");
            chartType = getArguments().getString(ARG_PARAM2);
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
        goalArrayList = new ArrayList<>();
        RecyclerView rvPlayers = view.findViewById(R.id.players_rv);
        goalsChartAdapter = new GoalsChartAdapter(goalArrayList, getContext());
        rvPlayers.setAdapter(goalsChartAdapter);
        rvPlayers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPlayers.setItemAnimator(new DefaultItemAnimator());
        rvPlayers.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        createChart();
    }

    private void createChart() {
        Singleton.getCurrentSeason().setSeasonPlayerPresencesChart(new HashMap<String, String>());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("rooms").child(roomIndex).child("existingSeasons").child(seasonIndex).child("seasonMatches");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Match match = dataSnapshot1.getValue(Match.class);
                    for(Map.Entry<String, String> entry : match.getPlayerGoals().entrySet()){
                        int n = 0;
                        n = Integer.parseInt(Singleton.getCurrentSeason().getSeasonPlayerGoalsChart().get(entry.getKey()));
                        n = Integer.parseInt(n + entry.getValue());
                        Singleton.getCurrentSeason().getSeasonPlayerGoalsChart().put(entry.getKey(), String.valueOf(n));
                    }
                }
                getArrayListFromMap();
                goalsChartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        goalsChartAdapter.notifyDataSetChanged();
    }

    private void getArrayListFromMap() {
        ArrayList<String> stringArrayList = new ArrayList<>();
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for(Map.Entry<String, String> entry : Singleton.getCurrentSeason().getSeasonPlayerGoalsChart().entrySet()) {
            integerArrayList.add(Integer.parseInt(entry.getValue()));
        }
        Collections.sort(integerArrayList);
        Collections.reverse(integerArrayList);
        for(int i : integerArrayList) {
            for(Map.Entry<String, String> entry : Singleton.getCurrentSeason().getSeasonPlayerGoalsChart().entrySet()) {
                if(i==(Integer.parseInt(entry.getValue()))&&!goalArrayList.contains(entry.getKey() + "-" + entry.getValue())) {
                    goalArrayList.add(entry.getKey() + "-" + entry.getValue());
                }
            }
        }
        Log.d("Goals", String.valueOf(goalArrayList));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
