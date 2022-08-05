package com.example.ciante_mclemore_homework1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import Models.Movie;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // hide the action bar
        getSupportActionBar().hide();

        // Load the apps saved data
        LoadInitialAppData();
    }

    private void LoadInitialAppData() {
        FileInputStream fis = null;
        try {
            fis = openFileInput(BuildConfig.USER_APP_DATA_FILENAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(fis != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line);
                    line = reader.readLine();
                }
                // Convert the string to the arraylist of user movies
                fis.close();
                startApplication(stringBuilder.toString());
            } catch (IOException e) {
                // Error occurred when opening raw file for reading.
            }
        }
        else {
            startApplication(null);
        }
    }

    @Nullable
    private void startApplication(String intentData) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);

                if(intentData != null){
                    intent.putExtra(BuildConfig.USER_APP_DATA_FILENAME, intentData);
                }
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
