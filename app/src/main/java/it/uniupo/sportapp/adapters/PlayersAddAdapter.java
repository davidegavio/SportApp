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

public class PlayersAddAdapter extends RecyclerView.Adapter<PlayersAddAdapter.ViewHolder> implements Filterable {



    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTv, mailTv;
        public Button addBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.tv_name);
            mailTv = itemView.findViewById(R.id.tv_mail);
            addBtn = itemView.findViewById(R.id.add_button);

        }
    }

    private List<Player> mPlayers;
    private List<Player> mFilteredPlayers = new ArrayList<>();
    private Context mContext;

    public PlayersAddAdapter(List<Player> mPlayers, Context mContext) {
        this.mPlayers = mPlayers;
        this.mContext = mContext;
        mFilteredPlayers = mPlayers;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public PlayersAddAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View playerView = inflater.inflate(R.layout.add_player_row, parent, false);
        return new ViewHolder(playerView);
    }

    @Override
    public void onBindViewHolder(PlayersAddAdapter.ViewHolder holder, int position) {
        final Player tempPlayer = mFilteredPlayers.get(position);
        tempPlayer.setPlayerRooms(new ArrayList<String>());
        if(!mFilteredPlayers.isEmpty()) {
            final TextView nameTextView = holder.nameTv;
            nameTextView.setText(tempPlayer.getPlayerName());
            TextView mailTextView = holder.mailTv;
            mailTextView.setText(tempPlayer.getPlayerMail());
            Button addButton = holder.addBtn;
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    tempPlayer.getPlayerRooms().add(Singleton.getCurrentRoom().getRoomKey());
                    Singleton.getCurrentRoom().getActivePlayers().add(tempPlayer.getPlayerKey());
                    mDatabase.child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).setValue(Singleton.getCurrentRoom());
                    mDatabase.child("users").child(tempPlayer.getPlayerKey()).setValue(tempPlayer);
                    Toast.makeText(getContext(), nameTextView.getText(), Toast.LENGTH_SHORT).show();
                    Log.d("K", "K: "+"rooms/"+Singleton.getCurrentRoom().getRoomKey());
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return mFilteredPlayers.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                FilterResults filterResults = new FilterResults();
                if (charString.isEmpty()) {
                    mFilteredPlayers = mPlayers;
                } else{
                    ArrayList<Player> filteredList = new ArrayList<>();
                    for(Player player : mPlayers){
                        if(player.getPlayerName().toLowerCase().contains(charString))
                            filteredList.add(player);
                    }
                    mFilteredPlayers = filteredList;
                }
                filterResults.values = mFilteredPlayers;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredPlayers = (ArrayList<Player>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
