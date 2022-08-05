package com.example.ciante_mclemore_homework1;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ciante_mclemore_homework1.databinding.FragmentSearchResultBinding;
import com.google.gson.Gson;

import Models.MovieSearch;

public class SearchResultFragment extends Fragment {

    private FragmentSearchResultBinding viewModel;
    private Gson gson;
    private Context context;
    private MovieSearch movieSearch;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = FragmentSearchResultBinding.inflate(inflater, container, false);
        context = container.getContext();
        View view = viewModel.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize an instance of our gson mapper
        gson = new Gson();
        String movieSearchResults = SearchResultFragmentArgs.fromBundle(getArguments()).getMovieSearchResults();
        movieSearch = gson.fromJson(movieSearchResults, MovieSearch.class);

        MovieAdapter movieAdapter = new MovieAdapter(context, movieSearch.movieSearchResults);
        viewModel.movieRecyclerView.setAdapter(movieAdapter);
        viewModel.movieRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel = null;
    }
}