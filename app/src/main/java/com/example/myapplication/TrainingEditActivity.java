package com.example.myapplication;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.health.connect.datatypes.units.Length;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adapter.ExerciseRecyclerAdapter;
import com.example.myapplication.adapter.TrainingContentRecyclerAdapter;
import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.databinding.ActivityTrainingEditBinding;
import com.example.myapplication.exercise.Exercise;
import com.example.myapplication.training.Training;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class TrainingEditActivity extends AppCompatActivity {

    ActivityTrainingEditBinding binding;
    private EditText nameBox, timeBox, descBox;
    private ImageView imageView;
    private Button delButton, pickImage, saveButton;
    private ImageButton addExerciseButton, removeExerciseButton;
    private ImageButton rotateLeft, rotateRight;
    private DatabaseAdapter adapter;
    private long trainingId=0;
    private long userId;
    private RecyclerView recyclerView;
    private ArrayList<Exercise> arrayList;
    Bitmap bitmap;
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri uri) {
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                binding.image.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Toast.makeText(TrainingEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrainingEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        nameBox = binding.name;
        timeBox = binding.time;
        descBox = binding.desc;
        imageView = binding.image;
        delButton = binding.deleteButton;
        saveButton = binding.saveButton;
        rotateLeft = binding.rotateLeft;
        rotateRight = binding.rotateRight;
        recyclerView = binding.exerciseList;
        addExerciseButton = binding.addExercise;
        removeExerciseButton = binding.removeExercise;
        pickImage = binding.pickImage;

        adapter = new DatabaseAdapter(this);
        arrayList = new ArrayList<>();
        arrayList.add(null);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            trainingId = extras.getLong("id");
            userId = extras.getLong("user_id");
        }
        //if 0 --> add, else update
        if (trainingId > 0) {
            adapter.open();
            Training training = adapter.getTraining(trainingId);
            nameBox.setText(training.getName());
            timeBox.setText(String.valueOf(training.getTime()));
            descBox.setText(training.getDesc());
            imageView.setImageBitmap(training.getImage());
            adapter.close();
        } else {
            delButton.setVisibility(View.GONE);
        }
        pickImage.setOnClickListener(v -> mGetContent.launch("image/*"));

        addExerciseButton.setOnClickListener( v -> {
            arrayList.add(null);
            this.onResume();
        });

        removeExerciseButton.setOnClickListener( v -> {
            arrayList.remove(arrayList.size() - 1);
            this.onResume();
        });

        //Image rotate clicks
        rotateLeft.setOnClickListener( v -> {
            Bitmap bit = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            Matrix matrix = new Matrix();
            matrix.postRotate(-90);
            bitmap = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(), bit.getHeight(), matrix, true);
            imageView.setImageBitmap(bitmap);
        });

        rotateRight.setOnClickListener( v -> {
            Bitmap bit = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(), bit.getHeight(), matrix, true);
            imageView.setImageBitmap(bitmap);
        });

        saveButton.setOnClickListener( v -> {
            String name = nameBox.getText().toString();
            int time = Integer.parseInt(timeBox.getText().toString());
            String desc = descBox.getText().toString();
            System.out.println(userId);
            long author = userId;
            if(trainingId==0){
                bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            }
            if(bitmap==null) {
                adapter.open();
                bitmap = adapter.getTraining(trainingId).getImage();
                adapter.close();
            }
            Bitmap image = bitmap;
            Training training = new Training(trainingId, name, time, desc, image, author);

            adapter.open();
            if (trainingId > 0) {
                adapter.updateTraining(training);
            } else {
                adapter.insertTraining(training);
            }
            adapter.close();
            onBackPressed();
        });

        delButton.setOnClickListener( v -> {
            adapter.open();
            adapter.deleteTraining(trainingId);
            adapter.close();
            onBackPressed();
        });
    }

    private void goHome(){
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        //adapter.open();

        //ArrayList<Exercise> exercises = adapter.getExercises();

        //ArrayAdapter<Exercise> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exercises);
        recyclerView.setAdapter(new ExerciseRecyclerAdapter(arrayList));
        //adapter.close();
    }
}