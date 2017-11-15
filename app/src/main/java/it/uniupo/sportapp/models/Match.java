package it.uniupo.sportapp.models;

import java.util.ArrayList;

/**
 * Created by dgavio on 16/10/17.
 */

public class Match {

    private ArrayList<Team> opposingTeams;
    private String matchDay;
    private String startTime;
    private String endTime;
    private ArrayList<ChatMessage> chatMessages;

    public Match() {
    }

    public Match(ArrayList<Team> opposingTeams, String matchDay, String startTime, String endTime) {
        this.opposingTeams = opposingTeams;
        this.matchDay = matchDay;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ArrayList<Team> getOpposingTeams() {
        return opposingTeams;
    }

    public void setOpposingTeams(ArrayList<Team> opposingTeams) {
        this.opposingTeams = opposingTeams;
    }

    public String getMatchDay() {
        return matchDay;
    }

    public void setMatchDay(String matchDay) {
        this.matchDay = matchDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public ArrayList<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(ArrayList<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }
}
