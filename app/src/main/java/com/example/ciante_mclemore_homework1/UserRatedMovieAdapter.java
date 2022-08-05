package com.example.ciante_mclemore_homework1;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import Models.RatedMovie;

public class UserRatedMovieAdapter extends FragmentStateAdapter {
    ArrayList<RatedMovie> userRatedMovies;

    public UserRatedMovieAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, ArrayList<RatedMovie> movies) {
        super(fragmentManager, lifecycle);
        userRatedMovies = movies;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        RatedMovieFragment ratedMovieFragment = new RatedMovieFragment(
                userRatedMovies.get(position).title,
                userRatedMovies.get(position).getUserRating(),
                userRatedMovies.get(position).poster);
        return ratedMovieFragment;
    }

    @Override
    public int getItemCount() {
        return userRatedMovies.size();
    }
}
