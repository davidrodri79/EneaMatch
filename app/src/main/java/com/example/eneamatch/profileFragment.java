package com.example.eneamatch;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.UUID;

public class profileFragment extends Fragment {

    /*ImageView photoImageView;
    TextView displayNameTextView, emailTextView;*/
    EditText nickTextView, genderTextView, ageTextView, aboutTextView;
    ImageView photoImageView[] = new ImageView[Profile.PROFILE_NUM_PICTURES];
    Spinner genderSpinner, eneatypeSpinner, subtypeSpinner;
    Button saveProfileButton;
    NavController navController;   // <-----------------
    Profile userProfile;

    public AppViewModel appViewModel;

    int currentPhotoId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        navController = Navigation.findNavController(view);  // <-----------------

        int photo_widgets[] = {R.id.photoImageView0, R.id.photoImageView1,R.id.photoImageView2, R.id.photoImageView3, R.id.photoImageView4, R.id.photoImageView5,};
        for(int i = 0; i < Profile.PROFILE_NUM_PICTURES; i++)
            photoImageView[i] = view.findViewById(photo_widgets[i]);
        nickTextView = view.findViewById(R.id.nickEditText);
        ageTextView = view.findViewById(R.id.ageEditText);
        aboutTextView = view.findViewById(R.id.aboutEditText);
        saveProfileButton = view.findViewById(R.id.saveProfileButton);

        /*photoImageView = view.findViewById(R.id.photoImageView);
        displayNameTextView = view.findViewById(R.id.displayNameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);*/

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        /*if(user != null){
            displayNameTextView.setText(user.getDisplayName());
            emailTextView.setText(user.getEmail());

            Glide.with(requireView()).load(user.getPhotoUrl()).into(photoImageView);
        }*/

        genderSpinner = (Spinner) view.findViewById(R.id.genreSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.genres_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        genderSpinner.setAdapter(adapter);

        eneatypeSpinner = (Spinner) view.findViewById(R.id.eneatypeSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.eneatype_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        eneatypeSpinner.setAdapter(adapter2);

        subtypeSpinner = (Spinner) view.findViewById(R.id.subtypeSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(),
                R.array.subtype_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        subtypeSpinner.setAdapter(adapter3);


        if(appViewModel.isProfileRetrieved())
        {
            userProfile = appViewModel.getUserProfile();

            if(userProfile == null) {
                userProfile = new Profile();
                userProfile.uid = user.getUid();
            }

            for(int i = 0; i < Profile.PROFILE_NUM_PICTURES; i++) {
                if (userProfile.photoUrl.get(i) != null)
                    Glide.with(requireView()).load(userProfile.photoUrl.get(i)).into(photoImageView[i]);
            }
            nickTextView.setText(userProfile.nick);
            ageTextView.setText("" + userProfile.age);
            genderSpinner.setSelection(userProfile.gender);
            eneatypeSpinner.setSelection(userProfile.eneatype);
            subtypeSpinner.setSelection(userProfile.subtype);
            aboutTextView.setText(userProfile.aboutMe);
        }
        else
        {
            userProfile = new Profile();
            userProfile.uid = user.getUid();
        }


        photoImageView[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                currentPhotoId = 0; seleccionarImagen();
            }
        });

        photoImageView[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                currentPhotoId = 1; seleccionarImagen();
            }
        });

        photoImageView[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                currentPhotoId = 2; seleccionarImagen();
            }
        });

        photoImageView[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                currentPhotoId = 3; seleccionarImagen();
            }
        });

        photoImageView[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                currentPhotoId = 4; seleccionarImagen();
            }
        });

        photoImageView[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                currentPhotoId = 5; seleccionarImagen();
            }
        });

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean error = false;

                if(TextUtils.isEmpty( nickTextView.getText().toString() ))
                {
                    nickTextView.setError("Este campo es obligatorio");
                    error = true;
                }
                if(Integer.parseInt( ageTextView.getText().toString() ) < 18)
                {
                    ageTextView.setError("La edad m??nima es 18");
                    error = true;
                }

                if(!error) {
                    updateProfile(user);

                }
            }
        });

    }

    void updateProfile(FirebaseUser user)
    {
        userProfile.nick = nickTextView.getText().toString();
        userProfile.gender = genderSpinner.getSelectedItemPosition();
        userProfile.eneatype = eneatypeSpinner.getSelectedItemPosition();
        userProfile.subtype = subtypeSpinner.getSelectedItemPosition();

        userProfile.age = Integer.parseInt( ageTextView.getText().toString() );
        userProfile.aboutMe = aboutTextView.getText().toString();

        appViewModel.setUserProfile(userProfile);

    }

    private final ActivityResultLauncher<String> galeria =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                //appViewModel.setMediaSeleccionado(uri, mediaTipo);


                //Delete previous picture
                if(userProfile.photoUrl.get(currentPhotoId) != null)
                    FirebaseStorage.getInstance().getReference(userProfile.uid + "/pictures/" + currentPhotoId).delete();

                // Upload new picture
                FirebaseStorage.getInstance().getReference(userProfile.uid + "/pictures/" + currentPhotoId)
                        .putFile(uri)
                        .continueWithTask(task ->
                                task.getResult().getStorage().getDownloadUrl())
                        .addOnSuccessListener(url -> {
                            userProfile.photoUrl.set(currentPhotoId, url.toString());

                            Glide.with(requireView()).load(userProfile.photoUrl.get(currentPhotoId)).into(photoImageView[currentPhotoId]);
                            });
            });

    private void seleccionarImagen() {
        //mediaTipo = "image";
        galeria.launch("image/*");
    }
}