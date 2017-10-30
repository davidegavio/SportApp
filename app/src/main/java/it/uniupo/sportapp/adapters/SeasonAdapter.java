package it.uniupo.sportapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import it.uniupo.sportapp.models.Season;

/**
 * Created by 20010562 on 10/30/17.
 */

public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.ViewHolder>{

    private final ArrayList<Season> mSeasonArrayList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SeasonAdapter(ArrayList<Season> inputSeasonArrayList) {
        mSeasonArrayList = inputSeasonArrayList;
    }

    @Override
    public SeasonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(SeasonAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
