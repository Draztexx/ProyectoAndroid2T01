package com.example.proyectoandroid2t01

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Register : AppCompatActivity() {

    lateinit var nombre :EditText
    lateinit var edad   :EditText
    lateinit var correo :EditText
    lateinit var password   :EditText
    lateinit var loginButton: Button
    lateinit var redirectButton:TextView

    private val baseUrl="http://10.0.2.2/"
    private lateinit var apiService: MainApi




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        nombre=findViewById(R.id.nombre)
        edad=findViewById(R.id.edad)
        correo=findViewById(R.id.correo)
        password=findViewById(R.id.password)
        loginButton=findViewById(R.id.loginbutton)
        redirectButton=findViewById(R.id.redirectlogin)

        loginButton.setOnClickListener(clickListener)
        redirectButton.setOnClickListener(clickListener)

        //preparar conexion a nases------------------------------
        val retrofit=Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService=retrofit.create(MainApi::class.java)
        //-----------------------------------------------------------


    }

    private val clickListener= View.OnClickListener { view->
        when(view.id){
            R.id.loginbutton->{
                val nombreapellido=nombre.text.toString()
                val edad=edad.text.toString().toInt()
                val correo=correo.text.toString()
                val contraseña=password.text.toString()




                if(validarCampos(nombreapellido,edad,correo,contraseña)){
                    apiService.crearusuario(nombreapellido,edad,correo,contraseña).enqueue(object :
                        Callback<UsuarioResponse> {
                        override fun onResponse(
                            call: Call<UsuarioResponse>,
                            response: Response<UsuarioResponse>
                        ) {
                            if(response.isSuccessful){
                                val usuarioResponse=response.body()
                                if(usuarioResponse?.nombreapellido!="vacio"){
                                    AlertDialog.Builder(this@Register)
                                        .setTitle("Vienvenido")
                                        .setMessage("Nombre: ${usuarioResponse?.nombreapellido} "+"\nCorreo: ${usuarioResponse?.correo}"+"\nEdad: ${usuarioResponse?.edad} ")
                                        .setPositiveButton("Aceptar") { dialog, _ ->
                                            dialog.dismiss()
                                        }
                                        .show()
                                    startActivity(Intent(this@Register,MainActivity::class.java))

                                }else{
                                    showToast("Contraseña o Correo incorrectos")
                                }

                            }
                        }

                        override fun onFailure(call: Call<UsuarioResponse>, t: Throwable) {
                            showToast("Erro al conectarse a la base de datos"+t.toString())
                            AlertDialog.Builder(this@Register)
                                .setTitle("Vienvenido")
                                .setMessage(t.toString())
                                .setPositiveButton("Aceptar") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()



                        }


                    })
                }















                AlertDialog.Builder(this)
                    .setTitle("Register")
                    .setMessage("Botton funciona")
                    .setPositiveButton("Aceptar"){dialog,_->}.show()
            }

            R.id.redirectlogin->{
                startActivity(Intent(this@Register,MainActivity::class.java))
            }

        }






    }



    fun validarCampos(nombre:String,edad:Int,correo:String, password:String, ):Boolean{
        return correo.isNotEmpty() && password.isNotEmpty() && nombre.isNotEmpty() && edad>=0
    }

    private fun showToast(message: String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }





}