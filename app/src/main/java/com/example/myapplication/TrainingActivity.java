package com.example.myapplication;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.databinding.ActivityTrainingBinding;
import com.example.myapplication.training.Training;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class TrainingActivity extends AppCompatActivity {

    ActivityTrainingBinding binding;
    private TextView authorText, nameText, timeText, descText;
    private Button editButton, startButton;
    private ImageView imageView;
    private DatabaseAdapter adapter;
    private long trainingId=0;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrainingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        nameText = binding.name;
        timeText = binding.time;
        descText = binding.desc;
        authorText = binding.author;
        imageView = binding.image;
        editButton = binding.editButton;
        startButton = binding.startButton;

        adapter = new DatabaseAdapter(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            trainingId = extras.getLong("id");
            if (extras.containsKey("user_id")) {
                userId = extras.getLong("user_id");
                editButton.setVisibility(View.VISIBLE);
            } else {
                editButton.setVisibility(View.INVISIBLE);
            }
        }
        startButton.setOnClickListener( v -> {
            Intent intent = new Intent(this, TrainingStartActivity.class);
            intent.putExtra("id", trainingId);
            startActivity(intent);
        });

        editButton.setOnClickListener( v -> {
            Intent intent = new Intent(this, TrainingEditActivity.class);
            intent.putExtra("id", trainingId);
            intent.putExtra("user_id", userId);
            startActivity(intent);
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        adapter.open();
        Training training = adapter.getTraining(trainingId);
        nameText.setText(training.getName());
        timeText.setText(String.valueOf(training.getTime()));
        descText.setText(training.getDesc());
        String author = adapter.getUser(training.getAuthor()).getUsername();
        authorText.setText(author);
        imageView.setImageBitmap(training.getImage());
        adapter.close();
    }
    private void goHome(){
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}