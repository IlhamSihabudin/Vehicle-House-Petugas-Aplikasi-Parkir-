package com.example.ilham.vehiclehousepetugas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ilham.vehiclehousepetugas.API.PetugasAPI;
import com.example.ilham.vehiclehousepetugas.Model.Petugas;
import com.example.ilham.vehiclehousepetugas.Model.Value;
import com.santalu.maskedittext.MaskEditText;

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

public class EditProfil extends AppCompatActivity {
    private static final String URL = "http://192.168.1.124/vehicle_house/";
    ProgressDialog progress;
    List<Petugas> petugas = new ArrayList<>();

    @BindView(R.id.edtNama)
    EditText edtNama;
    @BindView(R.id.edtNoTlp)
    MaskEditText edtNoTlp;
    @BindView(R.id.edtUsername)
    EditText edtUsername;
    @BindView(R.id.edtPassword)
    EditText edtPassword;

    @OnClick(R.id.btnSimpanPerubahan) void simpanPerubahan(){
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Tunggu Sebentar...");
        progress.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        String id_petugas = db.select();

        PetugasAPI api = retrofit.create(PetugasAPI.class);
        Call<Value> call = api.update(id_petugas, edtNama.getText().toString(), edtNoTlp.getText().toString(), edtUsername.getText().toString(), edtPassword.getText().toString());
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                progress.dismiss();
                String message  = response.body().getMessage();
                String status   = response.body().getStatus();
                progress.dismiss();
                if (status.equals("1")){
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progress.dismiss();
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        ButterKnife.bind(this);

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Tunggu Sebentar...");
        progress.show();

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
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
                petugas = response.body().getResult_petugas();
                Petugas data_petugas = petugas.get(0);

                edtNama.setText(data_petugas.getNama_petugas());
                edtNoTlp.setText(data_petugas.getNo_tlp());
                edtUsername.setText(data_petugas.getUsername());
                edtPassword.setText(data_petugas.getPassword());
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progress.dismiss();
                t.printStackTrace();
            }
        });
    }
}
