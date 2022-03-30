package com.example.eneamatch;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class AppViewModel extends AndroidViewModel {

    public interface RetrieveProfileCallback {
        void onProfileRetrieved(Profile profile);
    }

    private Profile userProfile = null, viewingProfile = null;
    private boolean profileRetrieved = false;

    public AppViewModel(@NonNull Application application)
    {
        super(application);
    }

    public void clearRetrievedProfile() { profileRetrieved = false; }

    public boolean isProfileRetrieved()
    {
        return profileRetrieved;
    }

    public Profile getUserProfile()
    {
        return userProfile;
    }

    public void retrieveUserProfile(RetrieveProfileCallback callback)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference docRef = FirebaseFirestore.getInstance().collection("profiles").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userProfile = documentSnapshot.toObject(Profile.class);
                profileRetrieved = true;

                callback.onProfileRetrieved(userProfile);
            }
        });
    }

    public void setUserProfile(Profile p)
    {
        userProfile = p;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore.getInstance().collection("profiles")
                .document(user.getUid()).set(userProfile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //navController.popBackStack();
                    }
                });
    }

    public Profile getViewingProfile()
    {
        return viewingProfile;
    }

    public void setViewingProfile(Profile p)
    {
        viewingProfile = p;
    }
}
