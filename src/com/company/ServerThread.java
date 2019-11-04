package com.company;

import com.company.model.Card;
import com.company.model.Message;
import com.company.model.Player;
import com.company.model.StreamOperator;
import com.company.utilities.CardCalculations;
import com.company.utilities.RandomNoRepeat;
import com.google.gson.Gson;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static com.company.Main.*;

public class ServerThread extends Thread{

    StreamOperator streamOperator;
    Player player;
    Table table;
    public ServerThread(Socket s){
        streamOperator=new StreamOperator(s);
        player=new Player("");
    }

    public String sendMessage(Message msg) {
        System.out.println("Sent"+msg.getMessage());
        streamOperator.send(msg.getMessage());
        return null;
    }

    public void run() {
        startGameLoop();
    }

    private void startGameLoop() {
        while(true){
            boolean serverStatus = serverPrepareResponse(streamOperator.getLine());
            if(!serverStatus){
                System.out.println("Response"+player.getPlayerId()+" was not perfect");
                responseServerInfo();
                break;
            }
        }
    }

    public boolean serverPrepareResponse(String line){
        Message message = new Message(line);
        if(message==null)return false;
        try{
            switch (message.getType()) {
                case "JOIN":
                    joinPlayerToTable(message);
                    break;
                case "START":
                    startGameAction(message);
                    break;
                case "DECLARE":
                    if(message.getUserId().equals(table.getCurrentPlayerName()))
                        receiveDeclarationFromUser(message);
                    break;
                case "SEND_EXTRA_CARD":
                    sendExtraCard(message.getText());
                    break;
                case "PUT_CARD":
                        giveCardOnTable(message);
                    break;
            }
        }catch (Exception e){
            System.out.println(message.getText());
            return false;
        }
        return true;
    }

    private void startGameAction(Message message) {
        availableTables.get(player.getJoinedTableId()).setPlayerReadyToPlay(player.getPlayerId());
        boolean started = availableTables.get(player.getJoinedTableId()).checkGameHasStarted();
        responseAll("MESSAGE", message.getUserId() + " is ready to start game");
        if (started) startGame();
    }

    public void sendExtraCard(String text) {
        String playerId = table.getNextHasReceived();
        Message extraCardMessage = new Message();
        extraCardMessage.setType("GIVE_EXTRA_CARD");
        extraCardMessage.setText(text);
        if(playerId.equals(table.getCurrentPlayerName())){
            if(table.getCurrentReceivedPlayerIndex() < table.getPlayers().size()){
                sendMessageToPlayer(table.getNextHasReceived(),extraCardMessage);
            }
        }
        else {
            sendMessageToPlayer(playerId,extraCardMessage);
        }

        if(table.getCurrentReceivedPlayerIndex() + 1 < table.getPlayers().size()){
            if(table.getPlayerIds().get(table.getCurrentReceivedPlayerIndex()+1).equals(table.getCurrentDeclarator())){
                if(table.getCurrentReceivedPlayerIndex()+2<table.getPlayers().size()){
                    beginCardPlaying();
                }
            }
            else {
                beginCardPlaying();
            }
        }
        else {
            beginCardPlaying();
        }

    }
    private void beginCardPlaying(){
        table.setCurrentPlayerName(table.getCurrentDeclarator());
        table.setStartingIndex(table.getCurrentPlayer());
        sendMessageToPlayer(table.getCurrentPlayerName(),Message.createPrivateMessage("CHANGE_PLAYER_STATUS","PLAYING"));
        responseServerInfo();
    }

    private void startGame() {
        responseAll("MESSAGE","Game has started");
        table.createGameQueue();
        prepareRound();
    }

    private void prepareRound() {
        table.clearTable();
        table.getNextPlayerToStartRound();
        giveCards();
        sendMessageToCurrentPlayer("DECLARE","POINTS");
    }

    private void sendMessageToCurrentPlayer(String type, String text){
        sendMessageToPlayer(table.getCurrentPlayerName(),Message.createPrivateMessage(type,text));
    }

