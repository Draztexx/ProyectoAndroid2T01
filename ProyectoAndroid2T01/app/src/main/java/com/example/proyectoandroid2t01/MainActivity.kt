package com.example.proyectoandroid2t01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    lateinit var correo :EditText
    lateinit var password:EditText
    lateinit var loginButton: Button
    lateinit var registerButton:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        correo=findViewById(R.id.correo)
        password=findViewById(R.id.Password)
        loginButton=findViewById(R.id.loginbutton)
        registerButton=findViewById(R.id.redirectregister)

        loginButton.setOnClickListener(clickListener)
        registerButton.setOnClickListener(clickListener)

    }

    private val clickListener= View.OnClickListener { view->
        when(view.id){
            R.id.loginbutton->{
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
}