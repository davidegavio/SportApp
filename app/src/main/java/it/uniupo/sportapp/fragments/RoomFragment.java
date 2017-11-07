package it.uniupo.sportapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.models.Player;
import it.uniupo.sportapp.models.Room;
import it.uniupo.sportapp.models.Season;

import static android.content.ContentValues.TAG;

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
    private String mParam2;
    private Player currentPlayer;

    //private OnFragmentInteractionListener mListener;

    public RoomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParam1 = getArguments().getString(ARG_KEY);
        mParam2 = getArguments().getString(ARG_INDEX);
        Log.d("mParam1", mParam1);
        Singleton.getCurrentRoom().setRoomKey(mParam1);
        Singleton.getCurrentPlayer().getPlayerRooms().get(Integer.parseInt(mParam2)).setRoomKey(mParam1);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).setValue(Singleton.getCurrentRoom());
        ref.child("players").child(FirebaseAuth.getInstance().getUid()).child("playerRooms").child(mParam2).setValue(Singleton.getCurrentRoom());
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
        FloatingActionButton addPlayerFab = view.findViewById(R.id.add_player_btn);
        Log.d("Admins", String.valueOf(Singleton.getCurrentRoom().getAdminPlayers()));
        Log.d("Current", String.valueOf(Singleton.getCurrentPlayer()));
        if(checkAdmin())
            addPlayerFab.setOnClickListener(this);
        FloatingActionButton addSeasonFab = view.findViewById(R.id.add_season_btn);
        addSeasonFab.setOnClickListener(this);
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
                ((MainActivity)getActivity()).addFragment(new PlayerListFragment());
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
                                //Log.d(TAG, String.valueOf(newSeasonName.getText()));
                                Season newSeason = new Season(String.valueOf(newSeasonName.getText()));
                                Calendar c = Calendar.getInstance();
                                System.out.println("Current time => " + c.getTime());
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                String formattedDate = df.format(c.getTime());
                                newSeason.setSeasonBeginningDate(formattedDate);
                                Singleton.getCurrentRoom().getExistingSeasons().add(newSeason);
                                DatabaseReference mDatabase;
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("rooms").child(mParam1).setValue(Singleton.getCurrentRoom());
                                mDatabase.child("users").child(FirebaseAuth.getInstance().getUid()).child("playerRooms").child(mParam2).setValue(Singleton.getCurrentRoom());
                                SeasonDetailFragment fragment = new SeasonDetailFragment();
                                Bundle args = new Bundle();
                                args.putString(ARG_KEY, String.valueOf(Singleton.getCurrentRoom().getExistingSeasons().size()));
                                args.putString("room", mParam1);
                                fragment.setArguments(args);
                                ((MainActivity)getActivity()).addFragment(fragment);

                            }
                        }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).create().show();

                Log.d("Add", "Add season");
                break;
            case R.id.show_seasons_btn:
                ((MainActivity)getActivity()).addFragment(new SeasonListFragment());
                break;
            case R.id.show_players_btn:

                break;
        }
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

    private boolean checkAdmin(){
        for(Player p : Singleton.getCurrentRoom().getAdminPlayers())
            if(p.getPlayerMail().equals(Singleton.getCurrentPlayer().getPlayerMail()))
                return true;
        return false;
    }

}

