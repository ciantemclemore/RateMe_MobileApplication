package com.example.ciante_mclemore_homework1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import Models.Movie;
import Models.RatedMovie;

public class RatingDialog extends DialogFragment {

    public interface RateMovieDialogListener {
        void onDialogPositiveClick(int rating);
    }

    private Movie movie;
    RateMovieDialogListener listener;

    public RatingDialog(Movie movie) {
        this.movie = movie;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        try{
            listener = (RateMovieDialogListener) getParentFragment() ;
        }catch(ClassCastException e){
            // the host class did not implement the interface
            Log.d("RateMovieDialogListener", "Host class must implement the interface");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.rating_dialog, null);

        builder.setView(view)
                .setTitle(R.string.rateDialog_title)
                .setNegativeButton(R.string.rateDialog_negativeButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RatingDialog.this.getDialog().cancel();
                    }
                })
                .setPositiveButton(R.string.rateDialog_positiveButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Get the rating from the rating bar
                        int rating = (int)((RatingBar) view.findViewById(R.id.ratingBar)).getRating();

                        // Save the rated movie
                        RatedMovie ratedMovie = new RatedMovie(movie, rating);

                        // Convert the rated movie to json string and store
                        MainActivity.USER_RATED_MOVIES.add(ratedMovie);

                        // send the rating back down to the host fragment
                        listener.onDialogPositiveClick(rating);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
