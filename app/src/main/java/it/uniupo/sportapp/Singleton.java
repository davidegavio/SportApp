package it.uniupo.sportapp;

import java.util.ArrayList;

import it.uniupo.sportapp.models.Match;
import it.uniupo.sportapp.models.Player;
import it.uniupo.sportapp.models.Room;

/**
 * Created by dgavio on 26/10/17.
 */

public class Singleton {

    private static Player currentPlayer;
    private static Player clickedPlayer;
    private static Room currentRoom;
    private static ArrayList<Match> matchesArrayList;
    private static String currentFragment;

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

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static void setCurrentPlayer(Player currentPlayer) {
        Singleton.currentPlayer = currentPlayer;
    }

    public static String getCurrentFragment() {
        return currentFragment;
    }

    public static void setCurrentFragment(String currentFragment) {
        Singleton.currentFragment = currentFragment;
    }

    public static Player getClickedPlayer() {
        return clickedPlayer;
    }

    public static void setClickedPlayer(Player clickedPlayer) {
        Singleton.clickedPlayer = clickedPlayer;
    }
}
