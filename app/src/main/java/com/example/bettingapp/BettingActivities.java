package com.example.bettingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class BettingActivities extends AppCompatActivity {

    private TextView balanceTextView;
    private CheckBox car1CheckBox, car2CheckBox, car3CheckBox, car4CheckBox;
    private EditText car1BetAmount, car2BetAmount, car3BetAmount, car4BetAmount;
    private Button startButton, resetButton;
    private SeekBar car1SeekBar, car2SeekBar, car3SeekBar, car4SeekBar;

    private int balance = 1000;
    private boolean isRacing = false;
    private Handler handler = new Handler();
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.betting_place);

        initializeViews();
        setupListeners();
        updateBalance();
    }
    private void initializeViews() {
        balanceTextView = findViewById(R.id.balanceTextView);
        car1CheckBox = findViewById(R.id.checkBoxCar1);
        car2CheckBox = findViewById(R.id.checkBoxCar2);
        car3CheckBox = findViewById(R.id.checkBoxCar3);
        car4CheckBox = findViewById(R.id.checkBoxCar4);
        car1BetAmount = findViewById(R.id.betAmountCar1);
        car2BetAmount = findViewById(R.id.betAmountCar2);
        car3BetAmount = findViewById(R.id.betAmountCar3);
        car4BetAmount = findViewById(R.id.betAmountCar4);
        startButton = findViewById(R.id.startButton);
        resetButton = findViewById(R.id.resetButton);
        car1SeekBar = findViewById(R.id.seekBarCar1);
        car2SeekBar = findViewById(R.id.seekBarCar2);
        car3SeekBar = findViewById(R.id.seekBarCar3);
        car4SeekBar = findViewById(R.id.seekBarCar4);
    }

    private void setupListeners() {
        startButton.setOnClickListener(v -> startRace());
        resetButton.setOnClickListener(v -> resetBet());
    }

    private void updateBalance() {
        balanceTextView.setText("Balance: $" + balance);
    }

    private void startRace() {
        if (isRacing) return;

        int totalBet = getTotalBetAmount();
        if (totalBet <= 0) {
            Toast.makeText(this, "Invalid bet amount", Toast.LENGTH_SHORT).show();
            return;
        }
        if (totalBet > balance) {
            Toast.makeText(this, "Not enough money to bet", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!car1CheckBox.isChecked() && !car2CheckBox.isChecked() && !car3CheckBox.isChecked() && !car4CheckBox.isChecked()) {
            Toast.makeText(this, "Please select at least one car", Toast.LENGTH_SHORT).show();
            return;
        }

        resetButton.setEnabled(false);
        isRacing = true;
        balance -= totalBet;

        car1SeekBar.setProgress(0);
        car2SeekBar.setProgress(0);
        car3SeekBar.setProgress(0);
        car4SeekBar.setProgress(0);

        runRace();
    }

    private void runRace() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                car1SeekBar.setProgress(car1SeekBar.getProgress() + random.nextInt(5));
                car2SeekBar.setProgress(car2SeekBar.getProgress() + random.nextInt(5));
                car3SeekBar.setProgress(car3SeekBar.getProgress() + random.nextInt(5));
                car4SeekBar.setProgress(car4SeekBar.getProgress() + random.nextInt(5));

                if (car1SeekBar.getProgress() >= 100 || car2SeekBar.getProgress() >= 100 ||
                        car3SeekBar.getProgress() >= 100 || car4SeekBar.getProgress() >= 100) {
                    endRace();
                } else {
                    handler.postDelayed(this, 100);
                }
            }
        }, 100);
    }

    private void endRace() {
        isRacing = false;
        int winner = determineWinner();
        int totalWinnings = 0;
        int totalLosses = 0;

        if (car1CheckBox.isChecked()) {
            int betAmount = getBetAmount(car1BetAmount);
            if (winner == 1) {
                totalWinnings += betAmount;
            } else {
                totalLosses += betAmount;
            }
        }
        if (car2CheckBox.isChecked()) {
            int betAmount = getBetAmount(car2BetAmount);
            if (winner == 2) {
                totalWinnings += betAmount;
            } else {
                totalLosses += betAmount;
            }
        }
        if (car3CheckBox.isChecked()) {
            int betAmount = getBetAmount(car3BetAmount);
            if (winner == 3) {
                totalWinnings += betAmount;
            } else {
                totalLosses += betAmount;
            }
        }
        if (car4CheckBox.isChecked()) {
            int betAmount = getBetAmount(car4BetAmount);
            if (winner == 4) {
                totalWinnings += betAmount;
            } else {
                totalLosses += betAmount;
            }
        }

        resetButton.setEnabled(true);
        balance += totalWinnings;
        updateBalance();
        int totalOutcome = totalWinnings - totalLosses;

        Intent intent = new Intent(this, ModelNotificationAction.class);
        intent.putExtra("winner", winner);
        intent.putExtra("totalWinnings", totalWinnings);
        intent.putExtra("totalLosses", totalLosses);
        intent.putExtra("totalOutcome", totalOutcome);
        startActivity(intent);
        resetBet();
    }

    private int determineWinner() {
        int max = Math.max(car1SeekBar.getProgress(),
                Math.max(car2SeekBar.getProgress(),
                Math.max(car3SeekBar.getProgress(),
                car4SeekBar.getProgress())));
        if (max == car1SeekBar.getProgress()) return 1;
        if (max == car2SeekBar.getProgress()) return 2;
        if (max == car3SeekBar.getProgress()) return 3;
        return 4;
    }

    private void resetBet() {
        car1CheckBox.setChecked(false);
        car2CheckBox.setChecked(false);
        car3CheckBox.setChecked(false);
        car4CheckBox.setChecked(false);
        car1BetAmount.setText("");
        car2BetAmount.setText("");
        car3BetAmount.setText("");
        car4BetAmount.setText("");
        car1SeekBar.setProgress(0);
        car2SeekBar.setProgress(0);
        car3SeekBar.setProgress(0);
        car4SeekBar.setProgress(0);
    }

    private int getBetAmount(EditText editText) {
        try {
            return Integer.parseInt(editText.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int getTotalBetAmount() {
        return getBetAmount(car1BetAmount) + getBetAmount(car2BetAmount) +
                getBetAmount(car3BetAmount) + getBetAmount(car4BetAmount);
    }
}
