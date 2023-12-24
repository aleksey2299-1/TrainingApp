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
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adapter.CreatedExerciseRecyclerAdapter;
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
    private ImageButton addExerciseButton, removeExerciseButton, rotateLeft, rotateRight;
    private DatabaseAdapter adapter;
    private long trainingId = 0;
    private long userId;
    private RecyclerView recyclerView, createdExerciseRecyclerView;
    private ArrayList<Exercise> arrayList, createdExerciseArrayList;
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
        createdExerciseRecyclerView = binding.createdExerciseList;
        addExerciseButton = binding.addExercise;
        removeExerciseButton = binding.removeExercise;
        pickImage = binding.pickImage;

        adapter = new DatabaseAdapter(this);
        arrayList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManagerForCreatedExercises = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(createdExerciseRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        createdExerciseRecyclerView.addItemDecoration(dividerItemDecoration1);
        createdExerciseRecyclerView.setLayoutManager(linearLayoutManagerForCreatedExercises);

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
            arrayList.add(null);
        }
        pickImage.setOnClickListener(v -> mGetContent.launch("image/*"));

        addExerciseButton.setOnClickListener( v -> {
            arrayList.add(null);
            this.onResume();
        });

        removeExerciseButton.setOnClickListener( v -> {
            int lastIndex = arrayList.size() - 1;
            arrayList.remove(lastIndex);
            this.onResume();
        });

        //Image rotate click listeners
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
                trainingId = adapter.insertTraining(training);
            }
            //add exercises
            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                View view = recyclerView.getLayoutManager().findViewByPosition(i);
                ExerciseRecyclerAdapter.ViewHolder viewHolder = (ExerciseRecyclerAdapter.ViewHolder) recyclerView.getChildViewHolder(view);
                String nameEdited = viewHolder.nameEdited;
                String descEdited = viewHolder.descEdited;
                int repsEdited = Integer.parseInt(viewHolder.repsEdited);
                int setsEdited = Integer.parseInt(viewHolder.setsEdited);
                int timePerSetEdited = Integer.parseInt(viewHolder.timePerSetEdited);
                int relaxTimeEdited = Integer.parseInt(viewHolder.relaxTimeEdited);
                Exercise exercise = new Exercise(0, nameEdited, descEdited, repsEdited, setsEdited, timePerSetEdited, relaxTimeEdited, trainingId);
                adapter.insertExercise(exercise);
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
        adapter.open();
        // add views for created exercises
        if (trainingId!=0) {
            createdExerciseArrayList = adapter.getExercisesByTrainingId(trainingId);
            createdExerciseRecyclerView.setAdapter(new CreatedExerciseRecyclerAdapter(createdExerciseArrayList, this));
        }
        // add views for creating exercise
        recyclerView.setAdapter(new ExerciseRecyclerAdapter(arrayList));
        adapter.close();
    }
}