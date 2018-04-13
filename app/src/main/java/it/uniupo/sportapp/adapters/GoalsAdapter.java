package it.uniupo.sportapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.models.Player;

/**
 * Created by dgavio on 28/11/17.
 */

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTv, mailTv, goalsATv, goalsBTv;
        public Spinner goalsSpnr;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.tv_name);
            mailTv = itemView.findViewById(R.id.tv_mail);
            goalsSpnr = itemView.findViewById(R.id.spinner_goals);
            goalsATv = itemView.findViewById(R.id.goals_a_list);
            goalsBTv = itemView.findViewById(R.id.goals_b_list);
            if(Singleton.getCurrentMatch().getPlayerGoals()==null)
                Singleton.getCurrentMatch().setPlayerGoals(new HashMap<String, String>());
            if(Singleton.getCurrentSeason().getSeasonPlayerGoalsChart()==null)
                Singleton.getCurrentSeason().setSeasonPlayerGoalsChart(new HashMap<String, String>());
            if(Singleton.getCurrentSeason().getSeasonPlayerPresencesChart()==null)
                Singleton.getCurrentSeason().setSeasonPlayerPresencesChart(new HashMap<String, String>());
        }
    }

    private List<Player> mPlayers;
    private Context mContext;
    private int matchResult;


    public GoalsAdapter(ArrayList<Player> teamPlayers, int matchResult,Context context){
        this.mPlayers = teamPlayers;
        this.mContext = context;
        this.matchResult = matchResult;
    }

    @Override
    public GoalsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View playerView = inflater.inflate(R.layout.set_goal_player_row, parent, false);
        return new GoalsAdapter.ViewHolder(playerView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String[] goals = {""};
        final Player tempPlayer = mPlayers.get(position);
        //if(Singleton.getCurrentSeason().getSeasonPlayerGoalsChart().get(tempPlayer.getPlayerKey())==null){
            //Singleton.getCurrentSeason().getSeasonPlayerGoalsChart().put(tempPlayer.getPlayerKey(), "0");
        //}
        HashMap hashMap = Singleton.getCurrentSeason().getSeasonPlayerGoalsChart();
        TextView nameTextView = holder.nameTv;
        nameTextView.setText(tempPlayer.getPlayerName());
        TextView mailTextView = holder.mailTv;
        mailTextView.setText(tempPlayer.getPlayerMail());
        final Spinner goalsSpinner = holder.goalsSpnr;
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext, R.array.spinner_goals, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalsSpinner.setAdapter(adapter);
        //goalsSpinner.setSelection(Integer.parseInt(Singleton.getCurrentSeason().getSeasonPlayerGoalsChart().get(tempPlayer.getPlayerKey())));
        goalsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int goalsNumber = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
                if(matchResult>=0 && goalsNumber<=matchResult) {
                    if(goalsNumber>0)
                        Singleton.setGoalsString(Singleton.getGoalsString().concat(tempPlayer.getPlayerName() + "(" + goalsNumber + ")"));
                    //int n = Integer.parseInt(Singleton.getCurrentSeason().getSeasonPlayerGoalsChart().get(tempPlayer.getPlayerKey()));
                    //n+=goalsNumber;
                    //matchResult-=n;
                    if(goalsNumber>0)
                        Singleton.getCurrentMatch().getPlayerGoals().put(tempPlayer.getPlayerKey(), String.valueOf(goalsNumber));
                    else
                        Singleton.getCurrentMatch().getPlayerGoals().put(tempPlayer.getPlayerKey(), "0");                    //Singleton.getCurrentSeason().getSeasonPlayerGoalsChart().put(tempPlayer.getPlayerKey(), String.valueOf(n));
                }
                else{
                    Toast.makeText(mContext, "There's an error in your goals count", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    @Override
    public int getItemCount() {
        return mPlayers.size();
    }



}
