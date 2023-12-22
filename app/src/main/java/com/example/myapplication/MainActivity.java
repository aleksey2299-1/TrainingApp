package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.CursorWindow;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.user.User;
import com.example.myapplication.user.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private ActivityMainBinding binding;
    DrawerLayout drawerLayout;
    private ImageButton profileButton;
    private Button loginButton;
    static final String USERNAME="USERNAME";

    public static User user;
    private DatabaseAdapter adapter;
    private UserViewModel userViewModel;
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

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //for large images
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

        drawerLayout = binding.drawerLayout;
        NavigationView navigationView = binding.leftNavView;
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, bar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.left_nav_view);

        loginButton.setOnClickListener( v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            mStartForResult.launch(intent);
        });

        profileButton.setOnClickListener( v -> {
            drawerLayout.openDrawer(GravityCompat.END);
//            Intent intent = new Intent(this, TestActivity.class);
//            intent.putExtra("id", user.getId());
//            startActivity(intent);
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(drawerLayout.isDrawerOpen(GravityCompat.END)){
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(user!=null) {
            adapter.open();
            user = adapter.getUserByUsername(user.getUsername());
            adapter.close();
            loginButton.setVisibility(View.INVISIBLE);
            profileButton.setVisibility(View.VISIBLE);
            userViewModel.loggedUser(user);
            TextView username = findViewById(R.id.user_username);
            TextView userInfo = findViewById(R.id.user_info);
            username.setText(user.getUsername());
            userInfo.setText(user.getFirstName() + " " + user.getLastName());
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }else {
            loginButton.setVisibility(View.VISIBLE);
            profileButton.setVisibility(View.INVISIBLE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.user_profile) {
            Intent intent = new Intent(this, TestActivity.class);
            intent.putExtra("id", user.getId());
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

}