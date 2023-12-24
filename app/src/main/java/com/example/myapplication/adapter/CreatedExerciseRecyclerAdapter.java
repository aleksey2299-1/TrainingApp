package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.TrainingEditActivity;
import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.exercise.Exercise;

import java.util.ArrayList;

public class CreatedExerciseRecyclerAdapter extends RecyclerView.Adapter<CreatedExerciseRecyclerAdapter.ViewHolder> {
    private ArrayList<Exercise> mArrayList;
    private Context context;
    public CreatedExerciseRecyclerAdapter(ArrayList<Exercise> arrayList, Context context){
        this.mArrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CreatedExerciseRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.created_exercise, parent, false);
        CreatedExerciseRecyclerAdapter.ViewHolder viewHolder = new CreatedExerciseRecyclerAdapter.ViewHolder(contentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CreatedExerciseRecyclerAdapter.ViewHolder holder, int position) {
        Exercise arrayList = mArrayList.get(position);
        TextView textViewName = holder.name;
        TextView textViewDesc = holder.desc;
        TextView textViewReps = holder.reps;
        TextView textViewSets = holder.sets;
        TextView textViewTimePerSet = holder.timePerSet;
        TextView textViewRelaxTimeBetweenSets = holder.relaxTimeBetweenSets;
        Button deleteExerciseButton = holder.deleteExerciseButton;
        //TextView textViewTrainingId = holder.trainingId;
        textViewName.setText(arrayList.getName());
        textViewDesc.setText(arrayList.getDesc());
        textViewReps.setText(String.valueOf(arrayList.getReps()));
        textViewSets.setText(String.valueOf(arrayList.getSets()));
        textViewTimePerSet.setText(String.valueOf(arrayList.getTimePerSet()));
        textViewRelaxTimeBetweenSets.setText(String.valueOf(arrayList.getRelaxTimeBetweenSets()));
        //textViewTrainingId.setText(String.valueOf(arrayList.getTrainingId()));

        deleteExerciseButton.setOnClickListener( v -> {
            DatabaseAdapter adapter = new DatabaseAdapter(deleteExerciseButton.getContext());
            adapter.open();
            adapter.deleteExercise(arrayList.getId());
            adapter.close();
            ((TrainingEditActivity) context).onResume();
        });

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }
    // Создаем View для каждого объекта из массива
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, desc, reps, sets, timePerSet, relaxTimeBetweenSets, trainingId;
        public Button deleteExerciseButton;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.exercise_name);
            desc = itemView.findViewById(R.id.exercise_desc);
            reps = itemView.findViewById(R.id.exercise_reps);
            sets = itemView.findViewById(R.id.exercise_sets);
            timePerSet = itemView.findViewById(R.id.exercise_time_per_set);
            relaxTimeBetweenSets = itemView.findViewById(R.id.exercise_relax_time);
            //trainingId = itemView.findViewById(R.id.exercise_training_id);
            deleteExerciseButton = itemView.findViewById(R.id.delete_exercise_button);
        }
    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
        }
    }
}

