package it.uniupo.sportapp.models;

import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by dgavio on 16/10/17.
 */

public class Season {

    private String seasonName;
    private ArrayList<Match> seasonMatches;
    private String seasonBeginningDate;
    private String seasonEndingDate;
    private Player seasonAdmin;
    private ArrayList<Player> seasonPlayers;
    private ArrayMap<String, String> seasonPlayerGoalsChart;
    private ArrayMap<String, String> seasonPlayerPresencesChart;
    public Season() {
    }

    public Season(String seasonName) {
        this.seasonName = seasonName;
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

    public ArrayList<Player> getSeasonPlayers() {
        return seasonPlayers;
    }

    public void setSeasonPlayers(ArrayList<Player> seasonPlayers) {
        this.seasonPlayers = seasonPlayers;
    }

    public ArrayMap<String, String> getSeasonPlayerGoalsChart() {
        return seasonPlayerGoalsChart;
    }

    public void setSeasonPlayerGoalsChart(ArrayMap<String, String> seasonPlayerGoalsChart) {
        this.seasonPlayerGoalsChart = seasonPlayerGoalsChart;
    }

    public ArrayMap<String, String> getSeasonPlayerPresencesChart() {
        return seasonPlayerPresencesChart;
    }

    public void setSeasonPlayerPresencesChart(ArrayMap<String, String> seasonPlayerPresencesChart) {
        this.seasonPlayerPresencesChart = seasonPlayerPresencesChart;
    }
}
