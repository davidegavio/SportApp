package it.uniupo.sportapp.fragments;

import android.app.AlertDialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
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
import it.uniupo.sportapp.models.Season;

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

    private TextView seasonNameTv;


    // TODO: Rename and change types of parameters
    private String mSeasonKey;
    private String mRoomKey;
    private String matchDate, matchTime;

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
                createNewMatch();
            }
        });
        registerListener();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void createNewMatch() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View itemview = inflater.inflate(R.layout.create_match_dialog, null);
        builder.setView(itemview)
                // Add action buttons
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final Match newMatch = new Match();
                        final TextView dateTv = itemview.findViewById(R.id.date_tv);
                        final TextView timeTv = itemview.findViewById(R.id.time_tv);
                        newMatch.setMatchDay(matchDate);
                        newMatch.setStartTime(matchTime);
                        dateTv.setText(newMatch.getMatchDay());
                        timeTv.setText(newMatch.getStartTime());
                        currentSeason.getSeasonMatches().add(newMatch);
                        Singleton.getCurrentRoom().getExistingSeasons().get(Integer.parseInt(mSeasonKey)).setSeasonMatches(currentSeason.getSeasonMatches());
                        mAdapter.notifyDataSetChanged();
                        Singleton.setCurrentMatch(newMatch);
                        Singleton.getCurrentMatch().setChatMessages(new ArrayList<ChatMessage>());
                        DatabaseReference mDatabase;
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        mDatabase.child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).child("existingSeasons").child(mSeasonKey).setValue(currentSeason);
                        MatchDetailFragment fragment = new MatchDetailFragment();
                        Bundle b = new Bundle();
                        b.putString("season", mSeasonKey);
                        b.putString("index", String.valueOf(Singleton.getCurrentSeason().getSeasonMatches().size()-1));
                        fragment.setArguments(b);
                        ((MainActivity)getActivity()).addFragment(fragment);

                    }
                })
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).create().show();
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

    private void registerListener(){
        BroadcastReceiver listener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("intent", intent.getAction());
                if(intent.getAction().equals("date_set")){
                    Log.d("date", intent.getStringExtra("date"));
                    matchDate = intent.getStringExtra("date");
                }
                else if(intent.getAction().equals("time_set")){
                    Log.d("time", intent.getStringExtra("time"));
                    matchTime = intent.getStringExtra("time");
                }
            }
        };
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(listener, new IntentFilter("date_set"));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(listener, new IntentFilter("time_set"));

    }

}
