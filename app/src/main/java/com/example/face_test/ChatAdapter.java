package com.example.face_test;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.face_test.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Chat> chatList;
    private Context context;

    public ChatAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.bind(chat);
        holder.chatMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context.getApplicationContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.context_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_item1:
                                deleteChat(chat.getId());
                            case R.id.menu_item2:
                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                blockUser(userId, chat);
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
        holder.chatNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChatMessagesActivity(chat.getId());
            }
        });
    }

    private void deleteChat(String chatId) {
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference().child("chats").child(chatId);
        chatRef.removeValue();
    }

    private void blockUser(String userId, Chat chat) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        if(userId.equals(chat.getDoctorId())){
            userRef.child("blocked").child(chat.getPatientId()).setValue(true);
        }
        else{
            userRef.child("blocked").child(chat.getDoctorId()).setValue(true);
        }
    }

    private void openChatMessagesActivity(String chatId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("chatId", chatId);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        private TextView chatNameTextView;
        private Button chatMenu;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatNameTextView = itemView.findViewById(R.id.chatNameTextView);
            chatMenu = itemView.findViewById(R.id.chatMenu);
        }

        public void bind(Chat chat) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userRef;
            if(userId.equals(chat.getDoctorId())){
                userRef = FirebaseDatabase.getInstance().getReference().child("users").child(chat.getPatientId());
            }
            else{
                userRef = FirebaseDatabase.getInstance().getReference().child("users").child(chat.getDoctorId());
            }
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name_surname = dataSnapshot.child("name_surname").getValue(String.class);
                    chatNameTextView.setText(name_surname);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
