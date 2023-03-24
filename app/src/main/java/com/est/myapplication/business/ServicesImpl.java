package com.est.myapplication.business;


import com.est.myapplication.boot.Boot;
import com.est.myapplication.utils.GameResult;

import java.util.ArrayList;

public class ServicesImpl implements Services {

    private final int[][] WINNING_COMBINATIONS = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 },
                                                   { 1, 4, 7 }, { 2, 5, 8 }, { 3, 6, 9 },
                                                   { 1, 5, 9 }, { 3, 5, 7 } };

    private ArrayList<Integer> userChoices = new ArrayList<>();
    private ArrayList<Integer> bootChoices = new ArrayList<>();


    private Boot boot = new Boot();

    @Override
    public void setUserChoice(int choice) {
        userChoices.add(choice);
    }

    @Override
    public int setBootChoice() {
        int choice = boot.setBootChoice(bootChoices, userChoices);
        System.out.println("BOOT CHOICE : ------------- " + choice);
        return choice;
    }


    @Override
    public GameResult gameResult() {
        for (int[] combination : WINNING_COMBINATIONS) {
            int bootCount = 0;
            int userCount = 0;

            for (int cell : combination) {
                if (bootChoices.contains(cell)) bootCount++;
                else if (userChoices.contains(cell)) userCount++;
            }

            if (userCount == 3 ) return GameResult.USERWON;
            if (bootCount == 3 ) return GameResult.BOOTWON;
        }

        return GameResult.DRAW;
    }

    @Override
    public void restartGame() {
        bootChoices.clear();
        userChoices.clear();
        System.out.println("reseting values...");
    }

}
