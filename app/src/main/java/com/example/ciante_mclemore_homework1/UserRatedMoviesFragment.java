package com.example.ciante_mclemore_homework1;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ciante_mclemore_homework1.databinding.FragmentUserRatedMoviesBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class UserRatedMoviesFragment extends Fragment {

    private FragmentUserRatedMoviesBinding viewModel;
    private Context context;

    public UserRatedMoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = FragmentUserRatedMoviesBinding.inflate(inflater, container, false);
        context = container.getContext();
        View view = viewModel.getRoot();

        // This callback will handle onBackPressed calls for this top level view
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if ( Navigation.findNavController(viewModel.getRoot()).getCurrentDestination().getId() == R.id.rated_menu_item) {
                    // we do not want to navigate to a previous fragment
                    return;
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(MainActivity.USER_RATED_MOVIES.size() == 0) {
            viewModel.noMoviesRated.setVisibility(View.VISIBLE);
            viewModel.viewPager.setVisibility(View.INVISIBLE);
            viewModel.viewPager.setVisibility(View.INVISIBLE);
        }
        else{
            viewModel.indicator.setVisibility(View.VISIBLE);
            viewModel.viewPager.setVisibility(View.VISIBLE);
            viewModel.noMoviesRated.setVisibility(View.INVISIBLE);
            UserRatedMovieAdapter userRatedMovieAdapter = new UserRatedMovieAdapter(getActivity().getSupportFragmentManager(), getLifecycle(), MainActivity.USER_RATED_MOVIES);
            viewModel.viewPager.setAdapter(userRatedMovieAdapter);
            viewModel.indicator.setViewPager(viewModel.viewPager);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel = null;
    }
}