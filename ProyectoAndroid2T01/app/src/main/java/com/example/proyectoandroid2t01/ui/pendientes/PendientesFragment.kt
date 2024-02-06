package com.example.proyectoandroid2t01.ui.pendientes

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectoandroid2t01.R

class PendientesFragment : Fragment() {

    companion object {
        fun newInstance() = PendientesFragment()
    }

    private lateinit var viewModel: PendientesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pendientes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PendientesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}