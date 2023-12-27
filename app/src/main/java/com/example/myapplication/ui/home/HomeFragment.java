package com.example.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.TrainingActivity;
import com.example.myapplication.TrainingEditActivity;
import com.example.myapplication.adapter.TrainingContentRecyclerAdapter;
import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.training.Training;
import com.example.myapplication.user.User;
import com.example.myapplication.user.UserViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements TrainingContentRecyclerAdapter.OnClickListener{

    private RecyclerView recyclerView;
    private FragmentHomeBinding binding;
    private UserViewModel userViewModel;
    private Button buttonAddTraining;
    ArrayAdapter<Training> arrayAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = binding.trainingList;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        buttonAddTraining = binding.addTraining;
        buttonAddTraining.setOnClickListener( v -> {
            Intent intent = new Intent(this.getActivity(), TrainingEditActivity.class);
            intent.putExtra("user_id", userViewModel.getLoggedUser().getValue().getId());
            startActivity(intent);
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(userViewModel.getLoggedUser().getValue()!=null){
            buttonAddTraining.setVisibility(View.VISIBLE);
        } else {
            buttonAddTraining.setVisibility(View.INVISIBLE);
        }
        DatabaseAdapter adapter = new DatabaseAdapter(getActivity());
        adapter.open();

        ArrayList<Training> trainings = adapter.getTrainings();

        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, trainings);
        recyclerView.setAdapter(new TrainingContentRecyclerAdapter(trainings, this));
        adapter.close();
    }

    @Override
    public void onClick(int position) {
        Training training = arrayAdapter.getItem(position);
        if (training != null) {
            Intent intent = new Intent(this.getActivity(), TrainingActivity.class);
            intent.putExtra("id", training.getId());
            if(userViewModel.getLoggedUser().getValue()!=null) {
                intent.putExtra("user_id", userViewModel.getLoggedUser().getValue().getId());
            }
            startActivity(intent);
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}