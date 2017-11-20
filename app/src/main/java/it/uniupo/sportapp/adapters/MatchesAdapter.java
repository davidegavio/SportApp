package it.uniupo.sportapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;
import it.uniupo.sportapp.fragments.MatchDetailFragment;
import it.uniupo.sportapp.models.ChatMessage;
import it.uniupo.sportapp.models.Match;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

/**
 * Created by 20010562 on 11/3/17.
 */

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView dayTv, startHourTv, endHourTv;

        public ViewHolder(View itemView) {
            super(itemView);
            dayTv = itemView.findViewById(R.id.match_day);
            startHourTv = itemView.findViewById(R.id.match_start_hour);
            endHourTv = itemView.findViewById(R.id.match_end_hour);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Singleton.setCurrentMatch(mMatches.get(getAdapterPosition()));
                    Singleton.getCurrentMatch().setChatMessages(new ArrayList<ChatMessage>());
                    MatchDetailFragment fragment = new MatchDetailFragment();
                    Bundle b = new Bundle();
                    b.putString("index", String.valueOf(Singleton.getCurrentSeason().getSeasonMatches().size()-1));
                    b.putString("season", seasonIndex);
                    fragment.setArguments(b);
                    ((MainActivity)getmContext()).addFragment(fragment);
                }
            });
        }
    }

    // Store a member variable for the contacts
    private List<Match> mMatches;
    // Store the context for easy access
    private Context mContext;
    private String seasonIndex;

    public MatchesAdapter(List<Match> mMatches, String seasonIndex,Context mContext) {
        this.mMatches = mMatches;
        this.mContext = mContext;
        this.seasonIndex = seasonIndex;
    }

    private Context getmContext(){
        return mContext;
    }

    @Override
    public MatchesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View matchesView = inflater.inflate(R.layout.match_row, parent, false);
        return new ViewHolder(matchesView);
    }

    @Override
    public void onBindViewHolder(MatchesAdapter.ViewHolder holder, int position) {
        Match tempMatch = mMatches.get(position);
        TextView dayTextView = holder.dayTv;
        dayTextView.setText(tempMatch.getMatchDay());
        TextView startHourTextView = holder.startHourTv;
        startHourTextView.setText(tempMatch.getStartTime());
        TextView endHourTextView = holder.endHourTv;
        endHourTextView.setText(tempMatch.getEndTime());

    }

    @Override
    public int getItemCount() {
        return mMatches.size();
    }
}
