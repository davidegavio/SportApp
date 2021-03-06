package it.uniupo.sportapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Snapshot;
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
import it.uniupo.sportapp.adapters.RoomsAdapter;
import it.uniupo.sportapp.models.Room;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class RoomListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ROOMS = "rooms";

    // TODO: Rename and change types of parameters
    private ArrayList<Room> mRooms;
    RoomsAdapter mAdapter;
    private String mParam;
    private TextView emptyView;

    //private OnFragmentInteractionListener mListener;

    public RoomListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null)
            mParam = getArguments().getString("key");
        mRooms = new ArrayList<>();
        if (Singleton.getCurrentPlayer().getPlayerRooms() != null) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("rooms");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot s : dataSnapshot.getChildren()){
                        Room tempRoom = s.getValue(Room.class);
                        for(String l : Singleton.getCurrentPlayer().getPlayerRooms()){
                            Log.d("l", l);
                            if(l.equals(tempRoom.getRoomKey())){
                                mRooms.add(tempRoom);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        Singleton.setCurrentFragment("roomList");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_list, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rvRooms = view.findViewById(R.id.roomsRv);
        if(Singleton.getCurrentPlayer().getPlayerRooms().size()==0){
            rvRooms.setVisibility(View.GONE);
            emptyView = view.findViewById(R.id.empty_view);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            rvRooms.setVisibility(View.VISIBLE);
            emptyView = view.findViewById(R.id.empty_view);
            emptyView.setVisibility(View.GONE);
            mAdapter = new RoomsAdapter(mRooms, getContext());
            rvRooms.setAdapter(mAdapter);
            rvRooms.setLayoutManager(new LinearLayoutManager(getContext()));
            rvRooms.setItemAnimator(new DefaultItemAnimator());
            rvRooms.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
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

}
