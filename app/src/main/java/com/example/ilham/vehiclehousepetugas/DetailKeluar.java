package com.example.ilham.vehiclehousepetugas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ilham.vehiclehousepetugas.API.PetugasAPI;
import com.example.ilham.vehiclehousepetugas.Model.User;
import com.example.ilham.vehiclehousepetugas.Model.Value;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailKeluar extends AppCompatActivity {
    private static final String URL = "http://192.168.1.124/vehicle_house/";
    List<User> user = new ArrayList<>();

    @BindView(R.id.imgJenisKendaraan) ImageView imgJenisKendaraan;
    @BindView(R.id.txtNamaUser) TextView txtNamaUser;
    @BindView(R.id.txtBayar) TextView txtBayar;
    @BindView(R.id.txtMerkKendaraan) TextView txtMerkKendaraan;
    @BindView(R.id.txtPlatNomor) TextView txtPlatNomor;

    @OnClick(R.id.btnSelesai) void selesai(){
        startActivity(new Intent(DetailKeluar.this, QrScanKeluar.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_keluar);
        ButterKnife.bind(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        final Intent intent = getIntent();
        String id_user = intent.getStringExtra("id_user");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PetugasAPI api = retrofit.create(PetugasAPI.class);
        Call<Value> call = api.select_where_user(id_user);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                user = response.body().getResult_user();
                User data_user = user.get(0);

                txtNamaUser.setText(data_user.getNama());
                txtPlatNomor.setText(data_user.getPlat_nomor());
                if (data_user.getJenis_kendaraan().equals("Motor")){
                    Glide.with(getApplicationContext())
                            .load(R.drawable.ic_bike)
                            .into(imgJenisKendaraan);
                }else{
                    Glide.with(getApplicationContext())
                            .load(R.drawable.ic_car_side)
                            .into(imgJenisKendaraan);
                }

                txtMerkKendaraan.setText(data_user.getMerk_kendaraan());
                txtBayar.setText("Rp."+intent.getStringExtra("bayar"));
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
