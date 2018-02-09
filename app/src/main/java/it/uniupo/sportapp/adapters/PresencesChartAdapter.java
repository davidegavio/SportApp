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

public class PresencesChartAdapter extends RecyclerView.Adapter<PresencesChartAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTv, positionTv, presencesTv;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.presences_chart_player_name);
            positionTv = itemView.findViewById(R.id.chart_position_tv);
            presencesTv = itemView.findViewById(R.id.presences_chart_number);
        }
    }

    private List<String> mPlayersPresences;
    private Context mContext;

    public PresencesChartAdapter(ArrayList<String> mPlayersPresences, Context mContext) {
        this.mPlayersPresences = mPlayersPresences;
        this.mContext = mContext;
    }

    private Context getContext() {
        return mContext;
    }


    @Override
    public PresencesChartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View chartView = inflater.inflate(R.layout.goals_chart_player_row, parent, false);
        return new ViewHolder(chartView);
    }

    @Override
    public void onBindViewHolder(final PresencesChartAdapter.ViewHolder holder, final int position) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Player tempPlayer = dataSnapshot.child(mPlayersPresences.get(position).split("-")[0]).getValue(Player.class);
                Log.d("Pla", String.valueOf(tempPlayer.getPlayerName()));
                String presences = mPlayersPresences.get(position).split("-")[1];
                TextView positionTextView = holder.positionTv;
                TextView nameTextView = holder.nameTv;
                TextView presencesTextView = holder.presencesTv;
                positionTextView.setText(String.valueOf(position+1)+"°");
                nameTextView.setText(tempPlayer.getPlayerName());
                presencesTextView.setText("Presences: "+presences);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mPlayersPresences.size();
    }
}
