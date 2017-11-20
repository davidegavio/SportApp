package it.uniupo.sportapp.fragments;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.adapters.ChatAdapter;
import it.uniupo.sportapp.adapters.PlayersAdapter;
import it.uniupo.sportapp.models.ChatMessage;
import it.uniupo.sportapp.models.Match;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.support.v4.app.NotificationCompat.DEFAULT_ALL;
import static android.support.v4.app.NotificationCompat.DEFAULT_VIBRATE;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MatchChatTabFragment extends Fragment {

    private static final String ARG_PARAM1 = "index";
    private static final String ARG_PARAM2 = "season";

    private String matchIndex, seasonIndex;


    public MatchChatTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            matchIndex = getArguments().getString(ARG_PARAM1);
            seasonIndex = getArguments().getString(ARG_PARAM2);
        }
    }

    private void initializeFirebaseAndList(View view) {
        final RecyclerView rvMessages = view.findViewById(R.id.list_of_messages);
        final ChatAdapter mAdapter = new ChatAdapter(Singleton.getCurrentMatch().getChatMessages(), getActivity().getApplicationContext());
        rvMessages.setAdapter(mAdapter);
        rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMessages.setItemAnimator(new DefaultItemAnimator());
        Log.d("ref", "ref "+String.valueOf(FirebaseDatabase.getInstance().getReference().child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).child("existingSeasons").child("seasonMatches").child(seasonIndex).child(matchIndex).child("chatMessages")));
        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).child("existingSeasons").child("seasonMatches").child(seasonIndex).child(matchIndex).child("chatMessages");
        r.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren())
                    Singleton.getCurrentMatch().getChatMessages().add(d.getValue(ChatMessage.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).child("existingSeasons").child(seasonIndex).child("seasonMatches").child(matchIndex).child("chatMessages");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Singleton.getCurrentMatch().getChatMessages().add(dataSnapshot.getValue(ChatMessage.class));
                mAdapter.notifyItemInserted(Singleton.getCurrentMatch().getChatMessages().size()-1);
                rvMessages.scrollToPosition(Singleton.getCurrentMatch().getChatMessages().size()-1);
                if(!Singleton.getCurrentMatch().getChatMessages().get(Singleton.getCurrentMatch().getChatMessages().size()-1).getMessageUserKey().equals(Singleton.getCurrentPlayer().getPlayerKey()))
                    notifyUser(Singleton.getCurrentMatch().getChatMessages().get(Singleton.getCurrentMatch().getChatMessages().size()-1));

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
        initializeFirebaseAndList(view);
        FloatingActionButton fab = view.findViewById(R.id.fab_send);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).child("existingSeasons").child(seasonIndex).child("seasonMatches").child(matchIndex).child("chatMessages");
                Match m = Singleton.getCurrentMatch();
                Log.d("m", String.valueOf(m));
                ref.child(String.valueOf(Singleton.getCurrentMatch().getChatMessages().size())).setValue(new ChatMessage(input.getText().toString(),
                        Singleton.getCurrentPlayer().getPlayerKey(), Singleton.getCurrentPlayer().getPlayerName(), Singleton.getCurrentPlayer().getPlayerImageUid()));
                input.setText("");
            }
        });

    }

    private void notifyUser(ChatMessage chatMessage) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext())
                        .setSmallIcon(R.drawable.teams48)
                        .setDefaults(DEFAULT_VIBRATE)
                        .setPriority(DEFAULT_ALL)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(chatMessage.getMessageText());
        Intent resultIntent = new Intent(getContext(), MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(getContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(001, mBuilder.build());



    }
}
