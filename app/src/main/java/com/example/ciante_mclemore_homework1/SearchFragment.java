package com.example.ciante_mclemore_homework1;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ciante_mclemore_homework1.databinding.FragmentSearchBinding;
import com.google.gson.Gson;

import java.io.IOException;

import Models.BadRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding viewModel;
    private OkHttpClient client;
    private Gson gson;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = FragmentSearchBinding.inflate(inflater, container, false);
        View view = viewModel.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize an instance of our gson mapper
        gson = new Gson();

        // Initialize an instance of the OkHttpClient
        client = new OkHttpClient();

        // Set up text listener for edit text component
        initializeSearchBar();

        // Set up the search button's listener
        initializeSearchButton();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Clear any past UI fields
        viewModel.searchErrorResult.setText("");
        viewModel.searchBar.getText().clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel = null;
    }

    private void initializeSearchBar() {
        // This allows the edit text to respond to the enter key as well to invoke an action
        viewModel.searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    viewModel.searchButton.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    private void initializeSearchButton() {
        // on click, this method call a function to prepare a request to an api endpoint to receive movie data
        viewModel.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    search();
                } catch (Exception e) {
                    UpdateUIOnBadRequest("Open Movie API is down, try later!");
                    e.printStackTrace();
                }
            }
        });
    }

    private void search() throws Exception {
        // This method gets the user's search query, and executes a request to a movie database
        // Once the query is processed, the data will be saved and a new activity opened
        String userQuery = viewModel.searchBar.getText().toString();
        Request request = new Request.Builder()
                .url("https://www.omdbapi.com/?s="+userQuery+"&apikey="+BuildConfig.OMDb_API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Response was not successful");

                    // Here we will determine if the response body is not an error and update the UI appropriately
                    // Must store json response string to variable because it can only be called once from okHttp. It does not store in memory once called
                    String responseJson = responseBody.string();
                    BadRequest badRequest = gson.fromJson(responseJson, BadRequest.class);

                    if(badRequest.error == null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                NavDirections action = SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(responseJson);
                                Navigation.findNavController(viewModel.getRoot()).navigate(action);
                            }
                        });
                    }
                    else{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UpdateUIOnBadRequest(badRequest.error);
                            }
                        });
                    }
                }
                catch (Exception e){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UpdateUIOnBadRequest(e.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void UpdateUIOnBadRequest(String message){
        viewModel.searchErrorResult.setTextColor(Color.RED);
        viewModel.searchErrorResult.setText(message);
    }
}