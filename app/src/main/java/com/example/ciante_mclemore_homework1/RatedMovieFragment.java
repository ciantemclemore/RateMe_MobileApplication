package com.example.ciante_mclemore_homework1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.ciante_mclemore_homework1.databinding.FragmentRatedMovieBinding;

public class RatedMovieFragment extends Fragment {
    FragmentRatedMovieBinding viewModel;
    String movieTitle, moviePosterUrl;
    int movieRating;

    public RatedMovieFragment(String movieTitle, int movieRating, String moviePosterUrl) {
        this.movieTitle = movieTitle;
        this.moviePosterUrl = moviePosterUrl;
        this.movieRating = movieRating;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = FragmentRatedMovieBinding.inflate(inflater, container, false);
        View view = viewModel.getRoot();
        viewModel.ratedMovieTitle.setText(movieTitle);
        viewModel.ratedMovieRateBar.setRating(movieRating);
        Glide.with(this)
                .load(moviePosterUrl)
                .placeholder(R.drawable.ic_loading)
                .into(viewModel.ratedMoviePoster);
        return view;
    }

    public void onDestroyView() {
        super.onDestroyView();
        viewModel = null;
    }
}