package com.company.database;

import com.company.model.Question;

import java.sql.*;
import java.util.ArrayList;

import static com.company.Secret.DBPASS;

public class DatabaseConnection {
    private final static String DBURL = "jdbc:mysql://localhost:3306/quiz";
    private final static String DBUSER = "root";
    private final static String DBDRIVER = "com.mysql.cj.jdbc.Driver";

    private Connection connection;
    private Statement statement;
    private String query;

    public DatabaseConnection() {
        //inicjalizacja parserów
    }


    public ArrayList<Question> getData(){
        ArrayList<Question> results = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
            statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * From questions");
            while(result.next()) {
                Question question = new Question();
                question.setText(result.getString("Text"));
                question.setCorrect(result.getString("CorrectAnswer"));
                question.addAnswer(result.getString("CorrectAnswer"));
                question.addAnswer(result.getString("BadAnswer1"));
                question.addAnswer(result.getString("BadAnswer2"));
                question.addAnswer(result.getString("BadAnswer3"));

                results.add(question);
            }
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
