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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class viewUserFragment extends Fragment {

    TextView nickTextView, genderTextView, ageTextView, aboutTextView;
    ImageView photoImageView;
    Button chatButton;

    AppViewModel appViewModel;
    NavController navController;   // <-----------------

    Profile viewingProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        navController = Navigation.findNavController(view);  // <-----------------

        viewingProfile = appViewModel.getViewingProfile();

        nickTextView = view.findViewById(R.id.nickTextView);
        ageTextView = view.findViewById(R.id.ageTextView);
        genderTextView = view.findViewById(R.id.genreTextView);
        aboutTextView = view.findViewById(R.id.aboutTextView);
        photoImageView = view.findViewById(R.id.photoImageView);
        chatButton = view.findViewById(R.id.chatButton);

        String[] genresArray = getResources().getStringArray(R.array.genres_array);

        nickTextView.setText(viewingProfile.nick);
        ageTextView.setText(viewingProfile.age+" años");
        genderTextView.setText(genresArray[viewingProfile.gender]);
        aboutTextView.setText(viewingProfile.aboutMe);
        if(viewingProfile.photoUrl != null)
            Glide.with(requireView()).load(viewingProfile.photoUrl).into(photoImageView);

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.userChatFragment);
            }
        });
    }
}