package it.uniupo.sportapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import it.uniupo.sportapp.R;
import it.uniupo.sportapp.models.Match;

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
        }
    }

    // Store a member variable for the contacts
    private List<Match> mMatches;
    // Store the context for easy access
    private Context mContext;

    public MatchesAdapter(List<Match> mMatches, Context mContext) {
        this.mMatches = mMatches;
        this.mContext = mContext;
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
