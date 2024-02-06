package com.example.proyectoandroid2t01.ui.crear

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import com.example.proyectoandroid2t01.R

class CrearFragment : Fragment() {

    companion object {
        fun newInstance() = CrearFragment()
    }

    private lateinit var viewModel: CrearViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crear, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CrearViewModel::class.java)
        // TODO: Use the ViewModel
    }

}