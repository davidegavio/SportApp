package it.uniupo.sportapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public RoomListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRooms = new ArrayList<>();
        if (Singleton.getCurrentPlayer().getPlayerRooms() != null) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("rooms");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot s : dataSnapshot.getChildren()){
                        Room tempRoom = s.getValue(Room.class);
                        Log.d("r", tempRoom.getRoomKey());
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rvRooms = view.findViewById(R.id.roomsRv);
        mAdapter = new RoomsAdapter(mRooms, getContext());
        rvRooms.setAdapter(mAdapter);
        rvRooms.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRooms.setItemAnimator(new DefaultItemAnimator());
        rvRooms.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

    }

}
