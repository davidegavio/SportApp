package it.uniupo.sportapp.models;

import java.util.ArrayList;

/**
 * Created by 20010562 on 11/2/17.
 */

public class Room {

    private ArrayList<Season> existingSeasons;
    private ArrayList<Player> activePlayers;
    private ArrayList<Player> adminPlayers;
    private String roomName;

    public Room() {
    }

    public Room(String roomName){
        this.roomName = roomName;
        this.activePlayers = new ArrayList<>();
        this.adminPlayers = new ArrayList<>();
        this.existingSeasons = new ArrayList<>();
    }

    public Room(ArrayList<Season> existingSeasons, ArrayList<Player> activePlayers, ArrayList<Player> adminPlayers, String roomName) {
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

    public ArrayList<Player> getActivePlayers() {
        return activePlayers;
    }

    public void setActivePlayers(ArrayList<Player> activePlayers) {
        this.activePlayers = activePlayers;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public ArrayList<Player> getAdminPlayers() {
        return adminPlayers;
    }

    public void setAdminPlayers(ArrayList<Player> adminPlayers) {
        this.adminPlayers = adminPlayers;
    }
}
