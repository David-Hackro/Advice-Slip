package com.david.hackro.adviceslip.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.david.hackro.adviceslip.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initObservers()
        initData()
    }

    private fun initObservers() {
        viewModel.state.observe(this) { state ->

            when {
                state.isProgress -> {}
                state.isError -> {}
                state.advice != null -> {
                    binding.textView8.text = state.advice.advice
                }
            }
        }
    }

    private fun initData() {
        viewModel.loadData()
    }


    private fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}