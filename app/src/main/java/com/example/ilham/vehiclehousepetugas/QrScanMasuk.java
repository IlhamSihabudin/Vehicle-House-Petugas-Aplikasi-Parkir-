package com.example.ilham.vehiclehousepetugas;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilham.vehiclehousepetugas.API.PetugasAPI;
import com.example.ilham.vehiclehousepetugas.Model.Petugas;
import com.example.ilham.vehiclehousepetugas.Model.User;
import com.example.ilham.vehiclehousepetugas.Model.Value;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QrScanMasuk extends AppCompatActivity {
    private static final String URL = "http://192.168.1.124/vehicle_house/";
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    ProgressDialog progress;
    List<User> user = new ArrayList<>();
    final int requestCameraPermissionID = 1001;
    String id_user = "";

    @BindView(R.id.cameraPreview) SurfaceView cameraPreview;
    @BindView(R.id.txtResultMasuk) TextView txtResultMasuk;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case requestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan_masuk);
        ButterKnife.bind(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource    = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(400, 400).build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //Request Permission
                    ActivityCompat.requestPermissions(QrScanMasuk.this,
                            new String[]{Manifest.permission.CAMERA}, requestCameraPermissionID);
                    return;
                }
                try{
                    cameraSource.start(cameraPreview.getHolder());
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcode = detections.getDetectedItems();
                if (qrcode.size() != 0 && id_user.equals("")){
                    txtResultMasuk.post(new Runnable() {
                        @Override
                        public void run() {
                            id_user = qrcode.valueAt(0).displayValue;
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
                                    if (user.isEmpty()){
                                        Toast.makeText(QrScanMasuk.this, "User Tidak Terdaftar", Toast.LENGTH_SHORT).show();
                                        id_user = "";
                                    }else {
                                        insertParkir();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Value> call, Throwable t) {
                                    t.printStackTrace();
                                    Toast.makeText(QrScanMasuk.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(100);
                        }
                    });
                }
            }
        });
    }

    public void insertParkir(){
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
        Call<Value> call = api.masuk_parkir(id_user, id_petugas);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                progress.dismiss();
                String message = response.body().getMessage();
                String status  = response.body().getStatus();

                if (status.equals("1")){
                    Intent intent = new Intent(getApplicationContext(), DetailMasuk.class);
                    intent.putExtra("id_user", id_user);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(QrScanMasuk.this, message, Toast.LENGTH_SHORT).show();
                    id_user = "";
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progress.dismiss();
                t.printStackTrace();
                Toast.makeText(QrScanMasuk.this, "Jaringan Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
