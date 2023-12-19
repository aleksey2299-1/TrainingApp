package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.training.Training;

import java.util.ArrayList;

public class TrainingContentRecyclerAdapter extends RecyclerView.Adapter<TrainingContentRecyclerAdapter.ViewHolder> {
    private ArrayList<Training> mArrayList;
    private OnClickListener mOnClickListener;
    public TrainingContentRecyclerAdapter(ArrayList<Training> arrayList, OnClickListener onClickListener){
        this.mArrayList = arrayList;
        this.mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public TrainingContentRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.training_content, parent, false);
        ViewHolder viewHolder = new ViewHolder(contentView, mOnClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingContentRecyclerAdapter.ViewHolder holder, int position) {
        Training arrayList = mArrayList.get(position);
        TextView textViewName = holder.name;
        TextView textViewTime = holder.time;
        ImageView imageViewTraining = holder.image;
        textViewName.setText(arrayList.getName());
        textViewTime.setText(String.valueOf(arrayList.getTime()));
        System.out.println(arrayList.getImage());
        imageViewTraining.setImageBitmap(arrayList.getImage());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }
    // Создаем View для каждого объекта из массива
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name, time;
        public ImageView image;
        OnClickListener onClickListener;
        public ViewHolder(View itemView, OnClickListener onClickListener){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            image = (ImageView) itemView.findViewById(R.id.training_pic);
            this.onClickListener = onClickListener;
            itemView.setOnClickListener(this); // весь View кликабельный
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClick(getAdapterPosition());
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

    public interface OnClickListener{
        void onClick(int position);
    }
}

