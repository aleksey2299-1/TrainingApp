package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DatabaseAdapter adapter = new DatabaseAdapter(this);

        binding.loginButton.setOnClickListener(view -> {
            String username = binding.username.getText().toString();
            String password = binding.password.getText().toString();
            if(username.equals("")||password.equals(""))
                Toast.makeText(LoginActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            else{
                Boolean checkCredentials = adapter.checkUsernamePassword(username, password);
                if(checkCredentials == true){
                    Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent  = new Intent();
                    System.out.println(username);
                    intent.putExtra(MainActivity.USERNAME, username);
                    setResult(RESULT_OK, intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.signupRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

    }

}