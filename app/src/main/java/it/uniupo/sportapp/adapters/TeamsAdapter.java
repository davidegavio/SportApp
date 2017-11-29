package it.uniupo.sportapp.adapters;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.models.Match;
import it.uniupo.sportapp.models.Player;
import it.uniupo.sportapp.models.Team;

/**
 * Created by dgavio on 22/11/17.
 */

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.ViewHolder>{
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTv, mailTv;
        public RadioGroup radioGrp;
        public RadioButton radioBtnA, radioBtnB;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.tv_name);
            mailTv = itemView.findViewById(R.id.tv_mail);
            radioGrp = itemView.findViewById(R.id.radio_grp);
            radioBtnA = itemView.findViewById(R.id.radio_btn_a);
            radioBtnB = itemView.findViewById(R.id.radio_btn_b);
        }
    }

    private List<Player> mPlayers;
    private Context mContext;
    private String seasonIndex, matchIndex;

    public TeamsAdapter(List<Player> mPlayers, Context mContext, String seasonIndex, String matchIndex) {
        this.mPlayers = mPlayers;
        this.mContext = mContext;
        this.seasonIndex = seasonIndex;
        this.matchIndex = matchIndex;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public TeamsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View playerView = inflater.inflate(R.layout.team_player_row, parent, false);
        return new TeamsAdapter.ViewHolder(playerView);
    }

    @Override
    public void onBindViewHolder(TeamsAdapter.ViewHolder holder, int position) {
        final Player tempPlayer = mPlayers.get(position);
        RadioGroup radioGroupTeams = holder.radioGrp;
        if(!mPlayers.isEmpty()) {
            final TextView nameTextView = holder.nameTv;
            nameTextView.setText(tempPlayer.getPlayerName());
            TextView mailTextView = holder.mailTv;
            mailTextView.setText(tempPlayer.getPlayerMail());

        }

        radioGroupTeams.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.radio_btn_a:
                        Singleton.getCurrentMatch().getTeamA().getTeamPlayers().add(tempPlayer);
                        if(Singleton.getCurrentMatch().getTeamB().getTeamPlayers().contains(tempPlayer))
                            Singleton.getCurrentMatch().getTeamB().getTeamPlayers().remove(tempPlayer);
                        break;
                    case R.id.radio_btn_b:
                        Singleton.getCurrentMatch().getTeamB().getTeamPlayers().add(tempPlayer);
                        if(Singleton.getCurrentMatch().getTeamA().getTeamPlayers().contains(tempPlayer))
                            Singleton.getCurrentMatch().getTeamA().getTeamPlayers().remove(tempPlayer);
                        break;
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return mPlayers.size();
    }


}
