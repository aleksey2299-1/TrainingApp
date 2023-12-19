package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.databinding.ActivitySignupBinding;
import com.example.myapplication.user.User;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DatabaseAdapter adapter = new DatabaseAdapter(this);

        binding.signupButton.setOnClickListener(view -> {
            String username = binding.username.getText().toString();
            String email = binding.email.getText().toString();
            String firstName = binding.firstName.getText().toString();
            String lastName = binding.lastName.getText().toString();
            String password = binding.password.getText().toString();
            String confirmPassword = binding.passwordConfirm.getText().toString();
            if(username.equals("")||email.equals("")||firstName.equals("")||lastName.equals("")||password.equals("")||confirmPassword.equals(""))
                Toast.makeText(SignupActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            else{
                if(password.equals(confirmPassword)){
                    System.out.println("Пароли прошли");
                    Boolean checkUsername = adapter.checkUsername(username);
                    Boolean checkUserEmail = adapter.checkEmail(email);
                    if(checkUserEmail == false&checkUsername == false){
                        System.out.println("Валидация прошла");
                        User user = new User(0, username, email, firstName, lastName, password);
                        adapter.open();
                        adapter.insertUser(user);
                        adapter.close();
                        Toast.makeText(SignupActivity.this, "Signup Successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(SignupActivity.this, "User already exists! Please login", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignupActivity.this, "Invalid Password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.loginRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}