package it.uniupo.sportapp.models;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

/**
 * Created by dgavio on 16/10/17.
 */

public class Player {

    private String playerName;
    private String playerDescription;
    private String playerMail;
    private boolean isGoalkeeper;
    private String playerImageUid;
    private ArrayList<String> playerRooms;
    @Exclude
    private String playerKey;

    public Player(String playerName, String playerDescription, String playerMail, boolean isGoalkeeper, ArrayList<String> playerRooms) {
        this.playerName = playerName;
        this.playerDescription = playerDescription;
        this.playerMail = playerMail;
        this.isGoalkeeper = isGoalkeeper;
        this.playerRooms = playerRooms;
    }

    public Player(String playerName, String playerDescription, String playerMail, boolean isGoalkeeper) {
        this.playerName = playerName;
        this.playerDescription = playerDescription;
        this.playerMail = playerMail;
        this.isGoalkeeper = isGoalkeeper;
        this.playerRooms = new ArrayList<>();
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

    public ArrayList<String> getPlayerRooms() {
        return playerRooms;
    }

    public void setPlayerRooms(ArrayList<String> playerRooms) {
        this.playerRooms = playerRooms;
    }

    public String getPlayerKey() {
        return playerKey;
    }

    public void setPlayerKey(String playerKey) {
        this.playerKey = playerKey;
    }

    public String getPlayerImageUid() {
        return playerImageUid;
    }

    public void setPlayerImageUid(String playerImageUid) {
        this.playerImageUid = playerImageUid;
    }
}
