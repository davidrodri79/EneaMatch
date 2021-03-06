package com.example.eneamatch;

import android.os.Bundle;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class searchOptionsFragment extends Fragment {

    AppViewModel appViewModel;
    NavController navController;
    EditText minAgeEditText, maxAgeEditText;
    Spinner genderSpinner;
    CheckBox eneatypeCheck[] = new CheckBox[9];
    Button saveSearchButton;

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        minAgeEditText = view.findViewById(R.id.minAgeEditText);
        maxAgeEditText = view.findViewById(R.id.maxAgeEditText);
        genderSpinner = view.findViewById(R.id.genreSpinner);
        eneatypeCheck[0] = view.findViewById(R.id.checkbox_e1);
        eneatypeCheck[1] = view.findViewById(R.id.checkbox_e2);
        eneatypeCheck[2] = view.findViewById(R.id.checkbox_e3);
        eneatypeCheck[3] = view.findViewById(R.id.checkbox_e4);
        eneatypeCheck[4] = view.findViewById(R.id.checkbox_e5);
        eneatypeCheck[5] = view.findViewById(R.id.checkbox_e6);
        eneatypeCheck[6] = view.findViewById(R.id.checkbox_e7);
        eneatypeCheck[7] = view.findViewById(R.id.checkbox_e8);
        eneatypeCheck[8] = view.findViewById(R.id.checkbox_e9);

        saveSearchButton = view.findViewById(R.id.saveSearchButton);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.genres_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        genderSpinner.setAdapter(adapter);


        saveSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean error = false;

                if(!error)
                    updateSearch(user);
            }
        });

        if(appViewModel.isSearchRetrieved())
        {
            userSearch = appViewModel.getUserSearch();

            if(userSearch == null) {
                userSearch = new Search();
                //userSearch.uid = user.getUid();
            }


            minAgeEditText.setText("" + userSearch.minAge);
            maxAgeEditText.setText("" + userSearch.maxAge);
            genderSpinner.setSelection( userSearch.gender );
            for(int i = 0; i < 9; i++)
                eneatypeCheck[i].setChecked( userSearch.eneatypes.get(i) );

        }
        else
        {
            userSearch = new Search();
            //userProfile.uid = user.getUid();
        }
    }

    void updateSearch(FirebaseUser user)
    {
        userSearch.gender = genderSpinner.getSelectedItemPosition();
        userSearch.minAge = Integer.parseInt( minAgeEditText.getText().toString() );
        userSearch.maxAge = Integer.parseInt( maxAgeEditText.getText().toString() );
        for(int i = 0; i < 9; i++)
            userSearch.eneatypes.set(i , eneatypeCheck[i].isChecked());

        appViewModel.setUserSearch(userSearch);

    }
}