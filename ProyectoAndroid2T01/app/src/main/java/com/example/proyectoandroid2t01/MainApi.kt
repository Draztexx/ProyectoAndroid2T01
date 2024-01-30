package com.example.proyectoandroid2t01

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.DELETE

interface MainApi {
    @POST("/localhost/buscarusuario.php")
    fun buscarusuario(
        @Query("Correo") correo:String?,
        @Query("Contraseña") contraseña:String?
    ):Call<UsuarioResponse>

    @POST("/localhost/crearusuario.php")
    fun crearusuario(
        @Query("Nombre") nombre:String?,
        @Query("Edad") edad:Number?,
        @Query("Correo") correo:String?,
        @Query("Password") password:String?
    ):Call<UsuarioResponse>



}