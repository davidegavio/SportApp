package it.uniupo.sportapp.models;

/**
 * Created by dgavio on 16/10/17.
 */

public class Player {

    private String playerName;
    private String playerDescription;
    private String playerMail;
    private boolean isGoalkeeper;

    public Player(String playerName, String playerDescription, String playerMail, boolean isGoalkeeper) {
        this.playerName = playerName;
        this.playerDescription = playerDescription;
        this.playerMail = playerMail;
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

    public String getPlayerMail() {
        return playerMail;
    }

    public void setPlayerMail(String playerMail) {
        this.playerMail = playerMail;
    }
}
