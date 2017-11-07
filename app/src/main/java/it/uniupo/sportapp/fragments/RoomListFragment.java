package it.uniupo.sportapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public RoomListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Singleton.getCurrentPlayer().getPlayerRooms() != null) {
            mRooms = Singleton.getCurrentPlayer().getPlayerRooms();
        }
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
        RoomsAdapter mAdapter = new RoomsAdapter(Singleton.getCurrentPlayer().getPlayerRooms(), getContext());
        rvRooms.setAdapter(mAdapter);
        rvRooms.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRooms.setItemAnimator(new DefaultItemAnimator());
        rvRooms.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

    }

}
