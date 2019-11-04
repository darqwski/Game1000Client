package com.company.utilities;

import java.util.ArrayList;
import com.company.model.Card;

import static com.company.model.Card.*;

public class CardCalculations {
    public final static int game1000 = 1000;

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public int getTriumph() {
        return triumph;
    }

    public void setTriumph(int triumph) {
        this.triumph = triumph;
    }

    ArrayList<Card> cards;
    int triumph;
    int points;
    int gameRule=game1000;
    public Card findHighestCard(){
        if(gameRule==game1000)
            return findHighestCardBy1000();
        return null;
    }
    private ArrayList<Card> getTriumphs(){
        ArrayList<Card> winner = new ArrayList<>();
        for(Card card :cards){
            if(card.getColor()==triumph){
                winner.add(card);
            }
        }
        return winner;
    }
    private ArrayList<Card> inColor(){
        ArrayList<Card> winner = new ArrayList<>();
        for(Card card :cards){
            if(card.getColor()==cards.get(0).getColor()){
                winner.add(card);
            }
        }
        System.out.println("W kolorze są "+String.valueOf(winner.size()));
        return winner;
    }

    private Card getStrongestCardIn1000(ArrayList<Card> cards){
        for(int i=Card.game1000FiguresOrder.length-1;i>=0;i--){
            for(Card card : cards){
                if(card.getSymbol() == game1000FiguresOrder[i]){
                    return card;
                }
            }
        }
        return null;
    }
    public Card findHighestCardBy1000(){
        ArrayList<Card> winner = getTriumphs();
        if(winner.size()==1)
            return winner.get(0);
        if(winner.size()==0){
            winner = inColor();
        }
        if(winner.size()==1)
            return winner.get(0);
        return getStrongestCardIn1000(winner);
    }

    public int calculatePointIn1000(){
        int sum = 0;
        for(Card card : cards){
            sum+=getPoints(card);
        }
        points=sum;
        return sum;
    }

    public int getPoints() {
        return points;
    }
    private int getPoints(Card card) {
        switch (card.getSymbol()){
            case card9:
                return 0;
            case cardJack:
                return 2;
            case cardQueen:
                return 3;
            case cardKing:
                return 4;
            case card10:
                return 10;
            case cardAce:
                return 11;
            default:
                return 0;
        }
    }
}
