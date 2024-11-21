package com.example.face_test;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private Button buttonSendMessage;
    private Button buttonBlockUser;
    private TextView blockTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference messagesRef;
    private String chatId;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onResume() {
        super.onResume();
        checkBlockedUser();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        chatId = getIntent().getStringExtra("chatId");
        messagesRef = FirebaseDatabase.getInstance().getReference().child("chats").child(chatId).child("messages");

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);
        buttonBlockUser = findViewById(R.id.buttonBlockUser);
        blockTextView = findViewById(R.id.blockTextView);
        buttonBlockUser.setVisibility(View.GONE);
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(messageAdapter);

        checkBlockedUser();
        loadMessages();
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        buttonBlockUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unBlockUser();
            }
        });
        Button button_close = findViewById(R.id.button_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void checkBlockedUser() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference().child("chats").child(chatId);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentUserRole = currentUserId.equals(dataSnapshot.child("doctorId").getValue(String.class)) ? "patientId" : "doctorId";
                String otherUserId = dataSnapshot.child(currentUserRole).getValue(String.class);
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Boolean isBlock = dataSnapshot.child(currentUserId).child("blocked").child(otherUserId).getValue(Boolean.class);
                        if (isBlock != null && isBlock) {
                            buttonSendMessage.setVisibility(View.GONE);
                            editTextMessage.setVisibility(View.GONE);
                            buttonBlockUser.setVisibility(View.VISIBLE);
                            blockTextView.setVisibility(View.GONE);
                        }
                        else {
                            DatabaseReference otherUserRef = FirebaseDatabase.getInstance().getReference()
                                    .child("users");
                            otherUserRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Boolean isBlocked = dataSnapshot.child(otherUserId).child("blocked").child(currentUserId).getValue(Boolean.class);
                                    if (isBlocked != null && isBlocked) {
                                        buttonSendMessage.setVisibility(View.GONE);
                                        editTextMessage.setVisibility(View.GONE);
                                        buttonBlockUser.setVisibility(View.GONE);
                                        blockTextView.setVisibility(View.VISIBLE);
                                    } else {
                                        buttonSendMessage.setVisibility(View.VISIBLE);
                                        editTextMessage.setVisibility(View.VISIBLE);
                                        buttonBlockUser.setVisibility(View.GONE);
                                        blockTextView.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void unBlockUser() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference().child("chats").child(chatId);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentUserRole = currentUserId.equals(dataSnapshot.child("doctorId").getValue(String.class)) ? "patientId" : "doctorId";
                String otherUserId = dataSnapshot.child(currentUserRole).getValue(String.class);
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
                userRef.child(currentUserId).child("blocked").child(otherUserId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        buttonBlockUser.setVisibility(View.GONE);
                        blockTextView.setVisibility(View.GONE);
                        Toast.makeText(ChatActivity.this, "Пользователь разблокирован", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatActivity.this, "Не удалось разблокировать пользователя", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void loadMessages() {
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                if (message != null) {
                    messageList.add(message);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
                // Не используется в данном примере
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Не используется в данном примере
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {
                // Не используется в данном примере
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Обработка ошибок
            }
        });
    }

    private void sendMessage() {
        String messageText = editTextMessage.getText().toString().trim();
        if (!messageText.isEmpty()) {
            long timestamp = System.currentTimeMillis();
            String senderId = mAuth.getCurrentUser().getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(senderId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name_surname = dataSnapshot.child("name_surname").getValue(String.class);
                    String senderName = name_surname;
                    Message message = new Message(senderId, messageText, timestamp, senderName);
                    messagesRef.push().setValue(message);
                    editTextMessage.setText("");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {
            Toast.makeText(this, "Введите сообщение", Toast.LENGTH_SHORT).show();
        }
    }
}
