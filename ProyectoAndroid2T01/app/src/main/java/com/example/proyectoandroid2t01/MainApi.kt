package com.example.proyectoandroid2t01

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Field

import retrofit2.http.FormUrlEncoded
import java.util.Date


interface MainApi {
    @POST("/proyectoandroid2t01/buscarusuario.php")
    @FormUrlEncoded
    fun buscarusuario(
        @Field("correo") correo:String?,
        @Field("contraseña") contraseña:String?
    ):Call<UsuarioResponse>

    @POST("/proyectoandroid2t01/crearusuario.php")
    @FormUrlEncoded
    fun crearusuario(
        @Field("nombreapellido") nombreapellido:String?,
        @Field("edad") edad:Number?,
        @Field("correo") correo:String?,
        @Field("contraseña") password:String?
    ):Call<UsuarioResponse>

    @POST("/proyectoandroid2t01/creartareas.php")
    @FormUrlEncoded
    fun creartareas(
        @Field("nombre") nombre:String?,
        @Field("descripcion") descripcion:String?,
        @Field("fecha") fecha:String?,
        @Field("prioridad") prioridad:String?,
        @Field("estado") estado:String?,
        @Field("correo") correo:String?,
    ):Call<JsonObject>


    @POST("/proyectoandroid2t01/tareas.php")
    @FormUrlEncoded
    fun tareas(
        @Field("correo") correo: String?,
        @Field("estado") estado:String?,
    ):Call<List<Tareas>>

    @POST("/proyectoandroid2t01/eliminartareas.php")
    @FormUrlEncoded
    fun eliminartareas(
        @Field("idtareas") idtareas: Number?,
    ):Call<JsonObject>

    @POST("/proyectoandroid2t01/modificartareas.php")
    @FormUrlEncoded
    fun modificartareas(
        @Field("idtareas") idtareas: Number?,
        @Field("nombre") nombre:String?,
        @Field("descripcion") descripcion:String?,
        @Field("fecha") fecha: String?,
        @Field("prioridad") prioridad:String?,
        @Field("estado") estado:String?,
        @Field("correo") correo:String?,
    ):Call<JsonObject>

}