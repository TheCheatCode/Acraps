package com.example.tcc.acraps;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;

public class GameActivity extends AppCompatActivity {

    public static int TABLE_ODDS = 3;
    public static double STARTING_BALANCE = 2000;

    private EditText balText;
    private EditText pBet;
    private EditText pOdds;
    private EditText cBet;

    private EditText[] cOddsEdit = new EditText[7];
    private TextView[] cBetText = new TextView[7];

    private TextView[] pointViews = new TextView[7];

    private TextView outcomeTextView;

    int[] cBets = {0, 0, 0, 0, 0, 0, 0}; //{cBet4, cBet5, cBet6, nothing, cBet8, cBet9, cBet10}

    int[] cOddsArr = {0, 0, 0, 0, 0, 0, 0}; // {cOdds4, 5, 6, null, 8, 9, 10}

    int d1Val = 0;
    int d2Val = 0;
    int dTotal;
    double balance;
    boolean point = false;
    int pointOn = 0;
    String gameType = "";
    String rebet = "";
    // TODO set up a home screen with Play, Test, Settings, and Analytics(with reset button) Nav Buttons
    // test brings to current application
    // play brings to current but disables balance changes, and dice changes by player. Enables dice roll by app
    // settings allows you to change table odds, starting balance, points per hour
    // Analytics displays how many times each number came up, longest lose streak, longest win streak
    // highest single win, winnings earned by each type of win(natural, pass win on 6/8, 5/9, 4/10)
    // max comps per hour(10% of bet/hr), points played, total money bet, etc

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        // get game type (play or test)
        // Intent intent = this.getIntent();
        gameType = MainActivity.type; // intent.getStringExtra(MainActivity.GAME_TYPE);

        if (gameType.equals("TEST")) {
            enableDiceButtons();
        } else if (gameType.equals("PLAY")) {
            disableDiceButtons();
        }

        balText = (EditText) findViewById(R.id.balanceText);
        pBet = (EditText) findViewById(R.id.passBet);
        pOdds = (EditText) findViewById(R.id.passOdds);
        pOdds.setEnabled(false);
        cBet = (EditText) findViewById(R.id.comeBet);
        cBet.setEnabled(false);


        cOddsEdit[0] = (EditText) findViewById(R.id.comeOdds4);
        cOddsEdit[1] = (EditText) findViewById(R.id.comeOdds5);
        cOddsEdit[2] = (EditText) findViewById(R.id.comeOdds6);
        cOddsEdit[4] = (EditText) findViewById(R.id.comeOdds8);
        cOddsEdit[5] = (EditText) findViewById(R.id.comeOdds9);
        cOddsEdit[6] = (EditText) findViewById(R.id.comeOdds10);
        for (int i = 0; i < 7; i++) {
            if (i != 3) {
                cOddsEdit[i].setEnabled(false);
            }
        }

        cBetText[0] = (TextView) findViewById(R.id.comeBet4Text);
        cBetText[1] = (TextView) findViewById(R.id.comeBet5Text);
        cBetText[2] = (TextView) findViewById(R.id.comeBet6Text);
        cBetText[4] = (TextView) findViewById(R.id.comeBet8Text);
        cBetText[5] = (TextView) findViewById(R.id.comeBet9Text);
        cBetText[6] = (TextView) findViewById(R.id.comeBet10Text);

        pointViews[0] = (TextView) findViewById(R.id.point4Text);
        pointViews[1] = (TextView) findViewById(R.id.point5Text);
        pointViews[2] = (TextView) findViewById(R.id.point6Text);
        pointViews[4] = (TextView) findViewById(R.id.point8Text);
        pointViews[5] = (TextView) findViewById(R.id.point9Text);
        pointViews[6] = (TextView) findViewById(R.id.point10Text);

        outcomeTextView = (TextView) findViewById(R.id.outcomeText);

        balText.setText(String.valueOf(STARTING_BALANCE));

