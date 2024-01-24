package com.example.proyectoandroid2t01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class Register : AppCompatActivity() {

    lateinit var nombre :EditText
    lateinit var edad   :EditText
    lateinit var correo :EditText
    lateinit var password   :EditText
    lateinit var loginButton: Button
    lateinit var redirectButton:TextView




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


    }

    private val clickListener= View.OnClickListener { view->
        when(view.id){
            R.id.loginbutton->{
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








}