package com.david.hackro.adviceslip.presentation

import android.content.Intent
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
        startActivity(Intent(this, AdviceComponent::class.java))
    }

    private fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initObservers() {
      /*  viewModel.state.observe(this) { state ->

            when {
                state.isProgress -> {
                    binding.progressBar.isVisible = true
                }
                state.isError -> {
                    binding.progressBar.isVisible = false
                }
                else -> {
                    binding.progressBar.isVisible = false
                    binding.advice.text = state.response?.advice
                }
            }
        }*/
    }

    private fun initData() {
        viewModel.loadAdvice()
    }
}