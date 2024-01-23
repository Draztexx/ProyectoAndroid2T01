package com.example.proyectoandroid2t01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    lateinit var correo :EditText
    lateinit var password:EditText
    lateinit var loginButton: Button
    lateinit var registerButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        correo=findViewById(R.id.correo)
        password=findViewById(R.id.Password)
        loginButton=findViewById(R.id.logignbutton)
        registerButton=findViewById(R.id.registerbutton)

        loginButton.setOnClickListener(clickListener)
        registerButton.setOnClickListener(clickListener)





    }

    private val clickListener= View.OnClickListener { view->

    }
}