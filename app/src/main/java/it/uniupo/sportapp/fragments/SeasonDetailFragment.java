package it.uniupo.sportapp.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.adapters.SeasonDetailAdapter;
import it.uniupo.sportapp.models.Match;
import it.uniupo.sportapp.models.Season;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SeasonDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeasonDetailFragment extends Fragment {

    private static final String ARG_KEY = "key";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SeasonDetailAdapter mAdapter;

    private TextView seasonNameTv;


    // TODO: Rename and change types of parameters
    private String mSeasonKey;

    Season currentSeason;

    //private OnFragmentInteractionListener mListener;

    public SeasonDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SeasonDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeasonDetailFragment newInstance(String key) {
        SeasonDetailFragment fragment = new SeasonDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreate", "Here");
        if (getArguments() != null) {
            mSeasonKey = getArguments().getString(ARG_KEY);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("seasons");
            Log.d("Season", String.valueOf(mDatabase.child(mSeasonKey).child("seasonName")));
            String s = String.valueOf(mDatabase.child(mSeasonKey).child("seasonName"));
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("onDataChange", "Here");
                    currentSeason = dataSnapshot.child(mSeasonKey).getValue(Season.class);
                    seasonNameTv.setText(currentSeason.getSeasonName());
                    mAdapter = new SeasonDetailAdapter(currentSeason.getSeasonMatches());
                    mRecyclerView.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_season_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d("onViewCreated", "Here");
        seasonNameTv = view.findViewById(R.id.app_bar_tv);

        // Inflate the layout for this fragment
        mRecyclerView = (RecyclerView) view.findViewById(R.id.season_rv);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewMatch();

            }
        });
    }

    private void createNewMatch() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View timeview = inflater.inflate(R.layout.edit_dialog, null);
        builder.setView(timeview)
                // Add action buttons
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        TextView dateTv = timeview.findViewById(R.id.date_tv);
                        TextView timeTv = timeview.findViewById(R.id.time_tv);
                        dateTv.setText("31/10/2017");
                        timeTv.setText("19:00");
                        currentSeason.getSeasonMatches().add(new Match());
                        DatabaseReference mDatabase;
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        mDatabase.child("seasons").child(ARG_KEY).setValue(currentSeason);
                    }
                })
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).create().show();


    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }*/
}
