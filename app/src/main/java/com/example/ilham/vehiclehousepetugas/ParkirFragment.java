package com.example.ilham.vehiclehousepetugas;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilham.vehiclehousepetugas.API.PetugasAPI;
import com.example.ilham.vehiclehousepetugas.Adapter.RecyclerViewAdapter;
import com.example.ilham.vehiclehousepetugas.Model.Petugas;
import com.example.ilham.vehiclehousepetugas.Model.User;
import com.example.ilham.vehiclehousepetugas.Model.Value;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class ParkirFragment extends Fragment {

    public static final String URL = "http://192.168.1.124/vehicle_house/";
    List<User> user = new ArrayList<>();
    ProgressBar progressBar;
    RecyclerView recyclerView;
    TextView parkirNotFound;
    RecyclerViewAdapter recyclerViewAdapter;

    public ParkirFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parkir, container, false);


        setHasOptionsMenu(true);

        progressBar     = view.findViewById(R.id.progress_bar);
        recyclerView    = view.findViewById(R.id.recycler_view_parkir);
        parkirNotFound  = view.findViewById(R.id.parkir_not_found);

        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), user);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewAdapter);

        viewInParkir();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ((Dashboard) getActivity()).getSupportActionBar().setTitle("Kendaraan di Tempat Parkir");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewInParkir();
    }

    public void viewInParkir(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PetugasAPI api = retrofit.create(PetugasAPI.class);
        Call<Value> call = api.view_in_parkir();

        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String status = response.body().getStatus();

                progressBar.setVisibility(View.GONE);
                if (status.equals("1")){
                    user = response.body().getResult_user();
                    if (user.isEmpty()){
                        parkirNotFound.setVisibility(View.VISIBLE);
                    }else{
                        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), user);
                        recyclerView.setAdapter(recyclerViewAdapter);
                    }
                }else{
                    parkirNotFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                parkirNotFound.setText("Jaringan Error");
                parkirNotFound.setVisibility(View.VISIBLE);
                t.printStackTrace();
            }
        });
    }
}