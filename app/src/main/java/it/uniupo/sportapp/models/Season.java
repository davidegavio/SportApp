package it.uniupo.sportapp.models;

import java.util.ArrayList;

/**
 * Created by dgavio on 16/10/17.
 */

public class Season {

    private String seasonName;
    private ArrayList<Match> seasonMatches;
    private String seasonBeginningDate;
    private String seasonEndingDate;
    private Player seasonAdmin;
    private Player seasonPlayers;

    public Season(String seasonName, Player seasonAdmin) {
        this.seasonName = seasonName;
        this.seasonAdmin = seasonAdmin;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public ArrayList<Match> getSeasonMatches() {
        return seasonMatches;
    }

    public void setSeasonMatches(ArrayList<Match> seasonMatches) {
        this.seasonMatches = seasonMatches;
    }

    public String getSeasonBeginningDate() {
        return seasonBeginningDate;
    }

    public void setSeasonBeginningDate(String seasonBeginningDate) {
        this.seasonBeginningDate = seasonBeginningDate;
    }

    public Player getSeasonAdmin() {
        return seasonAdmin;
    }

    public void setSeasonAdmin(Player seasonAdmin) {
        this.seasonAdmin = seasonAdmin;
    }

    public String getSeasonEndingDate() {
        return seasonEndingDate;
    }

    public void setSeasonEndingDate(String seasonEndingDate) {
        this.seasonEndingDate = seasonEndingDate;
    }

    public Player getSeasonPlayers() {
        return seasonPlayers;
    }

    public void setSeasonPlayers(Player seasonPlayers) {
        this.seasonPlayers = seasonPlayers;
    }
}
