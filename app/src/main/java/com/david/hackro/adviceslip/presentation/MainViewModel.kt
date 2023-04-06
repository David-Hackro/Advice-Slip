package com.david.hackro.adviceslip.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.david.hackro.adviceslip.domain.Advice
import com.david.hackro.adviceslip.domain.GetAdviceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getAdviceUseCase: GetAdviceUseCase) :
    ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> = _state

    fun loadData() {
        getAdvice()
    }

    private fun getAdvice() = viewModelScope.launch {
        getAdviceUseCase.invoke().collect { result ->
            result.onSuccess {
               //el rLog.i("success:", it.advice.toString())
                _state.value = State(it)
            }.onFailure {
                _state.value = State(isError = true)
                //Log.i("failure:", it.message.toString())
            }
        }
    }

    data class State(val advice: Advice? = null, val isError: Boolean = false)
}