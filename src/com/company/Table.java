package com.company;

import com.company.model.Card;
import com.company.model.Message;
import com.company.model.Player;
import com.company.utilities.CardCalculations;
import com.company.utilities.RandomNoRepeat;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class Table {
    public static final int DECLARE_PASS = 0;
    HashMap<String, Player> players;
    ArrayList<Integer> gameQueue;
    int queuePointer = 0;
    ArrayList<String> playerIds;
    ArrayList<Card> onTable;
    ArrayList<Card> onStack;
    ArrayList<Card> onExtra;
    ArrayList<String> hasReceivedExtraCard = new ArrayList<>();
    String playerGivingCardFirst = "";
    int currentReceivedPlayerIndex = 0;
    String currentDeclarator;
    boolean gameHasStarted;
    ArrayList<String> alreadyDeclared;
    int highestDeclaration = 0;
    boolean gameStarted;
    int startingIndex;
    int currentPlayer;
    String currentPlayerName="";

    public int getCurrentReceivedPlayerIndex() {
        return currentReceivedPlayerIndex;
    }

    public void setCurrentReceivedPlayerIndex(int currentReceivedPlayerIndex) {
        this.currentReceivedPlayerIndex = currentReceivedPlayerIndex;
    }

    public ArrayList<String> getHasReceivedExtraCard() {
        return hasReceivedExtraCard;
    }

    public void setHasReceivedExtraCard(ArrayList<String> hasReceivedExtraCard) {
        this.hasReceivedExtraCard = hasReceivedExtraCard;
    }

    public String getNextHasReceived(){
        if(currentReceivedPlayerIndex >= playerIds.size())
            return null;
        currentReceivedPlayerIndex++;
        return playerIds.get(currentReceivedPlayerIndex-1);
    }
    public String getCurrentDeclarator() {
        return currentDeclarator;
    }

    public void setCurrentDeclarator(String currentDeclarator) {
        this.currentDeclarator = currentDeclarator;
    }

    public boolean isGameHasStarted() {
        return gameHasStarted;
    }

    public void setGameHasStarted(boolean gameHasStarted) {
        this.gameHasStarted = gameHasStarted;
    }

    public ArrayList<String> getAlreadyDeclared() {
        return alreadyDeclared;
    }

    public void setAlreadyDeclared(ArrayList<String> alreadyDeclared) {
        this.alreadyDeclared = alreadyDeclared;
    }

    public void resetDeclarationList(String userId){
        setAlreadyDeclared(new ArrayList<>());
        addToDeclarators(userId);
    }

    public void addToDeclarators(String userId){
        if(getAlreadyDeclared() == null){
            setAlreadyDeclared(new ArrayList<>());
        }
        getAlreadyDeclared().add(userId);
    }

    public int getHighestDeclaration() {
        return highestDeclaration;
    }

    public void setHighestDeclaration(int highestDeclaration) {
        this.highestDeclaration = highestDeclaration;
    }

    public ArrayList<Card> getOnExtra() {
        return onExtra;
    }

    public void setOnExtra(ArrayList<Card> onExtra) {
        this.onExtra = onExtra;
    }

    public int getStartingIndex() {
        return startingIndex;
    }

    public String getPlayerGivingCardFirst() {
        return playerGivingCardFirst;
    }

    public void setPlayerGivingCardFirst(String playerGivingCardFirst) {
        this.playerGivingCardFirst = playerGivingCardFirst;
    }

    public void setStartingIndex(int startingIndex) {
        this.startingIndex = startingIndex;
        setCurrentPlayer(startingIndex);
    }

    public int getTriumph() {
        return triumph;
    }

    public void setTriumph(int triumph) {
        this.triumph = triumph;
    }

    int triumph;

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public void setCurrentPlayerName(String currentPlayerName) {
        this.currentPlayerName = currentPlayerName;
        for(int i=0;i<playerIds.size();i++)
            if(playerIds.get(i).equals(currentPlayerName)){
                currentPlayer = (i);
                break;
            }
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean playerHasMove(Player player){
        return player.getPlayerId().equals(currentPlayerName);
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
        setCurrentPlayerName(playerIds.get(currentPlayer));

    }
    public ArrayList<String> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(ArrayList<String> playerIds) {
        this.playerIds = playerIds;
    }

    public String getTableName() {
        return tableName;
    }

    public Table setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    String tableName;

    public HashMap<String, Player> getPlayers() {
        return players;
    }

    public void setPlayers(HashMap<String, Player> players) {
        this.players = players;
    }
    public void setPlayerReadyToPlay(String playerId){
        getPlayers().get(playerId).setReadyToPlay(true);
    }

    public void addPlayerToTable(Player player){
        this.players.put(player.getPlayerId(),player);
        this.playerIds.add(player.getPlayerId());
    }

    public ArrayList<Card> getOnTable() {
        return onTable;
    }

    public void setOnTable(ArrayList<Card> onTable) {
        this.onTable = onTable;
    }

    public ArrayList<Card> getOnStack() {
        return onStack;
    }

    public void setOnStack(ArrayList<Card> onStack) {
        this.onStack = onStack;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }
    public Table(){
        setGameStarted(false);
        setPlayers(new HashMap<>());
        setPlayerIds(new ArrayList<>());
        setOnStack(new ArrayList<>());
        setOnTable(new ArrayList<>());
    }

    public boolean checkGameHasStarted() {
        if(players.size()<2) return false;
        for(String player : playerIds){
            if(!players.get(player).isReadyToPlay())return false;
        }
        setGameStarted(true);
        return true;
    }

    public String getTableInfo(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public void putCardOnTable(Card card){
        onTable.add(card);
        setNextPlayer();
    }
    public void setNextPlayer(){
        currentPlayer=(currentPlayer+1)%playerIds.size();
        setCurrentPlayerName(playerIds.get(currentPlayer));
    }
    public void moveTableToStack(){
        onStack.addAll(onTable);
        onTable=new ArrayList<>();
    }

    public Player playerIsInTable(String userId) {
        return players.get(userId);
    }

    public void clearTable() {
        setOnExtra(new ArrayList<>());
        setOnStack(new ArrayList<>());
        setHighestDeclaration(0);
        setCurrentDeclarator(null);
        setCurrentReceivedPlayerIndex(0);
        setTriumph(0);
        setAlreadyDeclared(new ArrayList<>());
    }

    public boolean userCanReport(String userId) {
        return getPlayerGivingCardFirst().equals(userId);
    }

    public void doCalculations() {
        CardCalculations cardCalculations = new CardCalculations();
        cardCalculations.setCards(onTable);
        cardCalculations.setTriumph(triumph);
        Card winner = cardCalculations.findHighestCardBy1000();
        int points = cardCalculations.calculatePointIn1000();
        setCurrentPlayerName(winner.getUserId());
        setPlayerGivingCardFirst(getCurrentPlayerName());
        players.get(winner.getUserId()).addPoints(points);
        moveTableToStack();
    }

    public void createGameQueue() {
        RandomNoRepeat randomNoRepeat = new RandomNoRepeat(playerIds.size());
        gameQueue = new ArrayList<>();
           while(gameQueue.size()!=playerIds.size())
               gameQueue.add(randomNoRepeat.getNext());
        queuePointer=0;
    }

    public void getNextPlayerToStartRound(){
        setStartingIndex(gameQueue.get(queuePointer));
        queuePointer++;
        setCurrentDeclarator(getCurrentPlayerName());
        addToDeclarators(getCurrentPlayerName());
    }

    public void givePoints() {
        for(String userId : getPlayerIds()){
            int pointsInRound = getPlayers().get(userId).getPointsInRound();
            if(userId.equals(playerIds.get(getStartingIndex()))){
                if(pointsInRound>=getPlayers().get(userId).getDeclaredPoints())
                    getPlayers().get(userId).savePoints();
                else
                    getPlayers().get(userId).deletePoints();
            }else{
                getPlayers().get(userId).savePoints();
            }
        }
    }

    public void receiveDeclaration(Message message) {
        if(Integer.valueOf(message.getText())> getHighestDeclaration()){
            getPlayers().get(getCurrentPlayerName()).setDeclaredPoints(Integer.valueOf(message.getText()));
            resetDeclarationList(message.getUserId());
            setHighestDeclaration(Integer.valueOf(message.getText()));
            setCurrentDeclarator(message.getUserId());
        }else {
            addToDeclarators(message.getUserId());
        }
    }
}
