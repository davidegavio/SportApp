package it.uniupo.sportapp.models;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

/**
 * Created by 20010562 on 11/2/17.
 */

public class Room {

    @Exclude
    private String roomKey;
    private ArrayList<Season> existingSeasons;
    private ArrayList<String> activePlayers;
    private ArrayList<String> adminPlayers;
    private String roomName;

    public Room() {
    }

    public Room(String roomName){
        this.roomName = roomName;
        this.activePlayers = new ArrayList<>();
        this.adminPlayers = new ArrayList<>();
        this.existingSeasons = new ArrayList<>();
    }

    public Room(ArrayList<Season> existingSeasons, ArrayList<String> activePlayers, ArrayList<String> adminPlayers, String roomName) {
        this.existingSeasons = existingSeasons;
        this.activePlayers = activePlayers;
        this.adminPlayers = adminPlayers;
        this.roomName = roomName;
    }

    public ArrayList<Season> getExistingSeasons() {
        return existingSeasons;
    }

    public void setExistingSeasons(ArrayList<Season> existingSeasons) {
        this.existingSeasons = existingSeasons;
    }

    public ArrayList<String> getActivePlayers() {
        return activePlayers;
    }

    public void setActivePlayers(ArrayList<String> activePlayers) {
        this.activePlayers = activePlayers;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public ArrayList<String> getAdminPlayers() {
        return adminPlayers;
    }

    public void setAdminPlayers(ArrayList<String> adminPlayers) {
        this.adminPlayers = adminPlayers;
    }

    public String getRoomKey() {
        return roomKey;
    }
    public void setRoomKey(String roomKey) {
        this.roomKey = roomKey;
    }
}
