package com.example.proyectoandroid2t01

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.proyectoandroid2t01.databinding.ActivityMenuBinding
import com.google.android.material.navigation.NavigationView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MenuActivity : AppCompatActivity() {

    //Variables de conexion

    private val baseUrl="http://10.0.2.2/"
    //private val baseUrl="http://185.27.134.139/"
    private lateinit var apiService: MainApi

    //
    private lateinit var dbHelper: AdminSQLiteOpenHelper

    var player: MediaPlayer? = null
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
        player = MediaPlayer.create(this, R.raw.musica)


        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMenu.toolbar)

        binding.appBarMenu.fab.setOnClickListener { view ->
            eliminarUsuario()


            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected( item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_settings ->{
                Toast.makeText(this,"Musica",Toast.LENGTH_LONG).show()
                playpause()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_menu)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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

    private fun eliminarUsuario() {
        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM usuario")
        db.close()
    }



    private fun playpause() {
        if (player != null) {
            if (player!!.isPlaying) {
                player!!.pause()
            } else {
                player!!.start()
            }
        }
    }






}