        // make TextWatcher to set table Mins and Maxs, also set max Odds
        pOdds.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // set Odds
                if (point) {
                    String pOddsStr = pOdds.getText().toString();
                    if (pOddsStr.length() > 1 && pOddsStr.startsWith("0")) { // edited, remove leading 0
                        pOddsStr = pOddsStr.substring(1);
                    }
                    int passOdds;
                    if (pOddsStr == null || pOddsStr.isEmpty()) {
                        return;
                    } else {
                        passOdds = Integer.parseInt(pOddsStr);
                    }

                    String pBetStr = pBet.getText().toString();
                    int passBet;
                    if (pBetStr == null || pBetStr.isEmpty()) {
                        passBet = 0;
                    } else {
                        passBet = Integer.parseInt(pBetStr);
                    }


                    if (passOdds > TABLE_ODDS * passBet) {
                        passOdds = TABLE_ODDS * passBet;
                        pOdds.setText(String.valueOf(passOdds));
                    }
                } else {
                    if (!(pOdds.getText().toString().equals(""))) { // avoid infinite loop with this check
                        pOdds.setText("");
                    }
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        setCOddsWatchers();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = this.getIntent();
        gameType = MainActivity.type; // intent.getStringExtra(MainActivity.GAME_TYPE);

        if (gameType.equals("TEST")) {
            enableDiceButtons();
        } else if (gameType.equals("PLAY")) {
            disableDiceButtons();
        }
    }

    public void onClickD1(View v) {
        TextView d1 = (TextView) findViewById(R.id.d1Text);


        if (v.getId() == R.id.d11) {
            d1Val = 1;
        } else if (v.getId() == R.id.d12) {
            d1Val = 2;
        } else if (v.getId() == R.id.d13) {
            d1Val = 3;
        } else if (v.getId() == R.id.d14) {
            d1Val = 4;
        } else if (v.getId() == R.id.d15) {
            d1Val = 5;
        } else if (v.getId() == R.id.d16) {
            d1Val = 6;
        }
        String newText = String.valueOf(d1Val);
        d1.setText(newText);
    }

    public void onClickD2(View v) {
        TextView d2 = (TextView) findViewById(R.id.d2Text);

        if (v.getId() == R.id.d21) {
            d2Val = 1;
        } else if (v.getId() == R.id.d22) {
            d2Val = 2;
        } else if (v.getId() == R.id.d23) {
            d2Val = 3;
        } else if (v.getId() == R.id.d24) {
            d2Val = 4;
        } else if (v.getId() == R.id.d25) {
            d2Val = 5;
        } else if (v.getId() == R.id.d26) {
            d2Val = 6;
        }
        String newText = String.valueOf(d2Val);
        d2.setText(newText);
    }

    public void onClickRoll(View v) {
        if (gameType.equals("PLAY")) {
            genDice();
        }
        dTotal = d1Val + d2Val;
        getBalance();
        getRebet();

        if (!(point)) {
            if (dTotal == 2) {
                craps();
            } else if (dTotal == 3) {
                craps();
            } else if (dTotal == 4) {
                onNewPoint();
                checkComeNoOdds();
            } else if (dTotal == 5) {
                onNewPoint();
                checkComeNoOdds();
            } else if (dTotal == 6) {
                onNewPoint();
                checkComeNoOdds();
            } else if (dTotal == 7) {
                natural();
                sevenNoOdds();
            } else if (dTotal == 8) {
                onNewPoint();
                checkComeNoOdds();
            } else if (dTotal == 9) {
                onNewPoint();
                checkComeNoOdds();
            } else if (dTotal == 10) {
                onNewPoint();
                checkComeNoOdds();
            } else if (dTotal == 11) {
                natural();
            } else if (dTotal == 12) {
                craps();
            }
        } else {
            // set odds bets here
            if (dTotal == 2) {
                comeCraps();
            } else if (dTotal == 3) {
                comeCraps();
            } else if (dTotal == 4) {
                checkComeWin();
                onNewCome();
                checkPassWin();
            } else if (dTotal == 5) {
                checkComeWin();
                onNewCome();
                checkPassWin();
            } else if (dTotal == 6) {
                checkComeWin();
                onNewCome();
                checkPassWin();
            } else if (dTotal == 7) {
                comeNatural();
                seven();
            } else if (dTotal == 8) {
                checkComeWin();
                onNewCome();
                checkPassWin();
            } else if (dTotal == 9) {
                checkComeWin();
                onNewCome();
                checkPassWin();
            } else if (dTotal == 10) {
                checkComeWin();
                onNewCome();
                checkPassWin();
            } else if (dTotal == 11) {
                comeNatural();
            } else if (dTotal == 12) {
                comeCraps();
            }
        }

        setBalance();
    }

