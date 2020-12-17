package com.example.zpo_l10_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private int seconds;
    private int period; // time of one period
    private double mass; // mass of an element
    private boolean running;
    private boolean wasRunning;
    private int periodCount = 0; // counting periods
    private boolean clicked = false; // if start clicked even once

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }

        // wywołanie metody
        runTimer();
    }

    private void runTimer() {

        final TextView massView = (TextView) findViewById(R.id.tvMassVal);
        final TextView periodView = (TextView) findViewById(R.id.tvPeriodVal);
        final TextView timeView = (TextView) findViewById(R.id.tvTimeVal);

        // handler, planowanie wykonania kodu, wykonanie kodu w innym oknie niż główne okno aplikacji
        // tu kolejkuje sekundy
        final Handler handler = new Handler(Looper.getMainLooper());
        // chcesz żeby było wykonane możliwie jak najszybciej

        handler.post(new Runnable() {
                         @Override
                         public void run() {

                             int hours = seconds / 3600;
                             int minutes = (seconds % 3600) / 60;
                             int secs = seconds % 60;

                             String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                             timeView.setText(time);
                             periodView.setText(Integer.toString(periodCount));
                             massView.setText(Double.toString(mass));

                             Log.d("licznik", String.valueOf(seconds));

                             // liczenie czasu, opóźnienie co 1 sec = 1000 msec
                             if (running) {
                                 seconds++;
                                 if (seconds % period == 0) {
                                     mass /= 2;
                                     periodCount++;
                                 }
                             }

                             // opóźniamy obiekt klasy runnable, przez jak długi czas, unit: msec
                             handler.postDelayed(this, 1000);
                         }
                     }
        );
    }

    public void onClickStart(View view) {
        final EditText enterMass = (EditText) findViewById(R.id.enterMass);
        final EditText enterPeriod = (EditText) findViewById(R.id.enterPeriod);

        try {
            int stPeriod = Integer.parseInt(String.valueOf(enterPeriod.getText()));
            double stMass = Double.parseDouble(String.valueOf(enterMass.getText()));

            if (clicked == false) { // get mass and period from user only on first click
                mass = stMass;
                period = stPeriod;
                clicked = true;
            }

            running = true;
        } catch (NumberFormatException e) {
            enterMass.setText("Error, wrong format");
            enterPeriod.setText("Error, wrong format");
        }
    }

    public void onClickStop(View view) {
        running = false;
    }

    public void onClickReset(View view) {
        running = false;
        seconds = 0;
        period = 0;
        mass = 0;
        periodCount = 0;
        clicked = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        wasRunning = running;
        running = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (wasRunning)
            running = true;
    }

    @Override
    // bundle - klasa pozwalająca na grupowanie danych w całość
    // i zapiswanie / odczytywanie danych (wartości) za pomocą kluczy
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        // zapisuje stan aplikacji, poniższe dane:
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putInt("periodCount", periodCount);
        savedInstanceState.putInt("period", period);
        savedInstanceState.putDouble("mass", mass);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("WasRunning", wasRunning);
        super.onSaveInstanceState(savedInstanceState);
    }
}