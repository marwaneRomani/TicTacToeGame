package com.est.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.est.myapplication.R;
import com.est.myapplication.context.MyContext;
import com.est.myapplication.models.Player;

public class PickeUserActivity extends AppCompatActivity {

    private Button bootStart;
    private Button userStart;

    private Spinner bootLevels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picke_user);

        bootStart = findViewById(R.id.boot_start);
        userStart = findViewById(R.id.user_start);


        MyContext context = (MyContext) getApplicationContext();

        
        bootStart.setOnClickListener(c -> {
            context.setStarter(Player.BOOT);
            Intent intent = new Intent(PickeUserActivity.this, GameActivity.class);
            startActivity(intent);
        });

        userStart.setOnClickListener(c -> {
            context.setStarter(Player.USER);
            Intent intent = new Intent(PickeUserActivity.this, GameActivity.class);
            startActivity(intent);
        });

//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.boot_levels, R.layout.activity_picke_user);
//
//        adapter.setDropDownViewResource(R.layout.activity_picke_user);
//        bootLevels.setAdapter(adapter);
    }
}