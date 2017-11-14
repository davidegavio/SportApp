package it.uniupo.sportapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.fragments.RoomFragment;
import it.uniupo.sportapp.models.Room;

/**
 * Created by 20010562 on 11/3/17.
 */

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nameTv, playersTv, seasonsTv;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.room_name_tv);
            playersTv = itemView.findViewById(R.id.room_players_tv);
            seasonsTv = itemView.findViewById(R.id.room_seasons_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("KK", "K: "+mRooms.get(getAdapterPosition()).getRoomKey());
                    Singleton.setCurrentRoom(mRooms.get(getAdapterPosition()));
                    //Log.d("roomListFragment", Singleton.getCurrentRoom().getExistingSeasons().toString());
                    RoomFragment fragment = new RoomFragment();
                    Bundle args = new Bundle();
                    args.putString("key", Singleton.getCurrentRoom().getRoomKey());
                    //args.putString("index", n);
                    fragment.setArguments(args);
                    ((MainActivity)getContext()).addFragment(fragment);
                }
            });
        }
    }


    private List<Room> mRooms;
    private Context mContext;

    public RoomsAdapter(List<Room> mRooms, Context mContext) {
        this.mRooms = mRooms;
        this.mContext = mContext;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View roomsView = inflater.inflate(R.layout.room_row, parent, false);
        return new ViewHolder(roomsView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Room tempRoom = mRooms.get(position);

        TextView nameTextView = holder.nameTv;
        nameTextView.setText(tempRoom.getRoomName());
        TextView playersTextView = holder.playersTv;
        playersTextView.setText(String.valueOf(tempRoom.getActivePlayers().size()));
        TextView seasonsTextView = holder.seasonsTv;
        if(tempRoom.getExistingSeasons()!=null)
            seasonsTextView.setText(String.valueOf(tempRoom.getExistingSeasons().size()));
        else seasonsTextView.setText("0");

    }

    @Override
    public int getItemCount() {
        return mRooms.size();
    }
}
