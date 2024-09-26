package com.example.bettingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ModelNotificationAction extends AppCompatActivity {

    private TextView winnerText, resultText, detailText;
    private Button confirmButton, exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.model_notification);

        initializeViews();
        setupConfirmButton();
        setupExitButton();
        displayResult();
    }
    private void initializeViews(){
        winnerText = findViewById(R.id.winnerText);
        resultText = findViewById(R.id.winningAmountText);
        confirmButton = findViewById(R.id.confirmButton);
        detailText = findViewById(R.id.detailText);
        exitButton = findViewById(R.id.exitButton);
    }
    private void setupConfirmButton(){
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void setupExitButton(){
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ModelNotificationAction.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void displayResult(){
        int winner = getIntent().getIntExtra("winner", 0);
        int totalWinnings = getIntent().getIntExtra("totalWinnings", 0);
        int totalLosses = getIntent().getIntExtra("totalLosses", 0);
        int totalOutcome = getIntent().getIntExtra("totalOutcome", 0);

        winnerText.setText("Winner: Car " + winner);

        resultText.setText("Total Winnings: $" + totalWinnings + "\nTotal Losses: $" + totalLosses);

        if (totalOutcome > 0) {
            detailText.setText("You won: $" + totalOutcome);
        } else if (totalOutcome < 0) {
            detailText.setText("you lost: $" + Math.abs(totalOutcome));
        } else {
            detailText.setText("No Gain, No Loss.");
        }
    }
}
