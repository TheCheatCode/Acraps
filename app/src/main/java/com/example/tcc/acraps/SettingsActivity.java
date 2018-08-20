package com.example.tcc.acraps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    private EditText oddsEdit;
    private EditText startBalanceEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);

        oddsEdit = (EditText) findViewById(R.id.odds);
        startBalanceEdit = (EditText) findViewById(R.id.startingBalance);
    }

    public void onClickMenu(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onClickApply(View v) {
        String oddsStr = oddsEdit.getText().toString();
        String balanceStr = startBalanceEdit.getText().toString();
        if (oddsStr == null || oddsStr.equals("")) {

        } else {
            GameActivity.TABLE_ODDS = Integer.parseInt(oddsStr);
        }

        if (balanceStr == null || balanceStr.equals("")) {

        } else {
            GameActivity.STARTING_BALANCE = Double.parseDouble(balanceStr);
        }


    }
}
