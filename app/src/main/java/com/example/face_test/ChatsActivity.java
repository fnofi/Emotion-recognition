package com.example.face_test;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.face_test.Chat;
import com.example.face_test.ChatAdapter;
import com.example.face_test.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ChatsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private List<Chat> chatList;
    private String doctorId;
    private FirebaseAuth mAuth;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Действия");
        menu.add(0, v.getId(), 0, "Удалить чат");
        menu.add(0, v.getId(), 0, "Заблокировать пользователя");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Удалить чат") {
            // Обработка удаления чата
        } else if (item.getTitle() == "Заблокировать пользователя") {
            // Обработка блокировки пользователя
        } else {
            return false;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        mAuth = FirebaseAuth.getInstance();
        // Инициализация RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Инициализация списка чатов
        chatList = new ArrayList<>();

        // Инициализация адаптера для RecyclerView
        adapter = new ChatAdapter(this,chatList);
        recyclerView.setAdapter(adapter);

        // Инициализация кнопки для создания нового чата
        Button createChatButton = findViewById(R.id.createChatButton);
        createChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Отображение списка доступных врачей для начала нового чата
                showDoctorList();
            }
        });
        Button button_close = findViewById(R.id.button_close);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                intent.putExtra("selectedTab", R.id.navigation_notifications);
                startActivity(intent);
            }
        });

        // Пример загрузки чатов из базы данных
        loadChatsFromDatabase();
    }
    private void showDoctorList() {
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference userRoleRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("role");
        userRoleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userRole = dataSnapshot.getValue(String.class);
                    if (Objects.equals(userRole, "patient") || Objects.equals(userRole, "admin")) {
                        DatabaseReference doctorsRef = FirebaseDatabase.getInstance().getReference().child("doctors");
                        doctorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<String> doctorNames = new ArrayList<>();
                                for (DataSnapshot doctorSnapshot : dataSnapshot.getChildren()) {
                                    String name_surname = doctorSnapshot.child("name_surname").getValue(String.class);
                                    doctorNames.add(name_surname);
                                }
                                String[] doctorsArray = doctorNames.toArray(new String[0]);

                                AlertDialog.Builder builder = new AlertDialog.Builder(ChatsActivity.this);
                                builder.setTitle("Выберите пользователя");
                                builder.setItems(doctorsArray, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String selectedDoctor = doctorsArray[which];
                                        // Здесь можно вызвать метод для создания нового чата с выбранным врачом
                                        createNewChatWithDoctor(selectedDoctor);
                                    }
                                });
                                builder.show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Обработка ошибок при загрузке списка врачей
                            }
                        });
                    } else {
                        DatabaseReference doctorsRef = FirebaseDatabase.getInstance().getReference().child("doctors");
                        doctorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<String> doctorNames = new ArrayList<>();
                                for (DataSnapshot doctorSnapshot : dataSnapshot.getChildren()) {
                                    String name_surname = doctorSnapshot.child("name_surname").getValue(String.class);
                                    doctorNames.add(name_surname);
                                }
                                String[] doctorsArray = doctorNames.toArray(new String[0]);
                                DatabaseReference sharedPatientsRef = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(userId).child("sharedPatients");
                                sharedPatientsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            List<String> patientUids = new ArrayList<>();
                                            for (DataSnapshot patientSnapshot : dataSnapshot.getChildren()) {
                                                String patientUid = patientSnapshot.getKey();
                                                patientUids.add(patientUid);
                                            }
                                            List<String> patientNames = new ArrayList<>();
                                            for (String patientUid : patientUids) {
                                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                                                        .child("users").child(patientUid);
                                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            String name = dataSnapshot.child("name_surname").getValue(String.class);
                                                            String fullName = name;
                                                            patientNames.add(fullName);
                                                            if (patientNames.size() == patientUids.size()) {
                                                                String[] patientNamesArray = patientNames.toArray(new String[0]);
                                                                String[] combinedArray = Arrays.copyOf(doctorsArray, doctorsArray.length+patientNamesArray.length);
                                                                System.arraycopy(patientNamesArray, 0, combinedArray, doctorsArray.length, patientNamesArray.length);
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(ChatsActivity.this);
                                                                builder.setTitle("Выберите пользователя");
                                                                builder.setItems(combinedArray, new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        String selectedPatient = combinedArray[which];
                                                                        createNewChatWithPatient(selectedPatient);
                                                                    }
                                                                });
                                                                builder.show();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        Log.e("Firebase", "Ошибка при получении данных пользователя: " + databaseError.getMessage());
                                                    }
                                                });
                                            }
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), "Список пациентов пуст",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.e("Firebase", "Не удалось получить данные", databaseError.toException());
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Обработка ошибок при загрузке списка врачей
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadChatsFromDatabase() {
        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference().child("chats");

        chatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Очищаем список чатов перед загрузкой новых данных
                chatList.clear();

                // Получаем идентификатор текущего пользователя
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser!=null) {
                    String userId = currentUser.getUid();
                    // Перебираем все дочерние узлы в "chats"
                    for (DataSnapshot chatSnapshot : dataSnapshot.getChildren()) {
                        // Получаем данные чата из снимка
                        String chatId = chatSnapshot.getKey();
                        String patientId = chatSnapshot.child("patientId").getValue(String.class);
                        String doctorId = chatSnapshot.child("doctorId").getValue(String.class);

                        // Проверяем, совпадает ли patientId с идентификатором текущего пользователя
                        if (patientId.equals(userId) || doctorId.equals(userId)) {
                            // Создаем новый объект Chat с полученными данными и добавляем его в список
                            Chat chat = new Chat(chatId, patientId, doctorId);
                            chatList.add(chat);
                        }
                    }

                    // Уведомляем адаптер об изменениях в данных
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок при загрузке чатов
                Log.e("Firebase", "Ошибка при загрузке чатов из базы данных: " + databaseError.getMessage());
            }
        });
    }




    private void createNewChatWithDoctor(String doctorName_Surname) {
        Query query = FirebaseDatabase.getInstance().getReference().child("doctors").orderByChild("name_surname").equalTo(doctorName_Surname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot doctorSnapshot = dataSnapshot.getChildren().iterator().next();
                    doctorId = doctorSnapshot.getKey();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser != null) {
                        String patientId = currentUser.getUid();
                        // Создание нового уникального идентификатора для чата
                        String chatId = generateUniqueChatId(); // Здесь нужно реализовать метод создания нового уникального идентификатора для чата

                        // Добавление чата в базу данных
                        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference().child("chats").child(chatId);
                        Chat newChat = new Chat(chatId, patientId, doctorId);

                        chatsRef.setValue(newChat);

                        // Открытие чата для общения
                        openChatActivity(chatId); // Здесь нужно реализовать метод открытия активности чата с переданным идентификатором чата
                    }
                    } else {

                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибки при выполнении запроса к базе данных
                Log.e("Firebase", "Ошибка при выполнении запроса к базе данных: " + databaseError.getMessage());
            }
        });
    }

    private void createNewChatWithPatient(String patientName_Surname) {
        Query query = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("name_surname").equalTo(patientName_Surname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataSnapshot doctorSnapshot = dataSnapshot.getChildren().iterator().next();
                    doctorId = doctorSnapshot.getKey();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if (currentUser != null) {
                        String patientId = currentUser.getUid();
                        // Создание нового уникального идентификатора для чата
                        String chatId = generateUniqueChatId(); // Здесь нужно реализовать метод создания нового уникального идентификатора для чата

                        // Добавление чата в базу данных
                        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference().child("chats").child(chatId);
                        Chat newChat = new Chat(chatId, patientId, doctorId);

                        chatsRef.setValue(newChat);

                        // Открытие чата для общения
                        openChatActivity(chatId); // Здесь нужно реализовать метод открытия активности чата с переданным идентификатором чата
                    }
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибки при выполнении запроса к базе данных
                Log.e("Firebase", "Ошибка при выполнении запроса к базе данных: " + databaseError.getMessage());
            }
        });
    }
    private String generateUniqueChatId() {
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 1000);
        return String.valueOf(timestamp) + "_" + String.valueOf(random);
    }
    private void openChatActivity(String chatId) {
        // Создаем Intent для открытия активности чата
        Intent chatIntent = new Intent(this, ChatActivity.class);

        // Передаем идентификатор чата в Intent
        chatIntent.putExtra("chatId", chatId);

        // Здесь вы можете передать другие необходимые данные в Intent, например, идентификатор текущего пользователя и т. д.

        // Запускаем активность чата
        startActivity(chatIntent);
    }


}
