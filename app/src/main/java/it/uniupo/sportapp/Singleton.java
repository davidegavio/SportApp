package it.uniupo.sportapp;

import java.util.ArrayList;

import it.uniupo.sportapp.models.Match;
import it.uniupo.sportapp.models.Room;

/**
 * Created by dgavio on 26/10/17.
 */

public class Singleton {

    public static Room currentRoom;
    public static ArrayList<Match> matchesArrayList;

    public static ArrayList<Match> getMatchesArrayList() {
        return matchesArrayList;
    }

    public static void setMatchesArrayList(ArrayList<Match> matchesArrayList) {
        Singleton.matchesArrayList = matchesArrayList;
    }

    public static Room getCurrentRoom() {
        return currentRoom;
    }

    public static void setCurrentRoom(Room currentRoom) {
        Singleton.currentRoom = currentRoom;
    }
}
