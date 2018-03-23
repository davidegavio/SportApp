package it.uniupo.sportapp.fragments;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.adapters.ChatAdapter;
import it.uniupo.sportapp.models.ChatMessage;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.support.v4.app.NotificationCompat.DEFAULT_ALL;
import static android.support.v4.app.NotificationCompat.DEFAULT_VIBRATE;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MatchChatTabFragment extends Fragment {

    private static final String ARG_PARAM1 = "match";
    private static final String ARG_PARAM2 = "season";

    private String matchIndex, seasonIndex;
    private RecyclerView rvMessages;
    private ChatAdapter mAdapter;


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
        if(Singleton.getCurrentMatch().getChatMessages()==null)
            Singleton.getCurrentMatch().setChatMessages(new ArrayList<ChatMessage>());
        rvMessages = view.findViewById(R.id.list_of_messages);
        mAdapter = new ChatAdapter(Singleton.getCurrentMatch().getChatMessages(), getContext());
        rvMessages.setAdapter(mAdapter);
        rvMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMessages.setItemAnimator(new DefaultItemAnimator());

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
        Log.d("MCTF", matchIndex);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).child("existingSeasons").child(seasonIndex).child("seasonMatches").child(matchIndex).child("chatMessages");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("datasnapshot", dataSnapshot.toString());
                notifyUser("Last message sent in match number: "+matchIndex+" is: "+Singleton.getCurrentMatch().getChatMessages().get(Singleton.getCurrentMatch().getChatMessages().size()-1).getMessageText());
                mAdapter.notifyDataSetChanged();
                rvMessages.scrollToPosition(Singleton.getCurrentMatch().getChatMessages().size()-1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab_send);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).child("existingSeasons").child(seasonIndex).child("seasonMatches").child(matchIndex).child("chatMessages");
                Singleton.getCurrentMatch().getChatMessages().add(new ChatMessage(input.getText().toString(),
                        Singleton.getCurrentPlayer().getPlayerKey(), Singleton.getCurrentPlayer().getPlayerName(), Singleton.getCurrentPlayer().getPlayerImageUid()));
                Log.d("m", String.valueOf(Singleton.getCurrentMatch().getChatMessages().size()));
                ref.child(String.valueOf(Singleton.getCurrentMatch().getChatMessages().size()-1)).setValue(Singleton.getCurrentMatch().getChatMessages().get(Singleton.getCurrentMatch().getChatMessages().size()-1));
                input.setText("");
            }
        });

    }

    private void notifyUser(String chatMessage) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext())
                        .setSmallIcon(R.drawable.teams48)
                        .setDefaults(DEFAULT_VIBRATE)
                        .setPriority(DEFAULT_ALL)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(chatMessage);
        Intent resultIntent = new Intent(getContext(), MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(getContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(001, mBuilder.build());
        Log.d("c", "C");


    }
}
