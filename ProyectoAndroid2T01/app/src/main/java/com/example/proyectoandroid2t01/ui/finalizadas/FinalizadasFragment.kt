package com.example.proyectoandroid2t01.ui.finalizadas

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectoandroid2t01.R

class FinalizadasFragment : Fragment() {

    companion object {
        fun newInstance() = FinalizadasFragment()
    }

    private lateinit var viewModel: FinalizadasViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_finalizadas, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FinalizadasViewModel::class.java)
        // TODO: Use the ViewModel
    }

}