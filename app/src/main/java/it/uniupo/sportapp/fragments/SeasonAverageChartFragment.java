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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.adapters.AverageChartAdapter;

public class SeasonAverageChartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "season";
    private static final String ARG_PARAM2 = "type";
    AverageChartAdapter averageChartAdapter;

    // TODO: Rename and change types of parameters
    private String seasonIndex;
    private String chartType;

    public SeasonAverageChartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            seasonIndex = getArguments().getString(ARG_PARAM1);
            chartType = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rvPlayers = view.findViewById(R.id.players_rv);
        averageChartAdapter = new AverageChartAdapter(getArrayListFromMap(), getContext());
        rvPlayers.setAdapter(averageChartAdapter);
        rvPlayers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPlayers.setItemAnimator(new DefaultItemAnimator());
        rvPlayers.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    }

    private ArrayList<String> getArrayListFromMap() {
        //goal/partite
        ArrayList<String> stringArrayList = new ArrayList<>();
        ArrayList<Integer> integerArrayList = new ArrayList<>();
        for(Map.Entry<String, String> entry : Singleton.getCurrentSeason().getSeasonPlayerGoalsChart().entrySet()) {
            integerArrayList.add(Integer.parseInt(entry.getValue())/Integer.parseInt(Singleton.getCurrentSeason().getSeasonPlayerPresencesChart().get(entry.getKey())));
        }
        Collections.sort(integerArrayList);
        Collections.reverse(integerArrayList);
        for(int i : integerArrayList) {
            for(Map.Entry<String, String> entry : Singleton.getCurrentSeason().getSeasonPlayerGoalsChart().entrySet()) {
                if(i==(Integer.parseInt(entry.getValue()))&&!stringArrayList.contains(entry.getKey() + "-" + entry.getValue())) {
                    stringArrayList.add(entry.getKey() + "-" + entry.getValue());
                }
            }
        }
        Log.d("Average", String.valueOf(stringArrayList));
        return stringArrayList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
