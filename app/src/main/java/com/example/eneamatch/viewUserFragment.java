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

    TextView nickTextView, genderTextView, ageTextView, eneatypeTextView, subtypeTextView, aboutTextView;
    ImageView photoImageView[] = new ImageView[Profile.PROFILE_NUM_PICTURES];
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
        eneatypeTextView = view.findViewById(R.id.eneatypeTextView);
        subtypeTextView = view.findViewById(R.id.subtypeTextView);
        aboutTextView = view.findViewById(R.id.aboutTextView);
        int widgetIds[]={R.id.photoImageView0, R.id.photoImageView1, R.id.photoImageView2,
                R.id.photoImageView3, R.id.photoImageView4, R.id.photoImageView5,};
        for(int i = 0; i < Profile.PROFILE_NUM_PICTURES; i++)
            photoImageView[i] = view.findViewById(widgetIds[i]);
        chatButton = view.findViewById(R.id.chatButton);

        String[] genresArray = getResources().getStringArray(R.array.genres_array);
        String[] eneatypesArray = getResources().getStringArray(R.array.eneatype_array);
        String[] subtypesArray = getResources().getStringArray(R.array.subtype_array);

        nickTextView.setText(viewingProfile.nick);
        ageTextView.setText(viewingProfile.age+" años");
        genderTextView.setText(genresArray[viewingProfile.gender]);
        eneatypeTextView.setText(eneatypesArray[viewingProfile.eneatype]);
        subtypeTextView.setText(subtypesArray[viewingProfile.subtype]);
        aboutTextView.setText(viewingProfile.aboutMe);
        for(int i = 0; i < Profile.PROFILE_NUM_PICTURES; i++) {

            if (viewingProfile.photoUrl.get(i) != null)
                Glide.with(requireView()).load(viewingProfile.photoUrl.get(i)).into(photoImageView[i]);
        }

        photoImageView[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { appViewModel.setSelectedPicture(0); navController.navigate(R.id.userPhotoFragment); }
        });
        photoImageView[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { appViewModel.setSelectedPicture(1); navController.navigate(R.id.userPhotoFragment); }
        });
        photoImageView[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { appViewModel.setSelectedPicture(2); navController.navigate(R.id.userPhotoFragment); }
        });
        photoImageView[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { appViewModel.setSelectedPicture(3); navController.navigate(R.id.userPhotoFragment); }
        });
        photoImageView[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { appViewModel.setSelectedPicture(4); navController.navigate(R.id.userPhotoFragment); }
        });
        photoImageView[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { appViewModel.setSelectedPicture(5); navController.navigate(R.id.userPhotoFragment); }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.userChatFragment);
            }
        });
    }
}