package it.uniupo.sportapp.models;

import java.util.ArrayList;

/**
 * Created by dgavio on 16/10/17.
 */

public class Season {

    private ArrayList<Match> seasonMatches;
    private String beginningDate;

    public Season(ArrayList<Match> seasonMatches, String beginningDate) {
        this.seasonMatches = seasonMatches;
        this.beginningDate = beginningDate;
    }

    public ArrayList<Match> getSeasonMatches() {
        return seasonMatches;
    }

    public void setSeasonMatches(ArrayList<Match> seasonMatches) {
        this.seasonMatches = seasonMatches;
    }

    public String getBeginningDate() {
        return beginningDate;
    }

    public void setBeginningDate(String beginningDate) {
        this.beginningDate = beginningDate;
    }
}
