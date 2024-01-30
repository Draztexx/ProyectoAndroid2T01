package com.example.proyectoandroid2t01

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var correo :EditText
    lateinit var password:EditText
    lateinit var loginButton: Button
    lateinit var registerButton:TextView
    lateinit var recordar: CheckBox

    private val baseUrl="http://127.0.0.1"
    private lateinit var apiService: MainApi
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition{false}


        setTheme(R.style.Theme_ProyectoAndroid2T01)
        setContentView(R.layout.activity_main)

        correo=findViewById(R.id.correo)
        password=findViewById(R.id.Password)
        loginButton=findViewById(R.id.loginbutton)
        registerButton=findViewById(R.id.redirectregister)
        recordar=findViewById(R.id.CBRecordar)

        loginButton.setOnClickListener(clickListener)
        registerButton.setOnClickListener(clickListener)

        val preferences=getSharedPreferences("User",Context.MODE_PRIVATE)
        correo.setText(preferences.getString("correo",""))
        password.setText(preferences.getString("password",""))
        if(preferences.getBoolean("guardar",false)){
            recordar.isChecked=true
        }

        val retrofit=Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService=retrofit.create(MainApi::class.java)

    }

    private val clickListener= View.OnClickListener { view->
        when(view.id){
            R.id.loginbutton->{
                val correo=correo.text.toString()
                //val contraseña=password.text.toString()
                val contraseña="pepito"
                guardar()
                /*
                AlertDialog.Builder(this)
                    .setTitle("Inicio")
                    .setMessage("Botton funciona")
                    .setPositiveButton("Aceptar"){dialog,_->}.show()
                 */

                if(validarCampos(correo,contraseña)){
                    apiService.buscarusuario(correo,contraseña).enqueue(object : Callback<UsuarioResponse> {
                        override fun onResponse(
                            call: Call<UsuarioResponse>,
                            response: Response<UsuarioResponse>
                        ) {
                            if(response.isSuccessful){
                                val usuarioResponse=response.body()
                                AlertDialog.Builder(this@MainActivity)
                                    .setTitle("Cliente")
                                    .setMessage("Nombre: ${usuarioResponse?.NombreApellido}"+"\nCorreo: ${usuarioResponse?.Correo}"+"\nEdad: ${usuarioResponse?.Edad} ")
                                    .setPositiveButton("Aceptar") { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    .show()
                            }
                        }

                        override fun onFailure(call: Call<UsuarioResponse>, t: Throwable) {
                            showToast("Erorr")
                        }


                    })

                }



            }
            R.id.redirectregister->{
                startActivity(Intent(this@MainActivity,Register::class.java));
            }
        }
    }

    fun guardar(){
        val preferences: SharedPreferences=getSharedPreferences("User", MODE_PRIVATE)
        if(recordar.isChecked){
            val myEditor:SharedPreferences.Editor=preferences.edit()
            myEditor.putString("correo",correo.text.toString())
            myEditor.putString("password",password.text.toString())
            myEditor.putBoolean("guardar",true)
            myEditor.apply()
        }else{
            val myEditor:SharedPreferences.Editor=preferences.edit()
            myEditor.putString("correo","")
            myEditor.putString("password","")
            myEditor.putBoolean("guardar",false)
            myEditor.apply()
        }

    }

    fun validarCampos(correo:String, password:String):Boolean{
        return correo.isNotEmpty() && password.isNotEmpty()
    }

    private fun showToast(message: String){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }



}