    private void genDice() {
        d1Val = (int) (Math.random() * 6) + 1;
        d2Val = (int) (Math.random() * 6) + 1;

        TextView d1 = (TextView) findViewById(R.id.d1Text);
        TextView d2 = (TextView) findViewById(R.id.d2Text);

        d1.setText(String.valueOf(d1Val));
        d2.setText(String.valueOf(d2Val));
    }

    private void craps() {
        String passStr = pBet.getText().toString();
        int passBet;
        if (passStr == null || passStr.isEmpty()) {
            // passBet = 0;
            return;
        } else {
            passBet = Integer.parseInt(passStr);
        }


        balance -= passBet;

        pBet.setText("");
        // set outcome text to "craps " + dTotal
        outcomeTextView.setText(getString(R.string.craps_outcome_text, dTotal));
        // set outcome text color to red
        outcomeTextView.setTextColor(ContextCompat.getColor(GameActivity.this, R.color.red));
    }

    private void natural() {

        // set outcome text to "natural " + dTotal
        outcomeTextView.setText(getString(R.string.natural_outcome_text, dTotal));
        // set outcome text color to green
        outcomeTextView.setTextColor(ContextCompat.getColor(GameActivity.this, R.color.green));

        String passStr = pBet.getText().toString();
        int passBet;
        if (passStr == null || passStr.isEmpty()) {
            // passBet = 0;
            return;
        } else {
            passBet = Integer.parseInt(passStr);
        }

        balance += passBet;

        pBet.setText("");
    }

    private void onNewPoint() {
        pointViews[dTotal - 4].setTextColor(ContextCompat.getColor(GameActivity.this, R.color.red));
        pointOn = dTotal;

        point = true;
        outcomeTextView.setText("");
        // hide pBet field, or make uneditable
        pBet.setEnabled(false);
        cBet.setEnabled(true);

        String pBetStr = pBet.getText().toString();
        if (pBetStr == null || pBetStr.isEmpty()) {
            return;
        } else {
            int passBet = Integer.parseInt(pBetStr);

            balance -= passBet;
            pOdds.setEnabled(true); // enabled pOdds only if a pBet has been made
        }


    }

    private void afterPointCleanup() {
        // change point text color back to black

        for (int i = 0; i < 7; i++) {
            if (i != 3) {
                pointViews[i].setTextColor(ContextCompat.getColor(GameActivity.this, R.color.black));
            }
        }

        point = false;

        // unhide pBet field, or make clickable
        pBet.setText("");
        pOdds.setText("");
        pBet.setEnabled(true);
        cBet.setEnabled(false);
        pOdds.setEnabled(false);
    }

    private void comeCraps() {

        String comeStr = cBet.getText().toString();
        int comeBet;
        if (comeStr == null || comeStr.isEmpty()) {
            comeBet = 0;
        } else {
            comeBet = Integer.parseInt(comeStr);
        }

        balance -= comeBet;

        cBet.setText("");
    }

    private void onNewCome() {
        String cBetStr = cBet.getText().toString();
        if (cBetStr == null || cBetStr.isEmpty()) {

        } else {
            int currentBet = Integer.parseInt(cBetStr);
            int arrLoc = dTotal - 4;
            cBets[arrLoc] = currentBet;
            cBetText[arrLoc].setText(String.valueOf(currentBet));
            cOddsEdit[arrLoc].setEnabled(true);

            balance -= currentBet;

            cBet.setText("");
        }
    }

    private void comeNatural() {
        String comeStr = cBet.getText().toString();
        int comeBet;
        if (comeStr == null || comeStr.isEmpty()) {
            // passBet = 0;
            return;
        } else {
            comeBet = Integer.parseInt(comeStr);
        }

        balance += comeBet;

        cBet.setText("");
    }

    private void seven() {
        // subtract pass odds from balance
        String pOddsStr = pOdds.getText().toString();
        int passOdds;
        if (pOddsStr == null || pOddsStr.isEmpty()) {
            passOdds = 0;
        } else {
            passOdds = Integer.parseInt(pOddsStr);
            balance -= passOdds;
        }

        // clear all cBets and cOdds
        for (int i = 0; i < 7; i++) {
            if (i != 3) {
                cBets[i] = 0;
                String cOddsStr = cOddsEdit[i].getText().toString();
                if (cOddsStr == null || cOddsStr.isEmpty()) {
                    cOddsArr[i] = 0;
                } else {
                    cOddsArr[i] = Integer.parseInt(cOddsStr);
                }
                balance -= cOddsArr[i];
                cOddsArr[i] = 0;
                cOddsEdit[i].setText("");
                cOddsEdit[i].setEnabled(false);
                cBetText[i].setText("");
            }
        }

        afterPointCleanup();
        outcomeTextView.setText(R.string.sSevenOutcome);
        outcomeTextView.setTextColor(ContextCompat.getColor(GameActivity.this, R.color.red));

    }

