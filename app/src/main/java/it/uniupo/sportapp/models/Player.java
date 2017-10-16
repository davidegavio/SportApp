package it.uniupo.sportapp.models;

/**
 * Created by dgavio on 16/10/17.
 */

public class Player {

    private String playerName;
    private String playerDescription;
    private boolean isGoalkeeper;

    public Player(String playerName, String playerDescription, boolean isGoalkeeper) {
        this.playerName = playerName;
        this.playerDescription = playerDescription;
        this.isGoalkeeper = isGoalkeeper;
    }

    public Player() {
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerDescription() {
        return playerDescription;
    }

    public void setPlayerDescription(String playerDescription) {
        this.playerDescription = playerDescription;
    }

    public boolean isGoalkeeper() {
        return isGoalkeeper;
    }

    public void setGoalkeeper(boolean goalkeeper) {
        isGoalkeeper = goalkeeper;
    }
}
