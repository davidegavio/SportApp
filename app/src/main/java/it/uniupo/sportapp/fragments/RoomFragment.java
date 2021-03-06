package it.uniupo.sportapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.Utility;
import it.uniupo.sportapp.adapters.PlayersAdapter;
import it.uniupo.sportapp.adapters.PlayersAddAdapter;
import it.uniupo.sportapp.models.Player;
import it.uniupo.sportapp.models.Room;
import it.uniupo.sportapp.models.Season;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class RoomFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_KEY = "key";
    private static final String ARG_INDEX = "index";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private int mParam2;
    private Player currentPlayer;

    //private OnFragmentInteractionListener mListener;

    public RoomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParam1 = getArguments().getString(ARG_KEY);
        mParam2 = getArguments().getInt(ARG_INDEX);
        Log.d("mParam1", "K: "+mParam1);
        Singleton.getCurrentRoom().setRoomKey(mParam1);
        Singleton.getCurrentPlayer().getPlayerRooms().set(mParam2, mParam1);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).setValue(Singleton.getCurrentRoom());
        ref.child("users").child(FirebaseAuth.getInstance().getUid()).child("playerRooms").child(String.valueOf(mParam2)).setValue(Singleton.getCurrentRoom().getRoomKey());
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("userToRestore", Singleton.getCurrentPlayer().getPlayerKey());
        editor.putString("fragmentSession", "room");
        editor.putString("roomKey", mParam1);
        editor.putString("roomIndex", String.valueOf(mParam2));
        editor.apply();
        Singleton.setCurrentFragment("room");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Button showSeasonsBtn = view.findViewById(R.id.show_seasons_btn);
        showSeasonsBtn.setOnClickListener(this);
        Button showActivePlayersBtn = view.findViewById(R.id.show_players_btn);
        showActivePlayersBtn.setOnClickListener(this);
        FloatingActionButton addPlayerFab = view.findViewById(R.id.add_player_btn);
        Log.d("Admins", String.valueOf(Singleton.getCurrentRoom().getAdminPlayers()));
        Log.d("Current", String.valueOf(Singleton.getCurrentPlayer()));
        if(Utility.checkIfAdmin())
            addPlayerFab.setOnClickListener(this);
        FloatingActionButton addSeasonFab = view.findViewById(R.id.add_season_btn);
        addSeasonFab.setOnClickListener(this);
        if(Singleton.getCurrentRoom().getExistingSeasons() == null)
            Singleton.getCurrentRoom().setExistingSeasons(new ArrayList<Season>());
        TextView roomNameTv = view.findViewById(R.id.room_name_tv);
        roomNameTv.setText(Singleton.getCurrentRoom().getRoomName());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_player_btn:
                Log.d("Add", "Add player");
                ((MainActivity)getActivity()).addFragment(new AddPlayerListFragment());
                break;
            case R.id.add_season_btn:
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Get the layout inflater
                LayoutInflater inflater = getActivity().getLayoutInflater();
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                final View editview = inflater.inflate(R.layout.create_season_dialog, null);
                builder.setView(editview)
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText newSeasonName = editview.findViewById(R.id.season_name_dialog);
                                Season newSeason = new Season(String.valueOf(newSeasonName.getText()));
                                Calendar c = Calendar.getInstance();
                                System.out.println("Current time => " + c.getTime());
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                String formattedDate = df.format(c.getTime());
                                newSeason.setSeasonBeginningDate(formattedDate);
                                newSeason.setSeasonPlayerGoalsChart(new HashMap<String, String>());
                                newSeason.setSeasonPlayerPresencesChart(new HashMap<String, String>());
                                setPlayersGoalsPresences(newSeason);
                            }
                        }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
            case R.id.show_seasons_btn:
                SeasonListFragment fragment = new SeasonListFragment();
                Bundle c = new Bundle();
                c.putString("key", String.valueOf(mParam2));
                fragment.setArguments(c);
                ((MainActivity)getActivity()).addFragment(fragment);
                break;
            case R.id.show_players_btn:
                ((MainActivity)getActivity()).addFragment(new PlayerListFragment());
                break;
        }
    }

    private void setPlayersGoalsPresences(Season season) {
        final Season editedSeason = season;
        FirebaseDatabase.getInstance().getReference().child("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(String s : Singleton.getCurrentRoom().getActivePlayers()){
                            Player p = dataSnapshot.child(s).getValue(Player.class);
                            editedSeason.getSeasonPlayerGoalsChart().put(p.getPlayerKey(), "0");
                            editedSeason.getSeasonPlayerPresencesChart().put(p.getPlayerKey(), "0");
                        }
                        Singleton.setCurrentSeason(editedSeason);
                        Singleton.getCurrentRoom().getExistingSeasons().add(Singleton.getCurrentSeason());
                        DatabaseReference mDatabase;
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        mDatabase.child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).setValue(Singleton.getCurrentRoom());
                        SeasonDetailFragment fragment = new SeasonDetailFragment();
                        Bundle args = new Bundle();
                        args.putString("season", String.valueOf(Singleton.getCurrentRoom().getExistingSeasons().size()-1));
                        args.putString("key", String.valueOf(mParam2));
                        fragment.setArguments(args);
                        ((MainActivity)getActivity()).addFragment(fragment);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
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

