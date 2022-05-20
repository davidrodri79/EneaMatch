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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;


public class myChatsFragment extends Fragment {

    NavController navController;   // <-----------------
    public AppViewModel appViewModel;

    String myUID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);  // <-----------------

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        Profile myProfile = appViewModel.getUserProfile();

        myUID = myProfile.uid;

        Query query = FirebaseFirestore.getInstance().collection("profiles")
                .document( myUID )
                .collection( "chats")
                .limit(50)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatEntry> options = new FirestoreRecyclerOptions.Builder<ChatEntry>()
                .setQuery(query, ChatEntry.class)
                .setLifecycleOwner(this)
                .build();

        RecyclerView postsRecyclerView = view.findViewById(R.id.chatEntryRecyclerView);

        postsRecyclerView.setAdapter(new myChatsFragment.ChatEntryAdapter(options));

    }

    class ChatEntryAdapter extends FirestoreRecyclerAdapter<ChatEntry, myChatsFragment.ChatEntryAdapter.ChatEntryViewHolder> {
        public ChatEntryAdapter(@NonNull FirestoreRecyclerOptions<ChatEntry> options) {super(options);}

        @NonNull
        @Override
        public myChatsFragment.ChatEntryAdapter.ChatEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new myChatsFragment.ChatEntryAdapter.ChatEntryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_chatentry, parent, false));
        }

        @Override
        protected void onBindViewHolder(@NonNull myChatsFragment.ChatEntryAdapter.ChatEntryViewHolder holder, int position, @NonNull final ChatEntry msg) {

            SimpleDateFormat simpleDate;
            simpleDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            holder.timeTextView.setText( simpleDate.format( msg.timestamp ));
            holder.msgTextView.setText((msg.uid.equals(myUID) ? "Yo: " : msg.companion.nick+": ") + msg.text);
            holder.nickTextView.setText(msg.companion.nick);
            if(msg.companion.photoUrl.get(0) != null)
                Glide.with(getContext()).load(msg.companion.photoUrl.get(0))/*.circleCrop()*/.into(holder.companionPhotoImageView);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appViewModel.setViewingProfile(msg.companion);
                    navController.navigate(R.id.userChatFragment);
                }
            });
        }

        class ChatEntryViewHolder extends RecyclerView.ViewHolder {
            ImageView companionPhotoImageView;
            TextView nickTextView, msgTextView, timeTextView;

            ChatEntryViewHolder(@NonNull View itemView) {
                super(itemView);

                companionPhotoImageView = itemView.findViewById(R.id.photoImageView);
                nickTextView = itemView.findViewById(R.id.nickTextView);
                msgTextView = itemView.findViewById(R.id.messageTextView);
                timeTextView = itemView.findViewById(R.id.timeTextView);

            }
        }
    }
}