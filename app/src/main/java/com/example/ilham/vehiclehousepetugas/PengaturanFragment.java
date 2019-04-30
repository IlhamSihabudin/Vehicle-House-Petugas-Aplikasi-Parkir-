package com.example.ilham.vehiclehousepetugas;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class PengaturanFragment extends Fragment {

    Button btnEdit, btnKeluar;

    public PengaturanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pengaturan, container, false);
        setHasOptionsMenu(true);

        btnEdit     = view.findViewById(R.id.btnEdit);
        btnKeluar   = view.findViewById(R.id.btnKeluar);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditProfil.class));
            }
        });

        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Peringatan!");

                alertDialogBuilder.setMessage("Anda yakin ingin keluar dari akun ini?")
                        .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHandler db = new DatabaseHandler(getActivity());
                                db.truncate();
                                startActivity(new Intent(getActivity(), LoginPetugas.class));
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ((Dashboard) getActivity()).getSupportActionBar().setTitle("Pengaturan");
        super.onCreateOptionsMenu(menu, inflater);
    }
}
