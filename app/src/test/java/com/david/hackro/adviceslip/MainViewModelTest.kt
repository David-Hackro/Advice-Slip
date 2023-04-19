package com.david.hackro.adviceslip

import androidx.lifecycle.MutableLiveData
import com.david.hackro.adviceslip.domain.Advice
import com.david.hackro.adviceslip.domain.GetAdviceUseCase
import com.david.hackro.adviceslip.presentation.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val mockRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var getAdviceUseCase: GetAdviceUseCase

    private lateinit var objectUnderTest: MainViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should update state with success response when getAdviceUseCase is success`() {
        val getAdviceUseCaseResponse = Advice("advice", 1)
        coEvery { getAdviceUseCase.invoke() } returns flowOf(Result.success(getAdviceUseCaseResponse))

        setUpMainViewModel()

        assertEquals(
            expected = MutableStateFlow(MainViewModel.State("", false, true)).value,
            actual = objectUnderTest.state.value
        )

        assertEquals(
            expected = MutableStateFlow(
                MainViewModel.State(getAdviceUseCaseResponse.advice, false, false)).value,
            actual = objectUnderTest.state.value
        )
    }


    private fun setUpMainViewModel() {
        objectUnderTest = MainViewModel(getAdviceUseCase)
    }
}