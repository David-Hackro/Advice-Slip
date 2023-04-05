package com.david.hackro.adviceslip.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.david.hackro.adviceslip.domain.GetAdviceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor(private val getAdviceUseCase: GetAdviceUseCase) : ViewModel() {

    fun loadData() {
        getAdvice()
    }

    private fun getAdvice() = viewModelScope.launch {
        getAdviceUseCase.invoke().collect { result ->
            result.onSuccess {
                Log.i("success:", it.advice.toString())
            }.onFailure {
                Log.i("failure:", it.message.toString())
            }
        }
    }
}