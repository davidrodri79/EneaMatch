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
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class userPhotoFragment extends Fragment {

    ImageView photoImageView;

    AppViewModel appViewModel;
    NavController navController;   // <-----------------

    Profile viewingProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_photo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        navController = Navigation.findNavController(view);  // <-----------------

        viewingProfile = appViewModel.getViewingProfile();

        photoImageView = view.findViewById(R.id.photoImageView);

        int selectedPicture = appViewModel.getSelectedPicture();

        if (viewingProfile.photoUrl.get(selectedPicture) != null)
            Glide.with(requireView()).load(viewingProfile.photoUrl.get(selectedPicture)).into(photoImageView);

    }
}