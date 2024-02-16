package com.example.proyectoandroid2t01.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.example.proyectoandroid2t01.R
import com.example.proyectoandroid2t01.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions


class HomeFragment : Fragment(),OnMapReadyCallback  {

    private lateinit var map: GoogleMap

    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        mapView = rootView.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        return rootView
    }



    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        crearMarcadorColegio()
    }

    private fun crearMarcadorColegio() {
        val coordenadas = LatLng(40.9688200, -5.6638800)
        val marcador: MarkerOptions = MarkerOptions().position(coordenadas).title("Salamanca")
        map.addMarker(marcador)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 15.0f))
    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

}