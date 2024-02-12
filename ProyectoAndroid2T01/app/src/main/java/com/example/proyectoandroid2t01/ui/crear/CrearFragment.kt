package com.example.proyectoandroid2t01.ui.crear

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.proyectoandroid2t01.AdminSQLiteOpenHelper
import com.example.proyectoandroid2t01.MainActivity
import com.example.proyectoandroid2t01.MainApi
import com.example.proyectoandroid2t01.R
import com.example.proyectoandroid2t01.UsuarioResponse
import com.example.proyectoandroid2t01.ui.home.HomeFragment
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar

class CrearFragment : Fragment(), SensorEventListener {

    companion object {
        fun newInstance() = CrearFragment()
    }

    private lateinit var viewModel: CrearViewModel
    private lateinit var sensorManager: SensorManager

    private val baseUrl="http://10.0.2.2/"
    private lateinit var apiService: MainApi

    private lateinit var dbHelper: AdminSQLiteOpenHelper

    private lateinit var nombre:EditText
    private lateinit var descripcion:EditText
    private lateinit var fecha:EditText
    private lateinit var prioridad:Spinner
    private lateinit var estado:Spinner


    private var accelerometer: Sensor? = null
    private var lastShakeTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //preparar la concexion a bases------------------------------
        val gson = GsonBuilder().setLenient().create()
        val retrofit= Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        apiService=retrofit.create(MainApi::class.java)
        //-----------------------------------------------------------
        dbHelper= AdminSQLiteOpenHelper(requireContext(),"aplicacion",null,1)
        //------------------------------------------------------------


        val rootView = inflater.inflate(R.layout.fragment_crear, container, false)
        val spinnerPrio: Spinner=rootView.findViewById(R.id.spinnerPrioridad)
        val spinnerEst: Spinner=rootView.findViewById(R.id.spinnerEstado)
        val fechaEdit:EditText= rootView.findViewById(R.id.fecha)

        //-----------------------------------------------------------

        nombre=rootView.findViewById(R.id.nombre)
        descripcion=rootView.findViewById(R.id.descripcion)
        fecha=rootView.findViewById(R.id.fecha)
        prioridad=rootView.findViewById(R.id.spinnerPrioridad)
        estado=rootView.findViewById(R.id.spinnerEstado)

        //_-------------------------------------------------
        sensorManager=requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val adaptador=ArrayAdapter.createFromResource(
            requireContext(),R.array.opciones_prioridad,android.R.layout.simple_spinner_item
        )
        spinnerPrio.adapter=adaptador

        val adaptador2=ArrayAdapter.createFromResource(
            requireContext(),R.array.opciones_estado,android.R.layout.simple_spinner_item
        )
        spinnerEst.adapter=adaptador2

        //Fechas--------------------------------------------------


        val calendar=   Calendar.getInstance()
        val año=calendar.get(Calendar.YEAR)
        val mes=calendar.get(Calendar.MONTH)
        val dia=calendar.get(Calendar.DAY_OF_MONTH)

        fechaEdit.setOnFocusChangeListener {view,hasFocus->
            if(hasFocus){
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, selectedYear, selectedMonth, selectedDay ->
                        val fechaSeleccionada = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                        fechaEdit.setText(fechaSeleccionada)
                    },
                    año,
                    mes,
                    dia
                )

                datePickerDialog.show()

            }
        }

        //----------------------------------------------------------















        //return inflater.inflate(R.layout.fragment_crear, container, false )
        return rootView
    }


    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Manejar cambios en la precisión si es necesario
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val currentTime = System.currentTimeMillis()
            val timeDifference = currentTime - lastShakeTime

            if (timeDifference > 1000) { // Evitar detección de agitación muy frecuente
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val acceleration = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()

                if (acceleration > 10) {
                    this.agregar()
                    nombre.setText("")
                    descripcion.setText("")
                    fecha.setText("")


                    lastShakeTime = currentTime
                }
            }
        }
    }

    private fun showToast(message: String){
        Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CrearViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun agregar(){
        val nombre=nombre.text.toString()
        val descripcion=descripcion.text.toString()
        val fecha=fecha.text.toString()
        val prioridad=prioridad.selectedItem.toString()
        val estado=estado.selectedItem.toString()
        val correo=consultar()

        if(validarCampos(nombre,descripcion,fecha,prioridad,estado)) {
            apiService.creartareas(nombre, descripcion, fecha, prioridad, estado, correo?.get(0))
                .enqueue(object :
                    Callback<JsonObject> {
                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        if (response.isSuccessful) {
                            val usuarioResponse = response.body()
                            if (usuarioResponse != null) {
                                showToast(usuarioResponse.get("mensaje").toString())

                            } else {
                                showToast("Erorr al coger el response")
                            }

                        }
                    }


                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        showToast("Error al conectarse a la base de datos" + t.toString())
                    }

                })
        }else{
            showToast("Los campos estan vacios")
        }

    }

    fun validarCampos(nombre:String,descripcion:String,fecha:String, prioridad:String,estado:String ):Boolean{
        return  descripcion.isNotEmpty() && fecha.isNotEmpty() && prioridad.isNotEmpty() && estado.isNotEmpty() && nombre.isNotEmpty()
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

    private fun salir(){
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_menu, HomeFragment())
        fragmentTransaction.commitAllowingStateLoss()
    }



}



