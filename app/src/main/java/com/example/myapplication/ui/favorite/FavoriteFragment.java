package com.example.myapplication.ui.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.TestActivity;
import com.example.myapplication.TrainingActivity;
import com.example.myapplication.adapter.ContentRecyclerAdapter;
import com.example.myapplication.adapter.TrainingContentRecyclerAdapter;
import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.training.Training;
import com.example.myapplication.user.User;
import com.example.myapplication.user.UserViewModel;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment implements TrainingContentRecyclerAdapter.OnClickListener{

    private RecyclerView favoriteList;
    private FavoriteViewModel mViewModel;
    ArrayAdapter<Training> arrayAdapter;
    private UserViewModel userViewModel;
    private long user_id;
    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        favoriteList = view.findViewById(R.id.favorite_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(favoriteList.getContext(),
                linearLayoutManager.getOrientation());
        favoriteList.addItemDecoration(dividerItemDecoration);
        favoriteList.setLayoutManager(linearLayoutManager);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        user_id = userViewModel.getLoggedUser().getValue().getId();
        DatabaseAdapter adapter = new DatabaseAdapter(getActivity());
        adapter.open();

        ArrayList<Training> favoriteTrainings = adapter.getFavoriteTrainingsByUserId(user_id);

        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, favoriteTrainings);
        favoriteList.setAdapter(new TrainingContentRecyclerAdapter(favoriteTrainings, this));
        adapter.close();
    }

    @Override
    public void onClick(int position) {
        Training training = arrayAdapter.getItem(position);
        if (training != null) {
            Intent intent = new Intent(this.getActivity(), TrainingActivity.class);
            intent.putExtra("id", training.getId());
            intent.putExtra("user_id", user_id);
            startActivity(intent);
        }
    }
}