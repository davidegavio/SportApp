package it.uniupo.sportapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.models.Player;

/**
 * Created by 20010562 on 11/6/17.
 */

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.ViewHolder>{



    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTv;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.simple_player_name);
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
        View playerView = inflater.inflate(R.layout.player_row, parent, false);
        return new ViewHolder(playerView);
    }

    @Override
    public void onBindViewHolder(PlayersAdapter.ViewHolder holder, int position) {
        final Player tempPlayer = mPlayers.get(position);
        tempPlayer.setPlayerRooms(new ArrayList<String>());
        if(!mPlayers.isEmpty()) {
            final TextView nameTextView = holder.nameTv;
            nameTextView.setText(tempPlayer.getPlayerName());
        }
    }


    @Override
    public int getItemCount() {
        return mPlayers.size();
    }


}
