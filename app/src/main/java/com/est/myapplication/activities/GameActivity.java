package com.est.myapplication.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.est.myapplication.R;
import com.est.myapplication.business.Services;
import com.est.myapplication.context.MyContext;
import com.est.myapplication.models.Player;
import com.est.myapplication.utils.GameResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class GameActivity extends AppCompatActivity {

    private MyContext context;
    private Services services;

    private Player currentPlayer;
    private AtomicInteger counter;
    private TextView result;

    private boolean gameOver;


    private Map<Integer,LottieAnimationView> cells = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        counter = new AtomicInteger(0);

        context = (MyContext) getApplicationContext();
        services = context.getServices();


        currentPlayer = context.getStarter();
        result = findViewById(R.id.result);
        result.setText(GameResult.DRAW.toString());


        gameOver = false;
        startGame();


        if (currentPlayer.equals(Player.BOOT)) {
            int bootChoice = services.setBootChoice();
            LottieAnimationView cell = cells.get(bootChoice);
            cell.performClick();
            currentPlayer = Player.USER;
        }

    }


    private void startGame() {
        setCells();
    }



    private void checkGameOver() {
        GameResult gameResult = services.gameResult();

        if (gameResult.equals(GameResult.BOOTWON)) {
            result.setText(GameResult.BOOTWON.toString());
            showDialog(GameResult.BOOTWON.toString());
        }
        else if (gameResult.equals(GameResult.USERWON)) {
            result.setText(GameResult.USERWON.toString());
            showDialog(GameResult.USERWON.toString());
        }
        else if (counter.get() > 8) {
            gameOver = true;
            showDialog(GameResult.DRAW.toString());
        }
    }


    private void setCells() {
        LottieAnimationView cell1 = findViewById(R.id._1);
        cells.put(1, cell1);

        LottieAnimationView cell2 = findViewById(R.id._2);
        cells.put(2, cell2);

        LottieAnimationView cell3 = findViewById(R.id._3);
        cells.put(3, cell3);

        LottieAnimationView cell4 = findViewById(R.id._4);
        cells.put(4, cell4);

        LottieAnimationView cell5 = findViewById(R.id._5);
        cells.put(5, cell5);

        LottieAnimationView cell6 = findViewById(R.id._6);
        cells.put(6, cell6);

        LottieAnimationView cell7 = findViewById(R.id._7);
        cells.put(7, cell7);

        LottieAnimationView cell8 = findViewById(R.id._8);
        cells.put(8, cell8);

        LottieAnimationView cell9 = findViewById(R.id._9);
        cells.put(9, cell9);

        for (Map.Entry<Integer, LottieAnimationView> cell : cells.entrySet()) {
            cell.getValue().setClickable(true);


            cell.getValue().setOnClickListener(c -> {
                counter.getAndIncrement();

                if (currentPlayer.equals(Player.USER)) {

                    services.setUserChoice(cell.getKey());

                    cell.getValue().setAnimation(R.raw.slashx);
                    currentPlayer = Player.BOOT;

                    int bootChoice = services.setBootChoice();
                    LottieAnimationView targetCell = cells.get(bootChoice);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            targetCell.performClick();
                            currentPlayer = Player.USER;
                        }
                    }, 1000);
                }
                else {
                    cell.getValue().setAnimation(R.raw.slasho);
                    currentPlayer = Player.USER;
                }

                cell.getValue().setClickable(false);

                checkGameOver();
            });
        }
    }

    private void showDialog(String resultText) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = new Dialog(GameActivity.this);
                dialog.setContentView(R.layout.game_over_dialog);
                TextView resultTextView = dialog.findViewById(R.id.result_text_view);
                resultTextView.setText(resultText);
                Button newGameButton = dialog.findViewById(R.id.new_game_button);
                Button exitButton = dialog.findViewById(R.id.exit_button);

                newGameButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        finish();
                    }
                });

                exitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

                dialog.show();
            }
        }, 2000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        services.restartGame();
        gameOver = true;
    }
}
