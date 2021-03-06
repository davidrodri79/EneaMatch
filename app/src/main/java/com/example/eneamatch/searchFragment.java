package com.example.eneamatch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;


public class searchFragment extends Fragment {

    NavController navController;   // <-----------------
    public AppViewModel appViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);  // <-----------------

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        if(!appViewModel.isProfileRetrieved())
            appViewModel.retrieveUserProfile(new AppViewModel.RetrieveProfileCallback() {
                @Override
                public void onProfileRetrieved(Profile profile) {
                    if(profile == null) {
                        //navController.navigate(R.id.profileFragment);
                    }
                }
            });

        if(!appViewModel.isSearchRetrieved())
            appViewModel.retrieveUserSearch();

        RecyclerView postsRecyclerView = view.findViewById(R.id.postsRecyclerView);

        Search userSearch = appViewModel.getUserSearch();
        if(userSearch == null)
            userSearch = new Search();

        ArrayList<Integer> wantedEneatypes = new ArrayList<Integer>();

        wantedEneatypes.add(0);
        for(int i = 0; i < 9; i++)
        {
            if(userSearch.eneatypes.get(i))
            wantedEneatypes.add(i+1);
        }

        Query query = FirebaseFirestore.getInstance().collection("profiles")
                .whereEqualTo("gender", userSearch.gender)
                //.whereIn("gender", Arrays.asList(0,1,2))
                .whereIn("eneatype", wantedEneatypes)
                .whereGreaterThanOrEqualTo("age", userSearch.minAge)
                .whereLessThanOrEqualTo("age", userSearch.maxAge)
                .limit(50);

        FirestoreRecyclerOptions<Profile> options = new FirestoreRecyclerOptions.Builder<Profile>()
                .setQuery(query, Profile.class)
                .setLifecycleOwner(this)
                .build();

        postsRecyclerView.setAdapter(new ProfileAdapter(options));

    }

    class ProfileAdapter extends FirestoreRecyclerAdapter<Profile, ProfileAdapter.PostViewHolder> {
        public ProfileAdapter(@NonNull FirestoreRecyclerOptions<Profile> options) {super(options);}

        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_profile, parent, false));
        }

        @Override
        protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull final Profile profile) {

            String[] genresArray = getResources().getStringArray(R.array.genres_array);
            String[] eneatypeArray = getResources().getStringArray(R.array.eneatype_array);
            String[] subtypeArray = getResources().getStringArray(R.array.subtype_array);
            if(profile.photoUrl.get(0) != null)
                Glide.with(getContext()).load(profile.photoUrl.get(0))/*.circleCrop()*/.into(holder.authorPhotoImageView);
            holder.nickTextView.setText(profile.nick);
            holder.ageTextView.setText(genresArray[profile.gender] +", "+profile.age+" a??os");
            holder.eneatypeTextView.setText(eneatypeArray[profile.eneatype] + " " + subtypeArray[profile.subtype]);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appViewModel.setViewingProfile(profile);
                    navController.navigate(R.id.viewUserFragment);
                }
            });
        }

        class PostViewHolder extends RecyclerView.ViewHolder {
            ImageView authorPhotoImageView;
            TextView eneatypeTextView, nickTextView, ageTextView;

            PostViewHolder(@NonNull View itemView) {
                super(itemView);

                authorPhotoImageView = itemView.findViewById(R.id.photoImageView);
                nickTextView = itemView.findViewById(R.id.nickTextView);
                ageTextView = itemView.findViewById(R.id.ageTextView);
                eneatypeTextView = itemView.findViewById(R.id.eneatypeTextView);
            }
        }
    }
}