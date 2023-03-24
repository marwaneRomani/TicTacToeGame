package com.est.myapplication.business;

import com.est.myapplication.utils.GameResult;

public interface Services {

    GameResult gameResult();

    void setUserChoice(int choice);
    int setBootChoice();
    void restartGame();
}
