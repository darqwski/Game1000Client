package com.company.model;

public class Player {
    String joinedTableId;
    String playerId;
    boolean readyToPlay;
    int points=0;
    public void resetPlayer(){
        setDeclaredPoints(0);
        setPointsInRound(0);
    }
    public int getDeclaredPoints() {
        return declaredPoints;
    }

    public void setDeclaredPoints(int declaredPoints) {
        this.declaredPoints = declaredPoints;
    }

    public int getPointsInRound() {
        return pointsInRound;
    }

    public void setPointsInRound(int pointsInRound) {
        this.pointsInRound = pointsInRound;
    }

    int declaredPoints=0;
    int pointsInRound=0;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isReadyToPlay() {
        return readyToPlay;
    }

    public void setReadyToPlay(boolean readyToPlay) {
        this.readyToPlay = readyToPlay;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getJoinedTableId() {
        return joinedTableId;
    }

    public void setJoinedTableId(String joinedTableId) {
        this.joinedTableId = joinedTableId;
    }

    public Player(String playerId) {
        this.playerId = playerId;
        points=0;
    }

    public void addPoints(int points) {
        this.pointsInRound+=points;
    }

    public void savePoints(){
        setPoints(getPoints()+getPointsInRound());
    }

    public void deletePoints() {
        setPoints(getPoints()-getDeclaredPoints());
    }
}
