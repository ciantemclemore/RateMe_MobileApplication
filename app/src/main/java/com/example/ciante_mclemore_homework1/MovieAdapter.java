package com.example.ciante_mclemore_homework1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Models.Movie;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    ArrayList<Movie> movies;
    HashMap<String, Bitmap> posterImages;
    Context context;
    private final OkHttpClient client = new OkHttpClient();

    public MovieAdapter(Context ct, ArrayList<Movie> movieResults){
        context = ct;
        movies = movieResults;
        posterImages = new HashMap<>();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_row_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        // Here you will set the text views and image views via the arraylist of movies
        Movie movieSearchResult = movies.get(position);
        holder.title.setText(movieSearchResult.title);
        holder.year.setText("Release: " + movieSearchResult.year);
        holder.type.setText("Type: " + movieSearchResult.type);
        if(movieSearchResult.poster.contentEquals("N/A")){
            holder.poster.setImageResource(R.drawable.ic_no_image_placeholder);
        }
        else{
            Glide.with(context)
                    .load(movieSearchResult.poster)
                    .placeholder(R.drawable.ic_loading)
                    .into(holder.poster);
        }

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here we will open a detailed view about the selected movie
                Request request = new Request.Builder()
                        .url("https://www.omdbapi.com/?t="+movieSearchResult.title+"&y="+movieSearchResult.year+"&apikey="+BuildConfig.OMDb_API_KEY)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override public void onResponse(Call call, Response response) throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful()) throw new IOException("Response was not successful");

                            // store the movie detail and send to the next view
                            String movieDetail = responseBody.string();

                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    NavDirections action = SearchResultFragmentDirections.actionSearchResultFragmentToMovieDetailsFragment(movieDetail);
                                    Navigation.findNavController(view).navigate(action);
                                }
                            });
                        }
                        catch (Exception e){
                            Toast.makeText(context, "Unable to fetch content", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView title, year, type;
        ImageView poster;
        ConstraintLayout rootLayout;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            // find the items by id or by view binding in here
            rootLayout = (ConstraintLayout) itemView.findViewById(R.id.rowItem_rootLayout);
            title = (TextView) itemView.findViewById(R.id.rowItem_title);
            year = (TextView) itemView.findViewById(R.id.rowItem_year);
            type = (TextView) itemView.findViewById(R.id.rowItem_type);
            poster = (ImageView) itemView.findViewById(R.id.rowItem_poster);
        }
    }
}
