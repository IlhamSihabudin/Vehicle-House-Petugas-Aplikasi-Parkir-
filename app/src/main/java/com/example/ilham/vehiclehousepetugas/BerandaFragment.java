package com.example.ilham.vehiclehousepetugas;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilham.vehiclehousepetugas.API.PetugasAPI;
import com.example.ilham.vehiclehousepetugas.Model.Petugas;
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
public class BerandaFragment extends Fragment {
    Button btnMasukParkir, btnKeluarParkir;
    TextView txtNamaPetugas;
    private static final String URL = "http://192.168.1.124/vehicle_house/";
    ProgressDialog progress;
    List<Petugas> petugas = new ArrayList<>();

    public BerandaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);
        setHasOptionsMenu(true);

        txtNamaPetugas  = view.findViewById(R.id.txt_nama_petugas);
        btnMasukParkir  = view.findViewById(R.id.btnMasukParkir);
        btnKeluarParkir = view.findViewById(R.id.btnKeluarParkir);

        btnMasukParkir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), QrScanMasuk.class));
            }
        });

        btnKeluarParkir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), QrScanKeluar.class));
            }
        });

        progress = new ProgressDialog(getActivity());
        progress.setCancelable(false);
        progress.setMessage("Tunggu Sebentar...");
        progress.show();

        DatabaseHandler db = new DatabaseHandler(getActivity());
        String id_petugas = db.select();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PetugasAPI api = retrofit.create(PetugasAPI.class);
        Call<Value> call = api.select_where(id_petugas);

        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                progress.dismiss();
                petugas = response.body().getResult_petugas();
                Petugas petugases = petugas.get(0);

                txtNamaPetugas.setText(petugases.getNama_petugas());
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ((Dashboard) getActivity()).getSupportActionBar().setTitle("Beranda");
        super.onCreateOptionsMenu(menu, inflater);
    }
}
