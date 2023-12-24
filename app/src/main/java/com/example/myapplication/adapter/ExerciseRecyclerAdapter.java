package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
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
        public String nameEdited, descEdited, repsEdited, setsEdited, timePerSetEdited, relaxTimeEdited;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (EditText) itemView.findViewById(R.id.exercise_name);
            name.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {}

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    nameEdited = s.toString();
                }
            });
            desc = (EditText) itemView.findViewById(R.id.exercise_desc);
            desc.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {}

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    descEdited = s.toString();
                }
            });
            reps = (EditText) itemView.findViewById(R.id.exercise_reps);
            reps.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {}

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    repsEdited = s.toString();
                }
            });
            sets = (EditText) itemView.findViewById(R.id.exercise_sets);
            sets.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {}

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    setsEdited = s.toString();
                }
            });
            timePerSet = (EditText) itemView.findViewById(R.id.exercise_time_per_set);
            timePerSet.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {}

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    timePerSetEdited = s.toString();
                }
            });
            relaxTimeBetweenSets = (EditText) itemView.findViewById(R.id.exercise_relax_time);
            relaxTimeBetweenSets.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {}

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    relaxTimeEdited = s.toString();
                }
            });
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