    private void getBalance() {
        // get balance once per roll
        String balStr = balText.getText().toString();
        if (balStr == null || balStr.isEmpty()) {
            balance = 0;
        } else {
            balance = Double.parseDouble(balStr);
        }

        // TODO remove getBalance once balance can't be edited by player anymore
    }

    private void setBalance() {
        // set balance once per roll
        balText.setText(String.valueOf(balance));

        // TODO change to formatted string in strings.xml once balance can't be edited by player anymore
    }

    private void checkPassWin() {
        if (dTotal == pointOn) {
            // get pass bet as needed, only used once per roll at most
            String pBetStr = pBet.getText().toString();
            int passBet;
            if (pBetStr == null || pBetStr.isEmpty()) {
                passBet = 0;
            } else {
                passBet = Integer.parseInt(pBetStr);
            }
            if (passBet > 0) {
                // get pass odds
                String pOddsStr = pOdds.getText().toString();
                int passOdds;
                if (pOddsStr == null || pOddsStr.isEmpty()) {
                    passOdds = 0;
                } else {
                    passOdds = Integer.parseInt(pOddsStr);
                }
                winCalc(passBet, passOdds);
            }

            afterPointCleanup();
            outcomeTextView.setText(R.string.sWinOutcome);
            outcomeTextView.setTextColor(ContextCompat.getColor(GameActivity.this, R.color.green));
        }
    }

    private void checkComeWin() {
        int arrLoc = dTotal - 4;
        String cBetStr = cBetText[arrLoc].getText().toString();
        if (cBetStr == null || cBetStr.isEmpty()) {
            cBets[arrLoc] = 0;
        } else {
            cBets[arrLoc] = Integer.parseInt(cBetStr);
        }
        if (cBets[arrLoc] > 0) {
            // get pass odds
            String cOddsStr = cOddsEdit[arrLoc].getText().toString();
            if (cOddsStr == null || cOddsStr.isEmpty()) {
                cOddsArr[arrLoc] = 0;
            } else {
                cOddsArr[arrLoc] = Integer.parseInt(cOddsStr);
            }
            winCalc(cBets[arrLoc], cOddsArr[arrLoc]);
        }
        // set cBet and cOdds for that number to 0
        cBetText[arrLoc].setText("");
        cOddsEdit[arrLoc].setText("");
        cOddsEdit[arrLoc].setEnabled(false);
        cBets[arrLoc] = 0;
        cOddsArr[arrLoc] = 0;
    }

    private void winCalc(int bet, int odds) {
        // add bet back to balance, then add bet again for win
        balance += bet;
        balance += bet;
        if (dTotal == 6 || dTotal == 8) {
            balance += odds * 1.2;
        } else if (dTotal == 5 || dTotal == 9) {
            balance += odds * 1.5;
        } else if (dTotal == 4 || dTotal == 10) {
            balance += odds * 2;
        }
    }

    private void checkComeNoOdds() {
        int arrLoc = dTotal - 4;
        String cBetStr = cBetText[arrLoc].getText().toString();
        if (cBetStr == null || cBetStr.isEmpty()) {
            cBets[arrLoc] = 0;
        } else {
            cBets[arrLoc] = Integer.parseInt(cBetStr);
        }
        if (cBets[arrLoc] > 0) {
            cOddsArr[arrLoc] = 0;
            winCalc(cBets[arrLoc], cOddsArr[arrLoc]);
        }
        // set cBet and cOdds for that number to 0
        cBetText[arrLoc].setText("");
        cOddsEdit[arrLoc].setText("");
        cOddsEdit[arrLoc].setEnabled(false);
        cBets[arrLoc] = 0;
        cOddsArr[arrLoc] = 0;
    }

    private void sevenNoOdds() {

        // clear all cBets and cOdds
        for (int i = 0; i < 7; i++) {
            if (i != 3) {
                cBets[i] = 0;
                cOddsArr[i] = 0;
                cOddsEdit[i].setText("");
                cOddsEdit[i].setEnabled(false);
                cBetText[i].setText("");
            }
        }
    }

