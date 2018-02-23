package it.uniupo.sportapp.fragments;


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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.adapters.GoalsChartAdapter;
import it.uniupo.sportapp.adapters.PresencesChartAdapter;
import it.uniupo.sportapp.models.Match;
import it.uniupo.sportapp.models.Player;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeasonPresencesChartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "season";
    private static final String ARG_PARAM2 = "key";

    // TODO: Rename and change types of parameters
    private String seasonIndex, roomIndex;
    private PresencesChartAdapter presencesChartAdapter;


    public SeasonPresencesChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            seasonIndex = getArguments().getString(ARG_PARAM1);
            roomIndex = getArguments().getString(ARG_PARAM2);
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
        presencesChartAdapter = new PresencesChartAdapter(getArrayListFromMap(), getContext());
        rvPlayers.setAdapter(presencesChartAdapter);
        rvPlayers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPlayers.setItemAnimator(new DefaultItemAnimator());
        rvPlayers.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    }

    private ArrayList<String> getArrayListFromMap() {
        ArrayList<String> stringArrayList = new ArrayList<>();
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for(Map.Entry<String, String> entry : Singleton.getCurrentSeason().getSeasonPlayerPresencesChart().entrySet()) {
            integerArrayList.add(Integer.parseInt(entry.getValue()));
        }
        Collections.sort(integerArrayList);
        Collections.reverse(integerArrayList);
        for(int i : integerArrayList) {
            for(Map.Entry<String, String> entry : Singleton.getCurrentSeason().getSeasonPlayerPresencesChart().entrySet()) {
                if(i==(Integer.parseInt(entry.getValue()))&&!stringArrayList.contains(entry.getKey() + "-" + entry.getValue())) {
                    stringArrayList.add(entry.getKey() + "-" + entry.getValue());
                }
            }
        }
        Log.d("Presences", String.valueOf(stringArrayList));
        return stringArrayList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
