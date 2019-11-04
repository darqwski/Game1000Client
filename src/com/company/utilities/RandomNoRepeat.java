package com.company.utilities;

import java.util.ArrayList;
import java.util.Random;

public class RandomNoRepeat {
    public int range;
    public ArrayList<Integer> taken;
    public Random random;
    public RandomNoRepeat(int range){
     this.range=range;
     taken=new ArrayList<>();
     random=new Random();
    }

    public int getNext(){
        int takenNumber=random.nextInt(range);
        while(taken.contains(takenNumber)){
            takenNumber=random.nextInt(range);
        }
        taken.add(takenNumber);
        return takenNumber;
    }
}
