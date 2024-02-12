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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.OnMapReadyCallback


class HomeFragment : Fragment(),OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var googleMap: GoogleMap

    private lateinit var mapView: MapView

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val rootView = binding.root


        // Configurar el MapView
        mapView = _binding!!.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)



        return rootView
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        // Verificar y solicitar permisos si es necesario
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        } else {
            // Solicitar permisos
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }


        val currentLocation = LatLng(37.7749, -122.4194)

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }


}