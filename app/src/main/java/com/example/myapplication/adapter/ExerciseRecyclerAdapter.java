package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.exercise.Exercise;

import java.util.ArrayList;

public class ExerciseRecyclerAdapter extends RecyclerView.Adapter<ExerciseRecyclerAdapter.ViewHolder> {
    private ArrayList<Exercise> mArrayList;
    public ExerciseRecyclerAdapter(ArrayList<Exercise> arrayList){
        this.mArrayList = arrayList;
    }

    @NonNull
    @Override
    public ExerciseRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.exercise, parent, false);
        ExerciseRecyclerAdapter.ViewHolder viewHolder = new ExerciseRecyclerAdapter.ViewHolder(contentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseRecyclerAdapter.ViewHolder holder, int position) {
        Exercise arrayList = mArrayList.get(position);
        EditText textViewName = holder.name;
        EditText textViewDesc = holder.desc;
        EditText textViewReps = holder.reps;
        EditText textViewSets = holder.sets;
        EditText textViewTimePerSet = holder.timePerSet;
        EditText textViewRelaxTimeBetweenSets = holder.relaxTimeBetweenSets;
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }
    // Создаем View для каждого объекта из массива
    public class ViewHolder extends RecyclerView.ViewHolder {
        public EditText name, desc, reps, sets, timePerSet, relaxTimeBetweenSets;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (EditText) itemView.findViewById(R.id.exercise_name);
            desc = (EditText) itemView.findViewById(R.id.exercise_desc);
            reps = (EditText) itemView.findViewById(R.id.exercise_reps);
            sets = (EditText) itemView.findViewById(R.id.exercise_sets);
            timePerSet = (EditText) itemView.findViewById(R.id.exercise_time_per_set);
            relaxTimeBetweenSets = (EditText) itemView.findViewById(R.id.exercise_relax_time);
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

