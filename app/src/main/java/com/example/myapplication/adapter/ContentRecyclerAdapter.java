package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.user.User;

import java.util.ArrayList;

public class ContentRecyclerAdapter extends RecyclerView.Adapter<ContentRecyclerAdapter.ViewHolder> {
    private ArrayList<User> mArrayList;
    private OnClickListener mOnClickListener;
    public ContentRecyclerAdapter(ArrayList<User> arrayList, OnClickListener onClickListener){
        this.mArrayList = arrayList;
        this.mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ContentRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.content, parent, false);
        ViewHolder viewHolder = new ViewHolder(contentView, mOnClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContentRecyclerAdapter.ViewHolder holder, int position) {
        User arrayList = mArrayList.get(position);
        TextView textViewName = holder.name;
        TextView textViewLastName = holder.last_name;
        textViewName.setText(arrayList.getUsername());
        textViewLastName.setText(arrayList.getLastName());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }
    // Создаем View для каждого объекта из массива
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name, last_name;
        OnClickListener onClickListener;
        public ViewHolder(View itemView, OnClickListener onClickListener){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            last_name = (TextView) itemView.findViewById(R.id.last_name);
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