    private void joinPlayerToTable(Message message) {
        table = availableTables.get(message.getText());
        player = table.playerIsInTable(message.getUserId());
        if(player==null){
            player = new Player(message.getUserId());
            player.setJoinedTableId(message.getText());
            availableTables.get(message.getText()).addPlayerToTable(player);
            players.put(message.getUserId(), player);
            responseAll("MESSAGE", message.getUserId() + " has joined game");
        }
        else {
            responseAll("MESSAGE", message.getUserId() + " has back to the game");
        }
        responseServerInfo();
    }

    public void giveCards(){
        ArrayList<Card> cards = Card.generateCards();
        RandomNoRepeat random = new RandomNoRepeat(cards.size());
        ArrayList<Card> onExtra = new ArrayList<>();
        for(int i = 0;i< serverThreads.size();i++)
            onExtra.add(cards.get(random.getNext()));
        table.setOnExtra(onExtra);
        table.setCurrentPlayer(table.getStartingIndex());
        for(int i = 0;i< serverThreads.size();i++){
            ServerThread serverThread = serverThreads.get(i);
            ArrayList<Card> userSet= new ArrayList<>();
            for(int j=1;j<cards.size()/table.getPlayers().size();j++){
                userSet.add(cards.get(random.getNext()));
            }
            Gson gson = new Gson();
            HashMap<String, Object> responseKeys=new HashMap<>();
            responseKeys.put("cards",(userSet));
            responseKeys.put("table",(table));
            serverThread.sendMessage(Message.createPrivateMessage("USER_CARDS",gson.toJson(responseKeys)));
        }
    }

    public void startRound(){
        table.setCurrentPlayerName(table.getCurrentDeclarator());
        sendMessageToCurrentPlayer("RECEIVE_EXTRA",new Gson().toJson(table.getOnExtra()));
        table.setHasReceivedExtraCard(table.getPlayerIds());
        responseAll("MESSAGE",table.getCurrentPlayerName()+" has declared "+String.valueOf(table.getHighestDeclaration()));
    }

    public void giveCardOnTable(Message message){
        if(!table.getCurrentPlayerName().equals(message.getUserId()))
            responsePrivate("MESSAGE","Nie możesz dodac karty na stoł");

        String[] cardParts = message.getText().split(":");
        Card card = new Card();
        card.setColor(Integer.parseInt(cardParts[0]));
        card.setSymbol(Integer.parseInt(cardParts[1]));
        card.setUserId(message.getUserId());
        try{
            if(table.userCanReport(message.getUserId())){
                if(cardParts[2].equals("REPORT")){
                    table.setTriumph(card.getColor());
                    table.players.get(player.getPlayerId()).addPoints(Card.getReportPoints(card.getColor()));
                }
            }
        }catch (ArrayIndexOutOfBoundsException exception){ }

        table.putCardOnTable(card);
        responseServerInfo();

        if(table.onTable.size()==table.playerIds.size()){
            try {
                sleep(1000);
                table.doCalculations();
                responseServerInfo();
                if(table.onStack.size()==24){
                    endRound();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void receiveDeclarationFromUser(Message message){
        table.receiveDeclaration(message);
        if(table.getAlreadyDeclared().size()==table.getPlayers().size()){
            startRound();
        }
        else {
            table.setNextPlayer();
            sendMessageToCurrentPlayer("DECLARE","POINTS");
        }
    }

    private void endRound() {
        table.givePoints();
        responseServerInfo();
        prepareRound();
    }

    public void responseAll(String type,String message){
        for(ServerThread serverThread : serverThreads){
            try{
                serverThread.sendMessage(Message.createPublicMessage(type,message));
            } catch (Exception e){ }
        }
    }

    public void responsePrivate(String type,String message){
        sendMessage(Message.createPrivateMessage(type,message));
    }

    public void responseServerInfo(){
        HashMap<String,Object> responseKeys = new HashMap<>();
        responseKeys.put("table",table);
        responseKeys.put("player",player);
        responseAll("INFO",new Gson().toJson(responseKeys));
    }

    public static void sendMessageToPlayer(String playerId, Message message){
        for(ServerThread serverThread : serverThreads){
            if(serverThread.player.getPlayerId().equals(playerId)){
                serverThread.sendMessage(message);
                break;
            }
        }
    }
}
