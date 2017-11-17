package it.uniupo.sportapp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.adapters.ChatAdapter;
import it.uniupo.sportapp.adapters.PlayersAdapter;
import it.uniupo.sportapp.models.ChatMessage;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MatchChatTabFragment extends Fragment {

    private ArrayList<ChatMessage> chatMessageList = new ArrayList<>();


    public MatchChatTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initializeFirebaseAndList(View view) {
        final RecyclerView rvMessages = view.findViewById(R.id.list_of_messages);
        final ChatAdapter mAdapter = new ChatAdapter(chatMessageList, getActivity().getApplicationContext());
        rvMessages.setAdapter(mAdapter);
        rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMessages.setItemAnimator(new DefaultItemAnimator());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("messages");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                chatMessageList.add(dataSnapshot.getValue(ChatMessage.class));
                mAdapter.notifyItemInserted(chatMessageList.size()-1);
                rvMessages.scrollToPosition(chatMessageList.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match_chat_tab, container, false);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final EditText input = view.findViewById(R.id.input);
        FloatingActionButton fab = view.findViewById(R.id.fab_send);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("messages")
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                            Singleton.getCurrentPlayer().getPlayerKey(), Singleton.getCurrentPlayer().getPlayerImageUid()));
                input.setText("");
            }
        });
        initializeFirebaseAndList(view);
    }
}
