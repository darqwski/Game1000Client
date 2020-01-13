package com.company;

import com.company.model.Question;
import com.company.model.StreamOperator;
import java.net.Socket;

import static com.company.Main.indexOfQuestion;

public class ServerThread extends Thread{

    public StreamOperator streamOperator;
    public int points = 0;
    public ServerThread(Socket s){
        streamOperator=new StreamOperator(s);
        points = 0;
    }

    public void run() {
        while(true){
            String serverStatus = serverPrepareResponse(streamOperator.getLine());
            streamOperator.send(serverStatus);
        }
    }

    public void informUsers(){
        int leftQuestions = indexOfQuestion;
        int max = 0;
        for (ServerThread serverThread:Main.serverThreads) {
            if(serverThread.points>max)max = serverThread.points;
        }
        Main.sendToAll("NUMBER@@"+leftQuestions+"@@"+points);
    }

    public String serverPrepareResponse(String request){
        String [] requestParts = request.split("@@");
        if(requestParts[0].equals("ANSWER")){
            boolean isCorrect = Main.isCorrectAnswer(requestParts[1],requestParts[2]);
            if(isCorrect){
                points++;
                informUsers();
                return "ANSWER@@Brawo, sztos odpowiedź@@"+points;
            } else {
                informUsers();
                return "ANSWER@@W dupie byłeś i gówno widziałeś@@"+points;
            }
        } else if(requestParts[0].equals("GET")){
            Question question = Main.questions.get(indexOfQuestion);
            indexOfQuestion--;
            informUsers();
            return "QUESTION@@"+question.toJson();
        }
        return request;
    }

}
