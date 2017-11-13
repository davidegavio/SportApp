package it.uniupo.sportapp.fragments;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.adapters.MatchesAdapter;
import it.uniupo.sportapp.adapters.SeasonsAdapter;
import it.uniupo.sportapp.models.Match;
import it.uniupo.sportapp.models.Season;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class SeasonDetailFragment extends Fragment {

    private static final String ARG_KEY = "key";
    private static final String ARG_ROOM = "room";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MatchesAdapter mAdapter;

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
        Log.d("onCreate", "Here");
        if (getArguments() != null) {
            mSeasonKey = getArguments().getString(ARG_KEY);
            mRoomKey = getArguments().getString(ARG_ROOM);
            currentSeason = Singleton.getCurrentRoom().getExistingSeasons().get(Integer.parseInt(mSeasonKey)-1);
            currentSeason.setSeasonMatches(new ArrayList<Match>());
            mAdapter = new MatchesAdapter(currentSeason.getSeasonMatches(), getContext());
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
        final View timeview = inflater.inflate(R.layout.create_match_dialog, null);
        builder.setView(timeview)
                // Add action buttons
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Match newMatch = new Match();
                        currentSeason.getSeasonMatches().add(newMatch);
                        mAdapter.notifyDataSetChanged();
                        DatabaseReference mDatabase;
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        mDatabase.child("rooms").child(mRoomKey).child("existingSeasons").child(String.valueOf(currentSeason.getSeasonMatches().size())).setValue(currentSeason);
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

}
