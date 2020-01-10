package com.company;

import com.company.database.DatabaseConnection;
import com.company.model.Card;
import com.company.model.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static java.lang.Thread.sleep;

public class Main {
    public static  HashMap<String,Table> availableTables;
    public static  HashMap<String, Player> players;
    public static ArrayList<ServerThread> serverThreads;
    public static ArrayList<Card> onTableCards;
    public static ArrayList<Card> onStack;
    public static CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    public static void main(String args[]){
        Socket s=null;
        ServerSocket ss2=null;
        System.out.println("Server Listening......");
        availableTables= new HashMap<>();
        serverThreads = new ArrayList<>();
        players= new HashMap<>();
        availableTables.put("Table 1",  new Table().setTableName("Table 1"));
        availableTables.put("Table 2",  new Table().setTableName("Table 1"));
        availableTables.put("Table 3",  new Table().setTableName("Table 1"));
        availableTables.put("Table 4",  new Table().setTableName("Table 1"));
        onTableCards=new ArrayList<>();
        onStack=new ArrayList<>();
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
