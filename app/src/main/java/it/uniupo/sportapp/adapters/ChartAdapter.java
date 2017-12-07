package it.uniupo.sportapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.uniupo.sportapp.R;
import it.uniupo.sportapp.models.Player;

/**
 * Created by dgavio on 07/12/17.
 */

public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTv, positionTv, goalsTv;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.goals_chart_player_name);
            positionTv = itemView.findViewById(R.id.chart_position_tv);
            goalsTv = itemView.findViewById(R.id.goals_chart_goals_number);
        }
    }

    private List<Player> mPlayers;
    private Context mContext;
    private String type;

    public ChartAdapter(ArrayList<Player> mPlayers, Context mContext, String type) {
        this.mPlayers = mPlayers;
        this.mContext = mContext;
        this.type = type;
    }

    private Context getContext() {
        return mContext;
    }


    @Override
    public ChartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View chartView = new View(mContext);
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if(type.equals("goals"))
             chartView = inflater.inflate(R.layout.goals_chart_player_row, parent, false);
        return new ViewHolder(chartView);
    }

    @Override
    public void onBindViewHolder(ChartAdapter.ViewHolder holder, int position) {
        TextView nameTextView = holder.nameTv;
        TextView goalsTextView = holder.nameTv;

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
