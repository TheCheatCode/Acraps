package com.example.tcc.acraps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public final static String GAME_TYPE = "com.example.tcc.TYPE";
    public static String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // TODO use reorder to front flag, find a way to pass it new intent in old activity
    public void onClickTest(View v) {

        Intent intent = new Intent(this, GameActivity.class);
        type = "TEST";
        // intent.putExtra(GAME_TYPE, type);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

    }

    public void onClickPlay(View v) {

        Intent intent = new Intent(this, GameActivity.class);
        type = "PLAY";
        // intent.putExtra(GAME_TYPE, type);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

    }

    public void onClickSettings(View v) {

        Intent intent = new Intent(this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

    }

    // if (minBet * (maxOdds + 1) > balance) { continue = false }
}
