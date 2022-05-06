package com.example.eneamatch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class userChatFragment extends Fragment {

    NavController navController;   // <-----------------
    public AppViewModel appViewModel;
    String myUID, otherUID;
    Button sendButton;
    EditText msgText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        navController = Navigation.findNavController(view);  // <-----------------

        RecyclerView chatRecyclerView = view.findViewById(R.id.chatRecyclerView);

        Query query = FirebaseFirestore.getInstance().collection("chats")
                .limit(50);

        FirestoreRecyclerOptions<Profile> options = new FirestoreRecyclerOptions.Builder<Profile>()
                .setQuery(query, Profile.class)
                .setLifecycleOwner(this)
                .build();

        chatRecyclerView.setAdapter(new userChatFragment.ChatAdapter(options));

        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        otherUID = appViewModel.getViewingProfile().uid;

        sendButton = view.findViewById(R.id.sendButton);
        msgText = view.findViewById(R.id.chatMsg);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ChatMsg chatMsg = new ChatMsg(myUID, msgText.getText().toString());

                FirebaseFirestore.getInstance().collection("chats")
                        .document(myUID + "-" + otherUID)
                        .collection("messages")
                        .document("1")
                        .set(chatMsg)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //navController.popBackStack();
                            }
                        });
            }
        });
    }

    class ChatAdapter extends FirestoreRecyclerAdapter<ChatMsg, userChatFragment.ChatAdapter.ChatMsgViewHolder> {
        public ChatAdapter(@NonNull FirestoreRecyclerOptions<ChatMsg> options) {super(options);}

        @NonNull
        @Override
        public userChatFragment.ChatAdapter.ChatMsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new userChatFragment.ChatAdapter.ChatMsgViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_chatmsg, parent, false));
        }

        @Override
        protected void onBindViewHolder(@NonNull userChatFragment.ChatAdapter.ChatMsgViewHolder holder, int position, @NonNull final ChatMsg msg) {

            /*String[] genresArray = getResources().getStringArray(R.array.genres_array);
            if(profile.photoUrl != null)
                Glide.with(getContext()).load(profile.photoUrl).into(holder.authorPhotoImageView);
            holder.nickTextView.setText(profile.nick);
            holder.ageTextView.setText(genresArray[profile.gender] +", "+profile.age+" años");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appViewModel.setViewingProfile(profile);
                    navController.navigate(R.id.viewUserFragment);
                }
            });*/
        }

        class ChatMsgViewHolder extends RecyclerView.ViewHolder {

            //TextView nickTextView, ageTextView;

            ChatMsgViewHolder(@NonNull View itemView) {
                super(itemView);

                //ageTextView = itemView.findViewById(R.id.ageTextView);
            }
        }
    }
}