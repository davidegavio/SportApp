package it.uniupo.sportapp.models;

import java.util.ArrayList;

/**
 * Created by dgavio on 16/10/17.
 */

public class Team {

    private String teamName;
    private ArrayList<Player> teamPlayers;

    public Team() {
    }

    public Team(String teamName, ArrayList<Player> teamPlayers) {
        this.teamName = teamName;
        this.teamPlayers = teamPlayers;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public ArrayList<Player> getTeamPlayers() {
        return teamPlayers;
    }

    public void setTeamPlayers(ArrayList<Player> teamPlayers) {
        this.teamPlayers = teamPlayers;
    }
}
