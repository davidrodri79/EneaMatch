package com.example.eneamatch;

import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class userChatFragment extends Fragment {

    NavController navController;   // <-----------------
    public AppViewModel appViewModel;
    String myUID, otherUID;
    Button sendButton;
    EditText msgText;
    Profile myProfile, otherProfile;

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


        myProfile = appViewModel.getUserProfile();
        otherProfile = appViewModel.getViewingProfile();

        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        otherUID = otherProfile.uid;

        String chatId = myUID.compareTo( otherUID )  < 0 ? myUID + "-" + otherUID : otherUID + "-" + myUID;

        Query query = FirebaseFirestore.getInstance().collection("chats")
                .document(chatId)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .limit(50);

        FirestoreRecyclerOptions<ChatMsg> options = new FirestoreRecyclerOptions.Builder<ChatMsg>()
                .setQuery(query, ChatMsg.class)
                .setLifecycleOwner(this)
                .build();

        chatRecyclerView.setAdapter(new userChatFragment.ChatAdapter(options));


        sendButton = view.findViewById(R.id.sendButton);
        msgText = view.findViewById(R.id.chatMsg);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date currentTime = Calendar.getInstance().getTime();

                ChatMsg chatMsg = new ChatMsg(myUID, msgText.getText().toString(), currentTime.getTime());

                FirebaseFirestore.getInstance().collection("chats")
                        .document(chatId)
                        .collection("messages")
                        .add(chatMsg)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                //navController.popBackStack();
                            }
                        });

                FirebaseFirestore.getInstance().collection( "profiles")
                        .document(myUID)
                        .update("chats."+otherUID, true);

                FirebaseFirestore.getInstance().collection( "profiles")
                        .document(otherUID)
                        .update("chats."+myUID, true);

                msgText.setText("");
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
            holder.messageTextView.setText(msg.text);

            Date msgTime = new Date(msg.timestamp);
            Date currentTime = Calendar.getInstance().getTime();

            SimpleDateFormat simpleDate;

            if (msgTime.getDay() != currentTime.getDay() ||
                    msgTime.getMonth() != currentTime.getMonth() ||
                    msgTime.getYear() != currentTime.getYear()) {
                simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            }
            else
            {
                simpleDate = new SimpleDateFormat("HH:mm");
            }


            holder.timeTextView.setText( simpleDate.format( msgTime ));

            if(msg.uid.equals( myUID ))
            {
                if(myProfile.photoUrl.get(0) != null)
                    Glide.with(getContext()).load(myProfile.photoUrl.get(0)).circleCrop().into(holder.chatOwnPhoto);

                holder.chatCardView.setCardBackgroundColor(getResources().getColor(R.color.teal_700));
                holder.messageTextView.setTextColor(getResources().getColor(R.color.white));
                holder.timeTextView.setTextColor(getResources().getColor(R.color.white));
                holder.messageTextView.setGravity(Gravity.RIGHT);
                holder.timeTextView.setGravity(Gravity.RIGHT);
                holder.chatOtherPhoto.setVisibility(View.GONE);

            }
            else
            {
                if(otherProfile.photoUrl != null)
                    Glide.with(getContext()).load(otherProfile.photoUrl).circleCrop().into(holder.chatOtherPhoto);

                holder.chatCardView.setCardBackgroundColor(getResources().getColor(R.color.gray));
                holder.messageTextView.setTextColor(getResources().getColor(R.color.black));
                holder.timeTextView.setTextColor(getResources().getColor(R.color.black));
                holder.messageTextView.setGravity(Gravity.LEFT);
                holder.timeTextView.setGravity(Gravity.LEFT);
                holder.chatOwnPhoto.setVisibility(View.GONE);
            }

        }

        class ChatMsgViewHolder extends RecyclerView.ViewHolder {

            TextView messageTextView, timeTextView;
            CardView chatCardView;
            ImageView chatOwnPhoto, chatOtherPhoto;

            ChatMsgViewHolder(@NonNull View itemView) {
                super(itemView);

                chatCardView = itemView.findViewById(R.id.chatCard);
                messageTextView = itemView.findViewById(R.id.messageTextView);
                timeTextView = itemView.findViewById(R.id.timeTextView);
                chatOwnPhoto = itemView.findViewById(R.id.chatOwnPhoto);
                chatOtherPhoto = itemView.findViewById(R.id.chatOtherPhoto);

            }
        }
    }
}