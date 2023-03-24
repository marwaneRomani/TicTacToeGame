package com.est.myapplication.utils;

import java.util.EventListener;

public class GameStatus implements EventListener {
    private Boolean gameOver;

    public GameStatus() {
        this.gameOver = false;
    }

    public void gameOver() {
        this.gameOver = true;
    }

    public Boolean isGameOver() {
        return gameOver;
    }
}
