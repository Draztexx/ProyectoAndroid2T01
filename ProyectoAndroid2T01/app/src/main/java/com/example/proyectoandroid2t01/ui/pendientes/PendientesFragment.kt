package com.example.proyectoandroid2t01.ui.pendientes

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoandroid2t01.AdminSQLiteOpenHelper
import com.example.proyectoandroid2t01.ListAdapter
import com.example.proyectoandroid2t01.ListElement
import com.example.proyectoandroid2t01.MainApi
import com.example.proyectoandroid2t01.R
import com.example.proyectoandroid2t01.Tareas
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PendientesFragment : Fragment() {

    companion object {
        fun newInstance() = PendientesFragment()
    }

    private val baseUrl="http://10.0.2.2/"
    private lateinit var apiService: MainApi

    private lateinit var dbHelper: AdminSQLiteOpenHelper

    private lateinit var viewModel: PendientesViewModel

    private var elements: MutableList<ListElement> = mutableListOf()

    private var listAdapter: ListAdapter? = null
    private var chanelID="chat"
    private var chanelName="chat"



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_pendientes, container, false)




        //configuramos



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



        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar la lista después de que la vista haya sido creada
        init()
    }

    private fun init() {
        elements.clear()
        agregar()

        listAdapter = ListAdapter(elements, requireContext())

        listAdapter?.itemClickListener = object : ListAdapter.ListItemClickListener {
            override fun onEditButtonClick(position: Int,idtareas: Int) {
                mostrarFormulario(elements[position])
            }

            override fun onDeleteButtonClick(position: Int,idtareas: Int) {
                eliminar(idtareas)
            }
        }
        val recyclerView: RecyclerView = requireView().findViewById(R.id.listRecyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = listAdapter
    }


    @SuppressLint("SuspiciousIndentation")
    private fun agregar(){
        val correo=consultar()
        val estado="Finalizado"


            apiService.tareas(correo?.get(0),estado)
                .enqueue(object :
                    Callback<List<Tareas>> {
                    override fun onResponse(
                        call: Call<List<Tareas>>,
                        response: Response<List<Tareas>>
                    ) {
                        if (response.isSuccessful) {
                            val usuarioResponse = response.body()
                            if (usuarioResponse != null) {
                                for (tarea in usuarioResponse) {
                                    val listElement = ListElement(
                                        idtareas=tarea.idtareas,
                                        nombre = tarea.nombre,
                                        fecha = tarea.fecha,
                                        prioridad = tarea.prioridad,
                                        estado = tarea.estado,
                                        correo = tarea.correo,
                                        descripcion = tarea.descripcion
                                    )
                                    val currentDate = Calendar.getInstance()
                                    val tareaDate = Calendar.getInstance().apply {
                                        time = listElement.fecha
                                    }
                                    if (currentDate.timeInMillis>=tareaDate.timeInMillis){
                                        notificacion("Hoy es el plazo para la tarea: ${tarea.nombre}, ${tarea.estado}, ${tarea.fecha}", tarea.idtareas.toInt())
                                    }
                                    elements.add(listElement)
                                }
                                listAdapter?.notifyDataSetChanged()
                            } else {
                                showToast("Erorr al coger el response")
                            }

                        }
                    }


                    override fun onFailure(call: Call<List<Tareas>>, t: Throwable) {
                        showToast("Error al conectarse a la base de datos" + t.toString())
                    }

                })

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

    private fun eliminar(idtareas:Number){
        apiService.eliminartareas(idtareas)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>,response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val mensaje = response.body()
                        if (mensaje != null) {
                            showToast(mensaje?.get("mensaje").toString())
                            elements.clear()
                            agregar()
                            listAdapter?.notifyDataSetChanged()
                        } else {
                            showToast("Respuesta vacia")
                        }

                    }else{
                        showToast("Error en la respuesta del servidor")
                    }
                }


                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    showToast("Error al conectarse a la base de datos" + t.toString())
                    Log.e("API_ERROR", "Error en la llamada a la API", t)
                }

            })
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PendientesViewModel::class.java)
        // TODO: Use the ViewModel
    }


    private fun showToast(message: String){
        Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
    }

    private fun mostrarFormulario(listElement: ListElement) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.formulario_layout, null)
        val nombreEditText = dialogLayout.findViewById<EditText>(R.id.nombreEditText)
        val fechaEditText = dialogLayout.findViewById<EditText>(R.id.fechaEditText)
        val prioridadEditText = dialogLayout.findViewById<EditText>(R.id.prioridadEditText)
        val estadoEditText = dialogLayout.findViewById<EditText>(R.id.estadoEditText)
        val correoEditText = dialogLayout.findViewById<EditText>(R.id.correoEditText)
        val descripcionEditText = dialogLayout.findViewById<EditText>(R.id.descripcionEditText)


        nombreEditText.setText(listElement.nombre)
        fechaEditText.setText(listElement.fecha.toString())
        prioridadEditText.setText(listElement.prioridad)
        estadoEditText.setText(listElement.estado)
        correoEditText.setText(listElement.correo)
        descripcionEditText.setText(listElement.descripcion)

        builder.setView(dialogLayout)
            .setTitle("Editar elemento")
            .setPositiveButton("Aceptar") { dialog, _ ->

                listElement.nombre = nombreEditText.text.toString()
                listElement.fecha = Date(fechaEditText.text.toString())
                listElement.prioridad = prioridadEditText.text.toString()
                listElement.estado = estadoEditText.text.toString()
                listElement.correo = correoEditText.text.toString()
                listElement.descripcion = descripcionEditText.text.toString()

                val nombre = nombreEditText.text.toString()
                val fecha = fechaEditText.text.toString()
                val prioridad = prioridadEditText.text.toString()
                val estado = estadoEditText.text.toString()
                val correo = correoEditText.text.toString()
                val descripcion = descripcionEditText.text.toString()

                apiService.modificartareas(listElement.idtareas.toInt(),nombre,descripcion,fecha,prioridad,estado,correo)
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
                                    elements.clear()
                                    agregar()
                                    listAdapter?.notifyDataSetChanged()
                                } else {
                                    showToast("Erorr al coger el response")
                                }

                            }
                        }


                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            showToast("Error al conectarse a la base de datos" + t.toString())
                        }

                    })

                listAdapter?.notifyDataSetChanged()

                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun notificacion(mensaje : String,id:Int){
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(chanelID, chanelName, importance)

        val manager=requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.createNotificationChannel(channel)

        val notificacionManager=NotificationManagerCompat.from(requireContext())

        val notificacion=NotificationCompat.Builder(requireContext(),chanelID).also { noti->
            noti.setContentTitle("titulo")
            noti.setContentText(mensaje)
            noti.setSmallIcon(R.drawable.baseline_library_add_check_24)
        }.build()


        notificacionManager.notify(id,notificacion)

    }
}