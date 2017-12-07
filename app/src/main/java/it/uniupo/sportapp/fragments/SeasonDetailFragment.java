package it.uniupo.sportapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.adapters.MatchesAdapter;
import it.uniupo.sportapp.models.ChatMessage;
import it.uniupo.sportapp.models.Match;
import it.uniupo.sportapp.models.Player;
import it.uniupo.sportapp.models.Season;
import it.uniupo.sportapp.models.Team;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class SeasonDetailFragment extends android.support.v4.app.Fragment {

    private static final String ARG_KEY = "season";
    private static final String ARG_ROOM = "room";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MatchesAdapter mAdapter;
    private ArrayList<Player> seasonPlayers;

    private TextView seasonNameTv;


    // TODO: Rename and change types of parameters
    private String mSeasonKey;
    private String mRoomKey;


    Season currentSeason;

    //private OnFragmentInteractionListener mListener;

    public SeasonDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSeasonKey = getArguments().getString(ARG_KEY);
            mRoomKey = getArguments().getString(ARG_ROOM);
            currentSeason = Singleton.getCurrentRoom().getExistingSeasons().get(Integer.parseInt(mSeasonKey));
            currentSeason.setSeasonMatches(new ArrayList<Match>());
            Log.i("onCreateSeason", "Here");
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).child("existingSeasons").child(mSeasonKey).child("seasonMatches");
            Log.i("onCreate", Singleton.getCurrentRoom().getRoomKey());
            Log.i("onCreate", mSeasonKey);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                        for(DataSnapshot d : dataSnapshot.getChildren()){
                            currentSeason.getSeasonMatches().add(d.getValue(Match.class));
                            mAdapter.notifyDataSetChanged();
                            Log.i("onDataChange",currentSeason.getSeasonMatches().toString());
                            Singleton.setCurrentSeason(currentSeason);
                        }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mAdapter = new MatchesAdapter(currentSeason.getSeasonMatches(), mSeasonKey, getContext());
            Singleton.setCurrentSeason(currentSeason);
            Singleton.setCurrentFragment("seasonDetailed");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_season_detail, container, false);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d("onViewCreated", "Here");
        seasonNameTv = view.findViewById(R.id.app_bar_tv);
        seasonNameTv.setText(currentSeason.getSeasonName());
        // Inflate the layout for this fragment
        mRecyclerView = (RecyclerView) view.findViewById(R.id.season_match_rv);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);


        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createNewMatch();
                Match newMatch = new Match();
                currentSeason.getSeasonMatches().add(newMatch);
                Singleton.getCurrentRoom().getExistingSeasons().get(Integer.parseInt(mSeasonKey)).setSeasonMatches(currentSeason.getSeasonMatches());
                mAdapter.notifyDataSetChanged();
                Singleton.setCurrentMatch(newMatch);
                Singleton.getCurrentMatch().setChatMessages(new ArrayList<ChatMessage>());
                Singleton.getCurrentMatch().setTeamA(new Team("Team A", new ArrayList<Player>()));
                Singleton.getCurrentMatch().setTeamB(new Team("Team B", new ArrayList<Player>()));
                Singleton.setCurrentSeason(currentSeason);
                MatchDetailFragment fragment = new MatchDetailFragment();
                Bundle b = new Bundle();
                b.putString("season", mSeasonKey);
                b.putString("index", String.valueOf(Singleton.getCurrentSeason().getSeasonMatches().size()-1));
                fragment.setArguments(b);
                ((MainActivity)getActivity()).addFragment(fragment);
            }
        });

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.season_charts_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_profile:
                ((MainActivity)getActivity()).addFragment(new ProfileFragment());
                return true;
            case R.id.action_goals_chart:
                SeasonChartFragment seasonChartFragment = new SeasonChartFragment();
                Bundle b = new Bundle();
                b.putString("season", mSeasonKey);
                b.putString("type", "goals");
                seasonChartFragment.setArguments(b);
                ((MainActivity)getActivity()).addFragment(seasonChartFragment);
                return true;
            case R.id.action_presences_chart:
                return true;
            case R.id.action_average_chart:
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }



}
