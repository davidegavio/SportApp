package it.uniupo.sportapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.models.Player;

/**
 * Created by 20010562 on 11/6/17.
 */

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTv, mailTv;
        public Button addBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.name_tv);
            mailTv = itemView.findViewById(R.id.tv_mail);
            addBtn = itemView.findViewById(R.id.add_button);
        }
    }

    private List<Player> mPlayers;
    private Context mContext;

    public PlayersAdapter(List<Player> mPlayers, Context mContext) {
        this.mPlayers = mPlayers;
        this.mContext = mContext;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public PlayersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View playerView = inflater.inflate(R.layout.add_player_row, parent, false);
        return new ViewHolder(playerView);
    }

    @Override
    public void onBindViewHolder(PlayersAdapter.ViewHolder holder, int position) {
        final Player tempPlayer = mPlayers.get(position);
        TextView nameTextView = holder.nameTv;
        nameTextView.setText(tempPlayer.getPlayerName());
        TextView mailTextView = holder.mailTv;
        mailTextView.setText(tempPlayer.getPlayerMail());
        Button addButton = holder.addBtn;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Singleton.getCurrentRoom().getActivePlayers().add(tempPlayer);
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(Singleton.getCurrentPlayer());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPlayers.size();
    }

}
