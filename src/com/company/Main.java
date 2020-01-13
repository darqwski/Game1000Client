package com.company;

import com.company.database.DatabaseConnection;
import com.company.model.Question;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static java.lang.Thread.sleep;

public class Main {
    public static ArrayList<ServerThread> serverThreads;
    public static ArrayList<Question> questions;
    public static CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
    public static int indexOfQuestion = 0;
    public static boolean isCorrectAnswer(String receivedQuestion, String receivedAnswer){
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        for (Question question: questions) {
            if(question.getText().equals(receivedQuestion)){
                return question.isCorrectAnswer(receivedAnswer);
            }

        }
        return false;
    }

    public static void sendToAll(String message){
        for (ServerThread serverThread: serverThreads) {
            serverThread.streamOperator.send(message);
        }
    }
    public static void main(String args[]){
        questions= new DatabaseConnection().getData();
        indexOfQuestion = questions.size()-1;
        Socket s=null;
        ServerSocket ss2=null;
        System.out.println("Server Listening......");
        serverThreads = new ArrayList<>();

        try{
            ss2 = new ServerSocket(6666);
        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println("Server error");

        }

        while(true){
            try{
                s= ss2.accept();
                System.out.println("connection Established");
                ServerThread st=new ServerThread(s);
                serverThreads.add(st);
                st.start();
            }

            catch(Exception e){
                e.printStackTrace();
                System.out.println("Connection Error");

            }
        }

    }
}
