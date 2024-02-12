package com.example.proyectoandroid2t01

import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectoandroid2t01.databinding.ActivityMenuBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MenuActivity : AppCompatActivity() {

    //Variables de conexion

    private val baseUrl="http://10.0.2.2/"
    private lateinit var apiService: MainApi

    //
    private lateinit var dbHelper: AdminSQLiteOpenHelper


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inicializando variables de conexion

        val retrofit= Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService=retrofit.create(MainApi::class.java)

        //------------------------------------------------------------------

        //preparar la conxion a SQLITE--------------------------------
        dbHelper=AdminSQLiteOpenHelper(this,"aplicacion",null,1)
        //__________________________________________________________________

        //consultar sqlite----------------------
        val usuario=consultar()

        //--------------------------------------

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMenu.toolbar)

        binding.appBarMenu.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_menu)

        // modificar texto del header del desplegable lateral
        val headerView = navView.getHeaderView(0)

        val headerNombre: TextView = headerView.findViewById(R.id.Nombremenu)
        val headerCorreo: TextView = headerView.findViewById(R.id.Correomenu)

        headerNombre.text = usuario?.get(1)
        headerCorreo.text = usuario?.get(0)

        // -----------------------------------------------------------------

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_crear, R.id.nav_pendientes,R.id.nav_finalizadas
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_menu)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    private fun showToast(message: String){

    }

    private fun consultar():Array<String>?{
        val db=dbHelper.readableDatabase
        val cursor=db.rawQuery("SELECT correo,nombre,edad FROM usuario",null)
        var Usuario:Array<String>?=null
        if(cursor.moveToFirst()){
            val correo=cursor.getString(0)
            val nombre=cursor.getString(1)
            val edad=cursor.getString(2)
            Usuario=arrayOf(correo,nombre,edad)
        }

        cursor.close()
        db.close()
        return Usuario

    }










}