package com.example.ilham.vehiclehousepetugas.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ilham.vehiclehousepetugas.Model.User;
import com.example.ilham.vehiclehousepetugas.ParkirFragment;
import com.example.ilham.vehiclehousepetugas.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private Context context;
    private List<User> user;

    public RecyclerViewAdapter(Context context, List<User> user) {
        this.context = context;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_parkir, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User users = user.get(position);
        holder.txtNama.setText(users.getNama());
        holder.txtMerkKendaraan.setText(users.getMerk_kendaraan());
        holder.txtPlatNomor.setText(users.getPlat_nomor());

        String jenisKendaraan = users.getJenis_kendaraan();
        if (jenisKendaraan.equals("Mobil")){
            Glide.with(context)
                    .load(R.drawable.ic_car_side)
                    .into(holder.imgJenisKendaraan);
        }else{
            Glide.with(context)
                    .load(R.drawable.ic_bike)
                    .into(holder.imgJenisKendaraan);
        }
    }

    @Override
    public int getItemCount() {
        return user.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txtNama) TextView txtNama;
        @BindView(R.id.txtMerkKendaraan) TextView txtMerkKendaraan;
        @BindView(R.id.txtPlatNomor) TextView txtPlatNomor;
        @BindView(R.id.imgJenisKendaraan) ImageView imgJenisKendaraan;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
