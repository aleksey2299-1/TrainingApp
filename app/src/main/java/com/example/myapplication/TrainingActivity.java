package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.training.Training;
import com.example.myapplication.user.User;

public class TrainingActivity extends AppCompatActivity {

    private EditText nameBox;
    private EditText timeBox;
    private EditText descBox;
    private EditText authorBox;
    private ImageView image;
    private Button delButton;
    private Button pickImage;
    private DatabaseAdapter adapter;
    private long trainingId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        nameBox = findViewById(R.id.name);
        timeBox = findViewById(R.id.time);
        descBox = findViewById(R.id.desc);
        authorBox = findViewById(R.id.author);
        image = findViewById(R.id.image);
        delButton = findViewById(R.id.deleteButton);
        adapter = new DatabaseAdapter(this);
        pickImage = findViewById(R.id.pickImage);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            trainingId = extras.getLong("id");
        }
        // если 0, то добавление
        if (trainingId > 0) {
            // получаем элемент по id из бд
            adapter.open();
            Training training = adapter.getTraining(trainingId);
            nameBox.setText(training.getName());
            timeBox.setText(String.valueOf(training.getTime()));
            descBox.setText(training.getDesc());
            authorBox.setText(String.valueOf(training.getAuthor()));
            //image.setImageURI(user.getPassword());
            adapter.close();
        } else {
            // скрываем кнопку удаления
            delButton.setVisibility(View.GONE);
        }
        pickImage.setOnClickListener( v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivity(intent);
        });
    }

    public void save(View view){

        String name = nameBox.getText().toString();
        int time = Integer.parseInt(timeBox.getText().toString());
        String desc = descBox.getText().toString();
        long author = Long.parseLong(authorBox.getText().toString());
        //String image = image.getBytes().toString();
        Training training = new Training(trainingId, name, time, desc, "Empty", author);

        adapter.open();
        if (trainingId > 0) {
            adapter.updateTraining(training);
        } else {
            adapter.insertTraining(training);
        }
        adapter.close();
        onBackPressed();
    }
    public void delete(View view){
        adapter.open();
        adapter.deleteTraining(trainingId);
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