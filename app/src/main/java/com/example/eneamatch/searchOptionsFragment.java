package com.example.eneamatch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bumptech.glide.Glide;

public class searchOptionsFragment extends Fragment {

    AppViewModel appViewModel;
    NavController navController;
    EditText minAgeEditText, maxAgeEditText;

    Search userSearch = null;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_options, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        navController = Navigation.findNavController(view);  // <-----------------

        minAgeEditText = view.findViewById(R.id.minAgeEditText);
        maxAgeEditText = view.findViewById(R.id.maxAgeEditText);

        if(appViewModel.isProfileRetrieved())
        {
            userSearch = appViewModel.getUserSearch();

            if(userSearch == null) {
                userSearch = new Search();
                //userSearch.uid = user.getUid();
            }


            minAgeEditText.setText("" + userSearch.minAge);
            maxAgeEditText.setText("" + userSearch.maxAge);
        }
        else
        {
            userSearch = new Search();
            //userProfile.uid = user.getUid();
        }
    }
}