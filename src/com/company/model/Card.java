package com.company.model;

import java.util.ArrayList;

public class Card  {
    public final static int card9=0;
    public final static int card10=1;
    public final static int cardJack=2;
    public final static int cardQueen=3;
    public final static int cardKing=4;
    public final static int cardAce=5;
    public final static int colorSpades=20;
    public final static int colorHearts=21;
    public final static int colorClubs=22;
    public final static int colorDiamonds=23;
    public static int [] colors = new int[]{colorSpades,colorClubs,colorDiamonds,colorHearts};
    public static int [] figures = new int[]{card9,card10,cardJack,cardQueen,cardKing,cardAce};
    public static int [] game1000FiguresOrder = new int[]{card9,cardJack,cardQueen,cardKing,card10,cardAce};

    public static int getReportPoints(int symbol) {
        switch (symbol){
            case colorClubs:
                return 100;
            case colorSpades:
                return 80;
            case colorHearts:
                return 60;
            case colorDiamonds:
                return 40;
            default:
                return 0;
        }
    }

    public String getCardText(){
        switch (getSymbol()){
            case card9:
                return "9";
            case card10:
                return "10";
            case cardJack:
                return "Jack";
            case cardQueen:
                return "Queen";
            case cardKing:
                return "King";
            case cardAce:
                return "Ace";
            default:
                return"unknown";
        }
    }
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getSymbol() {
        return symbol;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    int color;
    int symbol;

    String userId;

    public static ArrayList<Card> generateCards(){
        ArrayList<Card> set = new ArrayList<>();
        for(int color: colors){
            for(int figure: figures){
                Card card = new Card();
                card.setColor(color);
                card.setSymbol(figure);
                set.add(card);
            }
        }
        return set;
    }
}
