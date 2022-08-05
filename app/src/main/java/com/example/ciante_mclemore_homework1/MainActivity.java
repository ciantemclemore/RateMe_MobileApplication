package com.example.ciante_mclemore_homework1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.ciante_mclemore_homework1.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import Models.Movie;
import Models.RatedMovie;

public class MainActivity extends AppCompatActivity {
    public final static ArrayList<RatedMovie> USER_RATED_MOVIES = new ArrayList<>();
    private ActivityMainBinding viewModel;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bind the viewModel to the this instance and set content view
        viewModel = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewModel.getRoot());

        // set initial loaded data for application to use
        getIntentData();

        // Load the apps saved data
        setUpNavigationComponent();
        setUpBottomNavigationView();
    }

    private void setUpNavigationComponent() {
        // Set up navigation component to work with menu items
        NavHostFragment navHostFragment = viewModel.fragmentContainerView.getFragment();
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(viewModel.bottomNavigationView, navController);
    }

    private void setUpBottomNavigationView() {
        viewModel.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.rated_menu_item && id != viewModel.bottomNavigationView.getSelectedItemId()) {
                    navController.navigate(R.id.ratedMovies);
                }
                else if(id == R.id.search_menu_item && id != viewModel.bottomNavigationView.getSelectedItemId()) {
                    navController.navigateUp();
                }
                return true;
            }
        });
    }

    private void getIntentData() {
        if(getIntent() != null) {
            Gson gson = new Gson();
            String appDataJson = getIntent().getStringExtra(BuildConfig.USER_APP_DATA_FILENAME);

            if(appDataJson != null) {
                Type collectionType = new TypeToken<ArrayList<RatedMovie>>(){}.getType();
                ArrayList<RatedMovie> userRatedMovies = gson.fromJson(appDataJson, collectionType);
                for(RatedMovie movie : userRatedMovies) {
                    USER_RATED_MOVIES.add(movie);
                }
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        // Save the user's data
        Gson gson = new Gson();
        String userAppJsonData = gson.toJson(USER_RATED_MOVIES);
        try(FileOutputStream fos = openFileOutput(BuildConfig.USER_APP_DATA_FILENAME, Context.MODE_PRIVATE)){
            fos.write(userAppJsonData.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}