package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.user.User;

public class TestActivity extends AppCompatActivity {

    private EditText usernameBox;
    private EditText emailBox;
    private EditText firstNameBox;
    private EditText lastNameBox;
    private EditText passwordBox;
    private Button delButton;

    private DatabaseAdapter adapter;
    private long userId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        usernameBox = findViewById(R.id.username);
        emailBox = findViewById(R.id.email);
        firstNameBox = findViewById(R.id.firstName);
        lastNameBox = findViewById(R.id.lastName);
        passwordBox = findViewById(R.id.password);
        delButton = findViewById(R.id.deleteButton);
        adapter = new DatabaseAdapter(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }
        // если 0, то добавление
        if (userId > 0) {
            // получаем элемент по id из бд
            adapter.open();
            User user = adapter.getUser(userId);
            usernameBox.setText(user.getUsername());
            emailBox.setText(user.getEmail());
            firstNameBox.setText(user.getFirstName());
            lastNameBox.setText(user.getLastName());
            passwordBox.setText(user.getPassword());
            adapter.close();
        } else {
            // скрываем кнопку удаления
            delButton.setVisibility(View.GONE);
        }
    }

    public void save(View view){

        String username = usernameBox.getText().toString();
        String email = emailBox.getText().toString();
        String firstName = firstNameBox.getText().toString();
        String lastName = lastNameBox.getText().toString();
        String password = passwordBox.getText().toString();
        User user = new User(userId, username, email, firstName, lastName, password, null);

        adapter.open();
        if (userId > 0) {
            adapter.updateUser(user);
        } else {
            adapter.insertUser(user);
        }
        adapter.close();
        onBackPressed();
    }
    public void delete(View view){

        adapter.open();
        adapter.deleteUser(userId);
        adapter.close();
        onBackPressed();
    }
    private void goHome(){
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}