    private void setCOddsWatchers() {
        // make Text Watcher for all comeOdds fields
        // better way to do this is put inside a loop
        // for (int i = 0; i < 7; i++) {
        // if (i != 3) {
        // pass i as variable to text watcher?
        cOddsEdit[0].addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // set Odds
                String cOddsStr = cOddsEdit[0].getText().toString();
                if (cOddsStr == null || cOddsStr.isEmpty()) {
                    return;
                } else {
                    cOddsArr[0] = Integer.parseInt(cOddsStr);
                }

                String cBetStr = cBetText[0].getText().toString();
                if (cBetStr == null || cBetStr.isEmpty()) {
                    cBets[0] = 0;
                } else {
                    cBets[0] = Integer.parseInt(cBetStr);
                }


                if (cOddsArr[0] > TABLE_ODDS * cBets[0]) {
                    cOddsArr[0] = TABLE_ODDS * cBets[0];
                    cOddsEdit[0].setText(String.valueOf(cOddsArr[0]));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        cOddsEdit[1].addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // set Odds
                String cOddsStr = cOddsEdit[1].getText().toString();
                if (cOddsStr == null || cOddsStr.isEmpty()) {
                    return;
                } else {
                    cOddsArr[1] = Integer.parseInt(cOddsStr);
                }

                String cBetStr = cBetText[1].getText().toString();
                if (cBetStr == null || cBetStr.isEmpty()) {
                    cBets[1] = 0;
                } else {
                    cBets[1] = Integer.parseInt(cBetStr);
                }


                if (cOddsArr[1] > TABLE_ODDS * cBets[1]) {
                    cOddsArr[1] = TABLE_ODDS * cBets[1];
                    cOddsEdit[1].setText(String.valueOf(cOddsArr[1]));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        cOddsEdit[2].addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // set Odds
                String cOddsStr = cOddsEdit[2].getText().toString();
                if (cOddsStr == null || cOddsStr.isEmpty()) {
                    return;
                } else {
                    cOddsArr[2] = Integer.parseInt(cOddsStr);
                }

                String cBetStr = cBetText[2].getText().toString();
                if (cBetStr == null || cBetStr.isEmpty()) {
                    cBets[2] = 0;
                } else {
                    cBets[2] = Integer.parseInt(cBetStr);
                }


                if (cOddsArr[2] > TABLE_ODDS * cBets[2]) {
                    cOddsArr[2] = TABLE_ODDS * cBets[2];
                    cOddsEdit[2].setText(String.valueOf(cOddsArr[2]));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        cOddsEdit[4].addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // set Odds
                String cOddsStr = cOddsEdit[4].getText().toString();
                if (cOddsStr == null || cOddsStr.isEmpty()) {
                    return;
                } else {
                    cOddsArr[4] = Integer.parseInt(cOddsStr);
                }

                String cBetStr = cBetText[4].getText().toString();
                if (cBetStr == null || cBetStr.isEmpty()) {
                    cBets[4] = 0;
                } else {
                    cBets[4] = Integer.parseInt(cBetStr);
                }


                if (cOddsArr[4] > TABLE_ODDS * cBets[4]) {
                    cOddsArr[4] = TABLE_ODDS * cBets[4];
                    cOddsEdit[4].setText(String.valueOf(cOddsArr[4]));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        cOddsEdit[5].addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // set Odds
                String cOddsStr = cOddsEdit[5].getText().toString();
                if (cOddsStr == null || cOddsStr.isEmpty()) {
                    return;
                } else {
                    cOddsArr[5] = Integer.parseInt(cOddsStr);
                }

                String cBetStr = cBetText[5].getText().toString();
                if (cBetStr == null || cBetStr.isEmpty()) {
                    cBets[5] = 0;
                } else {
                    cBets[5] = Integer.parseInt(cBetStr);
                }


                if (cOddsArr[5] > TABLE_ODDS * cBets[5]) {
                    cOddsArr[5] = TABLE_ODDS * cBets[5];
                    cOddsEdit[5].setText(String.valueOf(cOddsArr[5]));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        cOddsEdit[6].addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // set Odds
                String cOddsStr = cOddsEdit[6].getText().toString();
                if (cOddsStr == null || cOddsStr.isEmpty()) {
                    return;
                } else {
                    cOddsArr[6] = Integer.parseInt(cOddsStr);
                }

                String cBetStr = cBetText[6].getText().toString();
                if (cBetStr == null || cBetStr.isEmpty()) {
                    cBets[6] = 0;
                } else {
                    cBets[6] = Integer.parseInt(cBetStr);
                }


                if (cOddsArr[6] > TABLE_ODDS * cBets[6]) {
                    cOddsArr[6] = TABLE_ODDS * cBets[6];
                    cOddsEdit[6].setText(String.valueOf(cOddsArr[6]));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }

    private void enableDiceButtons() {
        Button btn = (Button) findViewById(R.id.d11);
        btn.setEnabled(true);
        btn.setVisibility(View.VISIBLE);
        btn = (Button) findViewById(R.id.d12);
        btn.setEnabled(true);
        btn.setVisibility(View.VISIBLE);
        btn = (Button) findViewById(R.id.d13);
        btn.setEnabled(true);
        btn.setVisibility(View.VISIBLE);
        btn = (Button) findViewById(R.id.d14);
        btn.setEnabled(true);
        btn.setVisibility(View.VISIBLE);
        btn = (Button) findViewById(R.id.d15);
        btn.setEnabled(true);
        btn.setVisibility(View.VISIBLE);
        btn = (Button) findViewById(R.id.d16);
        btn.setEnabled(true);
        btn.setVisibility(View.VISIBLE);
        btn = (Button) findViewById(R.id.d21);
        btn.setEnabled(true);
        btn.setVisibility(View.VISIBLE);
        btn = (Button) findViewById(R.id.d22);
        btn.setEnabled(true);
        btn.setVisibility(View.VISIBLE);
        btn = (Button) findViewById(R.id.d23);
        btn.setEnabled(true);
        btn.setVisibility(View.VISIBLE);
        btn = (Button) findViewById(R.id.d24);
        btn.setEnabled(true);
        btn.setVisibility(View.VISIBLE);
        btn = (Button) findViewById(R.id.d25);
        btn.setEnabled(true);
        btn.setVisibility(View.VISIBLE);
        btn = (Button) findViewById(R.id.d26);
        btn.setEnabled(true);
        btn.setVisibility(View.VISIBLE);
    }

    private void disableDiceButtons() {
        Button btn = (Button) findViewById(R.id.d11);
        btn.setEnabled(false);
        btn.setVisibility(View.INVISIBLE);
        btn = (Button) findViewById(R.id.d12);
        btn.setEnabled(false);
        btn.setVisibility(View.INVISIBLE);
        btn = (Button) findViewById(R.id.d13);
        btn.setEnabled(false);
        btn.setVisibility(View.INVISIBLE);
        btn = (Button) findViewById(R.id.d14);
        btn.setEnabled(false);
        btn.setVisibility(View.INVISIBLE);
        btn = (Button) findViewById(R.id.d15);
        btn.setEnabled(false);
        btn.setVisibility(View.INVISIBLE);
        btn = (Button) findViewById(R.id.d16);
        btn.setEnabled(false);
        btn.setVisibility(View.INVISIBLE);
        btn = (Button) findViewById(R.id.d21);
        btn.setEnabled(false);
        btn.setVisibility(View.INVISIBLE);
        btn = (Button) findViewById(R.id.d22);
        btn.setEnabled(false);
        btn.setVisibility(View.INVISIBLE);
        btn = (Button) findViewById(R.id.d23);
        btn.setEnabled(false);
        btn.setVisibility(View.INVISIBLE);
        btn = (Button) findViewById(R.id.d24);
        btn.setEnabled(false);
        btn.setVisibility(View.INVISIBLE);
        btn = (Button) findViewById(R.id.d25);
        btn.setEnabled(false);
        btn.setVisibility(View.INVISIBLE);
        btn = (Button) findViewById(R.id.d26);
        btn.setEnabled(false);
        btn.setVisibility(View.INVISIBLE);
    }

    public void onClickMenu(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void onClickRebet(View v) {
        if (!point) {
            pBet.setText(rebet);
        }
    }

    private void getRebet() {
        String passBetStr = pBet.getText().toString();
        if (passBetStr.equals("")) {

        } else {
            if (Integer.parseInt(passBetStr) != 0) {
                rebet = passBetStr;
            }
        }
    }
}
