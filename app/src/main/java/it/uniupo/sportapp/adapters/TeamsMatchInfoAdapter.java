package it.uniupo.sportapp.adapters;

import android.content.Context;
import android.support.constraint.solver.Goal;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.models.Player;

/**
 * Created by dgavio on 28/11/17.
 */

public class TeamsMatchInfoAdapter extends RecyclerView.Adapter<TeamsMatchInfoAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTv, mailTv;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.simple_player_name);
        }
    }

    private List<Player> mPlayers;
    private Context mContext;

    public TeamsMatchInfoAdapter(ArrayList<Player> teamPlayers, Context context){
        this.mPlayers = teamPlayers;
        this.mContext = context;
    }

    @Override
    public TeamsMatchInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View playerView = inflater.inflate(R.layout.player_row, parent, false);
        return new TeamsMatchInfoAdapter.ViewHolder(playerView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Player tempPlayer = mPlayers.get(position);
        TextView nameTextView = holder.nameTv;
        nameTextView.setText(tempPlayer.getPlayerName());


    }


    @Override
    public int getItemCount() {
        return mPlayers.size();
    }

}
