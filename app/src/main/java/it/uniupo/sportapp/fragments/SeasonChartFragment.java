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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.adapters.ChartAdapter;
import it.uniupo.sportapp.models.Player;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SeasonChartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "season";
    private static final String ARG_PARAM2 = "type";

    // TODO: Rename and change types of parameters
    private String seasonIndex;
    private String chartType;
    private ChartAdapter chartAdapter;


    public SeasonChartFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_season_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rvPlayers = view.findViewById(R.id.players_rv);
        chartAdapter = new ChartAdapter(getArrayListFromMap(), getContext(), chartType);
        rvPlayers.setAdapter(chartAdapter);
        rvPlayers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPlayers.setItemAnimator(new DefaultItemAnimator());
        rvPlayers.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    }

    private ArrayList<Player> getArrayListFromMap() {
        ArrayList<Player> stringArrayList = new ArrayList<>():
        Iterator it = Singleton.getCurrentSeason().getSeasonPlayerGoalsChart().entrySet().iterator();
        while(it.hasNext()){
            Log.d("IT", )
        }
        return stringArrayList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
