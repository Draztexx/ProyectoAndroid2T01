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
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    lateinit var correo :EditText
    lateinit var password:EditText
    lateinit var loginButton: Button
    lateinit var registerButton:TextView
    lateinit var recordar: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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




    }

    private val clickListener= View.OnClickListener { view->
        when(view.id){
            R.id.loginbutton->{
                guardar()
                AlertDialog.Builder(this)
                    .setTitle("Inicio")
                    .setMessage("Botton funciona")
                    .setPositiveButton("Aceptar"){dialog,_->}.show()
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




}