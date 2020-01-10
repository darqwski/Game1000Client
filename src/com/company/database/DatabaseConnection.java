package com.company.database;

import com.company.model.Player;
import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;

import static com.company.Secret.DBPASS;

public class DatabaseConnection {
    private final static String DBURL = "jdbc:mysql://23195.m.tld.pl:3306/baza23195_darqwski";
    private final static String DBUSER = "admin23195_darqwski";
    private final static String DBDRIVER = "com.mysql.cj.jdbc.Driver";

    private Connection connection;
    private Statement statement;
    private String query;

    public DatabaseConnection() {
        //inicjalizacja parserów
    }

    public void saveData(Player user,int gameID) {
        try {
            connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
            statement = connection.createStatement();
            statement.executeUpdate("" +
                    "INSERT INTO `java_winners` (`id`, `name`, `date`, `points`, `gameID`) VALUES (NULL, '"+user.getPlayerId()+
                    "', NOW(),'"+user.getPoints()+"','"+gameID+"')");
            statement.close();
            connection.close();
        } catch ( SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public ArrayList<GameRecord> getData(){
        ArrayList<GameRecord> results = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * From java_winners GROUP BY `GameID`, `name` ORDER BY `GameID`");
            while(result.next()) {
                String name = result.getString("name");
                String date = result.getString("date");
                String points = result.getString("points");
                GameRecord gameRecord = new GameRecord();
                gameRecord.setPoints(points);
                gameRecord.setDate(date);
                gameRecord.setName(name);
                results.add(gameRecord);
            }
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
