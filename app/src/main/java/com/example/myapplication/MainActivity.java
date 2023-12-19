package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.CursorWindow;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.user.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ImageButton profileButton;
    private Button loginButton;
    static final String USERNAME="USERNAME";
    private User user;
    private DatabaseAdapter adapter;
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            TextView userText = binding.upToolbar.userText;
            if (result.getResultCode() == Activity.RESULT_OK){
                System.out.println("OK");
                Intent intent = result.getData();
                String username = intent.getStringExtra(USERNAME);
                adapter.open();
                user = adapter.getUserByUsername(username);
                adapter.close();
                userText.setText("Здравствуйте, " + user.getUsername());
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new DatabaseAdapter(this);
        Toolbar bar = binding.upToolbar.upToolbar;
        profileButton = binding.upToolbar.profileButton;
        loginButton = binding.upToolbar.login;
        BottomNavigationView navView = findViewById(R.id.bottom_nav);
        setSupportActionBar(bar);

        loginButton.setOnClickListener( v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            mStartForResult.launch(intent);
        });

        profileButton.setOnClickListener( v -> {
            Intent intent = new Intent(this, TestActivity.class);
            intent.putExtra("id", user.getId());
            startActivity(intent);
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(user!=null) {
            loginButton.setVisibility(View.INVISIBLE);
            profileButton.setVisibility(View.VISIBLE);
        }else {
            loginButton.setVisibility(View.VISIBLE);
            profileButton.setVisibility(View.INVISIBLE);
        }
    }
}