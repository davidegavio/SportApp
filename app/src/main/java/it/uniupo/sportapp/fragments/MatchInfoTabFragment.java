package it.uniupo.sportapp.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.Utility;
import it.uniupo.sportapp.adapters.TeamsMatchInfoAdapter;
import it.uniupo.sportapp.models.ChatMessage;
import it.uniupo.sportapp.models.Match;
import it.uniupo.sportapp.models.Player;
import it.uniupo.sportapp.models.Season;
import it.uniupo.sportapp.models.Team;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MatchInfoTabFragment extends Fragment implements Button.OnClickListener{

    private static final String ARG_PARAM1 = "index";
    private static final String ARG_PARAM2 = "season";

    private String matchDate, matchTime, matchIndex, seasonIndex, goalsList;
    private Button editTeamsButton, editResultButton, editGoalsButton;
    private TextView homeResultTextView, awayResultTextView, matchDayTextView, matchHourTextView, emptyView, goalsTextView;
    private RecyclerView teamARecyclerView, teamBRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TeamsMatchInfoAdapter teamAPlayers, teamBPlayers;
    private int matchResult;
    private String roomIndex;

    @Override
    public void onResume() {
        super.onResume();
    }

    public MatchInfoTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            matchIndex = getArguments().getString(ARG_PARAM1);
            seasonIndex = getArguments().getString(ARG_PARAM2);
            roomIndex = getArguments().getString("index");
            goalsList = getArguments().getString("goal");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match_info_tab, container, false);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        registerListener();
        if(Singleton.getCurrentMatch().getTeamA()!=null&&Singleton.getCurrentMatch().getTeamB()!=null) {
            if (Singleton.getCurrentMatch().getTeamA().getTeamPlayers() == null && Singleton.getCurrentMatch().getTeamB().getTeamPlayers() == null){
                Singleton.getCurrentMatch().getTeamA().setTeamPlayers(new ArrayList<Player>());
                Singleton.getCurrentMatch().getTeamB().setTeamPlayers(new ArrayList<Player>());
            }
            teamAPlayers = new TeamsMatchInfoAdapter(Singleton.getCurrentMatch().getTeamA().getTeamPlayers(), getContext());
            teamBPlayers = new TeamsMatchInfoAdapter(Singleton.getCurrentMatch().getTeamB().getTeamPlayers(), getContext());
        }
        teamARecyclerView = view.findViewById(R.id.team_a_rv);
        teamARecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        teamARecyclerView.setLayoutManager(mLayoutManager);
        teamARecyclerView.setItemAnimator(new DefaultItemAnimator());
        teamBRecyclerView = view.findViewById(R.id.team_b_rv);
        teamBRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        teamBRecyclerView.setLayoutManager(mLayoutManager);
        teamBRecyclerView.setItemAnimator(new DefaultItemAnimator());
        emptyView = view.findViewById(R.id.empty_view);
        teamARecyclerView.setAdapter(teamAPlayers);
        teamBRecyclerView.setAdapter(teamBPlayers);
        matchDayTextView = view.findViewById(R.id.match_day_tv);
        matchHourTextView = view.findViewById(R.id.match_hour_tv);
        homeResultTextView = view.findViewById(R.id.result_home);
        awayResultTextView = view.findViewById(R.id.result_away);
        goalsTextView = view.findViewById(R.id.goals_list);
        goalsTextView.setText(goalsList);
        editTeamsButton = view.findViewById(R.id.edit_teams_btn);
        editResultButton = view.findViewById(R.id.edit_result_btn);
        editGoalsButton = view.findViewById(R.id.edit_goals_btn);
        if (!Utility.checkIfAdmin()) {
            editTeamsButton.setVisibility(View.INVISIBLE);
            editResultButton.setVisibility(View.INVISIBLE);
            editGoalsButton.setVisibility(View.INVISIBLE);
        }else {
            editTeamsButton.setOnClickListener(this);
            editResultButton.setOnClickListener(this);
            editGoalsButton.setOnClickListener(this);
        }
        if(Singleton.getCurrentMatch().getMatchDay()==null && Singleton.getCurrentMatch().getStartTime()==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("You are invited to choose day and time of your match in the dialogs that will be displayed")
                    .setTitle("Warning!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ((MainActivity)getActivity()).showDatePickerDialog();
                            dialogInterface.dismiss();
                        }
                    });
            builder.create().show();
        }else {
            matchDayTextView.setText(Singleton.getCurrentMatch().getMatchDay());
            matchHourTextView.setText(Singleton.getCurrentMatch().getStartTime());
            if(Singleton.getCurrentMatch().getMatchResult()!=null) {
                String[] r = Singleton.getCurrentMatch().getMatchResult().split("-");
                homeResultTextView.setText(r[0]);
                awayResultTextView.setText(r[1]);
                matchResult = Integer.parseInt(String.valueOf(homeResultTextView.getText())) + Integer.parseInt(String.valueOf(awayResultTextView.getText()));
                Log.d("result:", String.valueOf(matchResult));
            }
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_teams_btn:
                EditTeamsFragment editTeamsFragment = new EditTeamsFragment();
                Bundle b = new Bundle();
                b.putString("index", matchIndex);
                b.putString("season", seasonIndex);
                editTeamsFragment.setArguments(b);
                ((MainActivity)getActivity()).addFragment(editTeamsFragment);
                Toast.makeText(getContext(), "Edit teams!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_result_btn:
                editResult();
                matchResult = Integer.parseInt(String.valueOf(homeResultTextView.getText())) + Integer.parseInt(String.valueOf(awayResultTextView.getText()));
                Log.d("result:", String.valueOf(matchResult));
                Toast.makeText(getContext(), "Edit result!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_goals_btn:
                EditGoalsFragment editGoalsFragment = new EditGoalsFragment();
                b = new Bundle();
                b.putInt("result", matchResult);
                b.putString("index", matchIndex);
                b.putString("season", seasonIndex);
                editGoalsFragment.setArguments(b);
                ((MainActivity)getActivity()).addFragment(editGoalsFragment);
                Toast.makeText(getContext(), "Edit goals!", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private void editResult() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View itemview = inflater.inflate(R.layout.edit_result_dialog, null);
        builder.setView(itemview)
                // Add action buttons
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final EditText homeEt = itemview.findViewById(R.id.home_result_dialog);
                        final EditText awayEt = itemview.findViewById(R.id.away_result_dialog);
                        homeResultTextView.setText(homeEt.getText());
                        awayResultTextView.setText(awayEt.getText());
                        Singleton.getCurrentMatch().setMatchResult(homeResultTextView.getText()+"-"+awayResultTextView.getText());
                        DatabaseReference mDatabase;
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        mDatabase.child("rooms").child(roomIndex).child(Singleton.getCurrentRoom().getRoomKey()).child("existingSeasons").child(seasonIndex).child("seasonMatches").child(matchIndex).setValue(Singleton.getCurrentMatch());
                    }
                })
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private void registerListener(){
        BroadcastReceiver listener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("intent", intent.getAction());
                if(intent.getAction().equals("date_set")){
                    Log.d("date", intent.getStringExtra("date"));
                    matchDate = intent.getStringExtra("date");
                    matchDayTextView.setText(matchDate);
                    ((MainActivity)getActivity()).showTimePickerDialog();
                }
                else if(intent.getAction().equals("time_set")){
                    Log.d("time", intent.getStringExtra("time"));
                    matchTime = intent.getStringExtra("time");
                    matchHourTextView.setText(matchTime);
                    createMatch();
                }
                else if(intent.getAction().equals("teams_set")){
                    Log.d("TP", Singleton.getCurrentMatch().getTeamA().getTeamPlayers().toString());
                    emptyView.setVisibility(View.INVISIBLE);
                    teamARecyclerView.setVisibility(View.VISIBLE);
                    teamBRecyclerView.setVisibility(View.VISIBLE);
                    teamAPlayers.notifyDataSetChanged();
                    teamBPlayers.notifyDataSetChanged();
                }
                else if(intent.getAction().equals("goals_set")){
                    Log.d("int", intent.getStringExtra("goal"));
                    goalsTextView.setText(intent.getStringExtra("goal"));
                    Log.d("tv", goalsTextView.getText().toString());
                }
            }
        };
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(listener, new IntentFilter("date_set"));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(listener, new IntentFilter("time_set"));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(listener, new IntentFilter("teams_set"));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(listener, new IntentFilter("goals_set"));

    }

    private void createMatch() {
        Singleton.getCurrentMatch().setMatchDay(matchDate);
        Singleton.getCurrentMatch().setStartTime(matchTime);
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("rooms").child(roomIndex).child("existingSeasons").child(seasonIndex).child("seasonMatches").child(matchIndex).setValue(Singleton.getCurrentMatch());

    }




}
