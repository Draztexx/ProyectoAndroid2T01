package com.example.proyectoandroid2t01

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Field

import retrofit2.http.FormUrlEncoded


interface MainApi {
    @POST("/proyectoandroid2t01/buscarusuario.php")
    @FormUrlEncoded
    fun buscarusuario(
        @Field("correo") correo:String?,
        @Field("contraseña") contraseña:String?
    ):Call<UsuarioResponse>

    @POST("/proyectoandroid2t01/crearusuario.php")
    fun crearusuario(
        @Query("Nombre") nombre:String?,
        @Query("Edad") edad:Number?,
        @Query("Correo") correo:String?,
        @Query("Password") password:String?
    ):Call<UsuarioResponse>



}