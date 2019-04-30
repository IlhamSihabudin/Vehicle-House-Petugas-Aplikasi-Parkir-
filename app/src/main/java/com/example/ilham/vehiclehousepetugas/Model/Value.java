package com.example.ilham.vehiclehousepetugas.Model;

import java.util.List;

public class Value {
    String message;
    String status;
    String id_petugas;
    String bayar;
    List<Petugas> result_petugas;
    List<User> result_user;

    public String getId_petugas() {
        return id_petugas;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public String getBayar() {
        return bayar;
    }

    public List<Petugas> getResult_petugas() {
        return result_petugas;
    }

    public List<User> getResult_user() {
        return result_user;
    }
}
