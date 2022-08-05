package com.example.ciante_mclemore_homework1;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.ciante_mclemore_homework1.databinding.FragmentMovieDetailsBinding;
import com.google.gson.Gson;

import Models.Movie;
import Models.RatedMovie;

public class MovieDetailsFragment extends Fragment implements RatingDialog.RateMovieDialogListener {

    private FragmentMovieDetailsBinding viewModel;
    private Gson gson;
    private Context context;
    private Movie movieInstance;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = FragmentMovieDetailsBinding.inflate(inflater, container, false);
        context = container.getContext();
        View view = viewModel.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize an instance of our gson mapper
        gson = new Gson();

        // get the arguments from the the bundle and map the movie class
        String movieDetails = MovieDetailsFragmentArgs.fromBundle(getArguments()).getMovieDetailResult();
        movieInstance = gson.fromJson(movieDetails, Movie.class);

        // Determine if the user has rated the movie already
        for(RatedMovie movie : MainActivity.USER_RATED_MOVIES) {
            if(movieInstance.title.contentEquals(movie.title)) {
                viewModel.movieDetailsRatingBar.setVisibility(View.VISIBLE);
                viewModel.movieDetailsRatingBar.setRating(movie.getUserRating());
                break;
            }
        }

        // bind the movie data to the layout and setup the save button
        bindMovieData(movieInstance);
        setupSaveButton();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel = null;
    }

    private void bindMovieData(Movie movie) {
        viewModel.movieTitle.setText(movieInstance.title);
        viewModel.movieInfo.setText(movieInstance.year+" "+"\u2022"+" "+movieInstance.rated+" "+"\u2022"+" "+movieInstance.runtime);
        viewModel.movieGenre.setText(movieInstance.genre);
        viewModel.moviePlot.setText(movieInstance.plot);
        viewModel.movieImdbRating.setText("IMDb Rating: " + movieInstance.imdbRating +"/10");
        viewModel.movieMetascore.setText("Metascore: " + movieInstance.metascore + "/100");

        if(viewModel.movieDetailsRatingBar.getVisibility() != View.VISIBLE) {
            // if the movie instance has not been rated, set the 'Rate' button visible
            viewModel.saveRatingButton.setVisibility(View.VISIBLE);
        }

        if(movieInstance.poster.contentEquals("N/A")) {
            viewModel.moviePoster.setImageResource(R.drawable.ic_no_image_placeholder);
        }
        else{
            Glide.with(this)
                    .load(movieInstance.poster)
                    .onlyRetrieveFromCache(true)
                    .placeholder(R.drawable.ic_loading)
                    .into(viewModel.moviePoster);
        }
    }

    private void setupSaveButton() {
        viewModel.saveRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open the custom rating dialog
                RatingDialog ratingDialog = new RatingDialog(movieInstance);
                ratingDialog.show(getChildFragmentManager(), null);
            }
        });
    }

    @Override
    public void onDialogPositiveClick(int rating) {
        viewModel.saveRatingButton.setVisibility(View.INVISIBLE);
        viewModel.movieDetailsRatingBar.setVisibility(View.VISIBLE);
        viewModel.movieDetailsRatingBar.setRating(rating);
    }
}