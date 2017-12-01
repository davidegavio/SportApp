package it.uniupo.sportapp;

import java.util.ArrayList;

import it.uniupo.sportapp.models.Match;
import it.uniupo.sportapp.models.Player;
import it.uniupo.sportapp.models.Room;
import it.uniupo.sportapp.models.Season;

/**
 * Created by dgavio on 26/10/17.
 */

public class Singleton {

    private static String currentFragment, goalsString;
    private static Player currentPlayer;
    private static Room currentRoom;
    private static Season currentSeason;
    private static Match currentMatch;
    private static Player clickedPlayer;
    private static ArrayList<Match> matchesArrayList;

    public Singleton() {
        currentFragment = "";
        goalsString = "";
        currentPlayer = new Player();
        currentRoom = new Room();
        currentSeason = new Season();
        currentMatch = new Match();
        matchesArrayList = new ArrayList<>();
    }

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

    public static Match getCurrentMatch() {
        return currentMatch;
    }

    public static void setCurrentMatch(Match currentMatch) {
        Singleton.currentMatch = currentMatch;
    }

    public static Season getCurrentSeason() {
        return currentSeason;
    }

    public static void setCurrentSeason(Season currentSeason) {
        Singleton.currentSeason = currentSeason;
    }

    public static String getGoalsString() {
        return goalsString;
    }

    public static void setGoalsString(String goalsString) {
        Singleton.goalsString = goalsString;
    }
}
