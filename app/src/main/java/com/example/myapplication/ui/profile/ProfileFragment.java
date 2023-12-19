package com.example.myapplication.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.myapplication.R;
import com.example.myapplication.TestActivity;
import com.example.myapplication.adapter.ContentRecyclerAdapter;
import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.user.User;

import java.util.ArrayList;

public class ProfileFragment extends Fragment implements ContentRecyclerAdapter.OnClickListener{

    private RecyclerView contentList;
    private ProfileViewModel mViewModel;
    ArrayAdapter<User> arrayAdapter;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        contentList = view.findViewById(R.id.content_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(contentList.getContext(),
                linearLayoutManager.getOrientation());
        contentList.addItemDecoration(dividerItemDecoration);
        contentList.setLayoutManager(linearLayoutManager);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        DatabaseAdapter adapter = new DatabaseAdapter(getActivity());
        adapter.open();

        ArrayList<User> users = adapter.getUsers();

        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, users);
        contentList.setAdapter(new ContentRecyclerAdapter(users, this));
        adapter.close();
    }

    @Override
    public void onClick(int position) {
        User user = arrayAdapter.getItem(position);
        if (user != null) {
            Intent intent = new Intent(this.getActivity(), TestActivity.class);
            intent.putExtra("id", user.getId());
            startActivity(intent);
        }
    }
}