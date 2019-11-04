package com.company.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class StreamOperator {
    Socket s;
    BufferedReader is = null;
    PrintWriter os=null;
    String line;
    public StreamOperator(Socket s){
        this.s=s;
        try{
            is= new BufferedReader(new InputStreamReader(s.getInputStream()));
            os=new PrintWriter(s.getOutputStream(), true);
        }catch(IOException e){
            System.out.println("IO error in server thread");
        }
    }
    public void send(String s){
        os.println(s);
    }
    public String getLine(){
        try {
            line=is.readLine();
            return line;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
