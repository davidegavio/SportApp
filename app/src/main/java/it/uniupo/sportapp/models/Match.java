package it.uniupo.sportapp.models;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by dgavio on 16/10/17.
 */

public class Match {

    private Team teamA, teamB;
    private String matchDay;
    private String startTime;
    private String endTime;
    private ArrayList<ChatMessage> chatMessages;
    private String matchResult;

    public Match() {
    }

    public Match(Team teamA, Team teamB, String matchDay, String startTime, String endTime) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.matchDay = matchDay;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Team getTeamA() {
        return teamA;
    }

    public void setTeamA(Team teamA) {
        this.teamA = teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public void setTeamB(Team teamB) {
        this.teamB = teamB;
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

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

}
