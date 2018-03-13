package it.uniupo.sportapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.adapters.PlayersAddAdapter;
import it.uniupo.sportapp.models.Player;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class AddPlayerListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_VALUE = "value";

    private ArrayList<Player> allPlayers,availablePlayers;
    private PlayersAddAdapter mAdapter;
    private Boolean showCurrentPlayers;
    private TextView emptyView;


    public AddPlayerListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null && getArguments().getString(ARG_VALUE).equals("current")){
            showCurrentPlayers = true;
        }
        allPlayers = new ArrayList<>();
        availablePlayers = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Player player = snapshot.getValue(Player.class);
                            allPlayers.add(player);
                            getAvailablePlayers();
                            Log.d("Name", player.getPlayerName());
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        Singleton.setCurrentFragment("playerList");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player_list, container, false);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        search(searchView);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView rvPlayers = view.findViewById(R.id.players_rv);
        rvPlayers.setVisibility(View.GONE);
        emptyView = view.findViewById(R.id.empty_view);
        emptyView.setVisibility(View.VISIBLE);
        getAvailablePlayers();
        Log.d("Av", String.valueOf(availablePlayers.size()));
            mAdapter = new PlayersAddAdapter(availablePlayers, getContext());
            rvPlayers.setAdapter(mAdapter);
            rvPlayers.setLayoutManager(new LinearLayoutManager(getContext()));
            rvPlayers.setItemAnimator(new DefaultItemAnimator());
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    getAvailablePlayers();
                    mAdapter.notifyDataSetChanged();
                    if(availablePlayers.size()==0) {
                        rvPlayers.setVisibility(View.GONE);
                        emptyView = view.findViewById(R.id.empty_view);
                        emptyView.setVisibility(View.VISIBLE);
                    }
                    else{
                        rvPlayers.setVisibility(View.VISIBLE);
                        emptyView = view.findViewById(R.id.empty_view);
                        emptyView.setVisibility(View.GONE);
                    }

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

    private void getAvailablePlayers(){
        availablePlayers.clear();
        for(Player p : allPlayers){
            if(!Singleton.getCurrentRoom().getActivePlayers().contains(p.getPlayerKey()))
                availablePlayers.add(p);
        }
    }

    private void search(final SearchView searchView){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });

    }


}

