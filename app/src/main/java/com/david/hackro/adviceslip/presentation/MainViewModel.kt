package com.david.hackro.adviceslip.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.david.hackro.adviceslip.domain.GetAdviceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getAdviceUseCase: GetAdviceUseCase) :
    ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        loadAdvice()
    }

    fun loadAdvice() {
        getAdvice()
    }

    private fun getAdvice() = viewModelScope.launch {
        _state.update { it.copy(isProgress = true, isError = false) }

        getAdviceUseCase.invoke().collect { result ->
            result.onSuccess { advice ->
                _state.update {
                    it.copy(
                        response = advice.advice,
                        isError = false,
                        isProgress = false
                    )
                }
            }.onFailure {
                _state.update {
                    it.copy(isError = true, isProgress = false)
                }
            }
        }
    }

    data class State(
        val response: String = "",
        val isError: Boolean = false,
        val isProgress: Boolean = false,
    )
}