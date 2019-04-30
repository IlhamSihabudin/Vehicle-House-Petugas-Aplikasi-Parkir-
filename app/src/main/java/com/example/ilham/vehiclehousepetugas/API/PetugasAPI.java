package com.example.ilham.vehiclehousepetugas.API;

import com.example.ilham.vehiclehousepetugas.Model.Value;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PetugasAPI {
    @FormUrlEncoded
    @POST("petugas_login.php")
    Call<Value> login(@Field("username") String username,
                      @Field("password") String password);

    @FormUrlEncoded
    @POST("petugas_select_where.php")
    Call<Value> select_where(@Field("id_petugas") String id_petugas);

    @FormUrlEncoded
    @POST("select_where.php")
    Call<Value> select_where_user(@Field("id_user") String id_user);

    @FormUrlEncoded
    @POST("petugas_update_profil.php")
    Call<Value> update(@Field("id_petugas") String id_petugas,
                       @Field("nama_petugas") String nama_petugas,
                       @Field("no_tlp") String no_tlp,
                       @Field("username") String username,
                       @Field("password") String password);

    @FormUrlEncoded
    @POST("petugas_masuk_parkir.php")
    Call<Value> masuk_parkir(@Field("id_user") String id_user,
                             @Field("id_petugas") String id_petugas);

    @FormUrlEncoded
    @POST("petugas_keluar_parkir.php")
    Call<Value> keluar_parkir(@Field("id_user") String id_user,
                             @Field("id_petugas") String id_petugas);

    @GET("petugas_select_in_parkir.php")
    Call<Value> view_in_parkir();
}
