package it.uniupo.sportapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.models.Player;

/**
 * Created by dgavio on 07/12/17.
 */

public class GoalsChartAdapter extends RecyclerView.Adapter<GoalsChartAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTv, positionTv, goalsTv;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.goals_chart_player_name);
            positionTv = itemView.findViewById(R.id.chart_position_tv);
            goalsTv = itemView.findViewById(R.id.goals_chart_goals_number);
        }
    }

    private List<String> mPlayersGoals;
    private Context mContext;

    public GoalsChartAdapter(ArrayList<String> mPlayersGoals, Context mContext) {
        this.mPlayersGoals = mPlayersGoals;
        this.mContext = mContext;
    }

    private Context getContext() {
        return mContext;
    }


    @Override
    public GoalsChartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View chartView = inflater.inflate(R.layout.goals_chart_player_row, parent, false);
        return new ViewHolder(chartView);
    }

    @Override
    public void onBindViewHolder(final GoalsChartAdapter.ViewHolder holder, final int position) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Player tempPlayer = dataSnapshot.child(mPlayersGoals.get(position).split("-")[0]).getValue(Player.class);
                Log.d("Pla", String.valueOf(tempPlayer.getPlayerName()));
                String goal = mPlayersGoals.get(position).split("-")[1];
                TextView positionTextView = holder.positionTv;
                TextView nameTextView = holder.nameTv;
                TextView goalsTextView = holder.goalsTv;
                positionTextView.setText(String.valueOf(position+1)+"°");
                nameTextView.setText(tempPlayer.getPlayerName());
                goalsTextView.setText("Goals: "+goal);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mPlayersGoals.size();
    }
}
