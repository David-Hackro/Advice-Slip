package com.david.hackro.adviceslip

import com.david.hackro.adviceslip.domain.Advice
import com.david.hackro.adviceslip.domain.GetAdviceUseCase
import com.david.hackro.adviceslip.presentation.viewmodel.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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
        setUpMainViewModel()
    }

    @Test
    fun `should update state with success response when getAdviceUseCase is success`() {
        val getAdviceRepositoryResponse = Advice("advice", 1)
        coEvery { getAdviceUseCase.invoke() } returns flowOf(
            Result.success(
                getAdviceRepositoryResponse
            )
        )

        assertEquals(
            expected = MutableStateFlow(
                MainViewModel.State()
            ).value,
            actual = objectUnderTest.state.value
        )

        objectUnderTest.loadAdvice()

        assertEquals(
            expected = MutableStateFlow(
                MainViewModel.State(getAdviceRepositoryResponse.advice)
            ).value,
            actual = objectUnderTest.state.value
        )
    }

    @Test
    fun `should update state with failure response when getAdviceUseCase is failure`() {

        val testException = Exception("Test message")

        coEvery { getAdviceUseCase.invoke() } returns flowOf(
            Result.failure(
                testException
            )
        )

        assertEquals(
            expected = MutableStateFlow(
                MainViewModel.State()
            ).value,
            actual = objectUnderTest.state.value
        )

        objectUnderTest.loadAdvice()

        assertEquals(
            expected = MutableStateFlow(
                MainViewModel.State(isError = true)
            ).value,
            actual = objectUnderTest.state.value
        )
    }

    private fun setUpMainViewModel() {
        objectUnderTest = MainViewModel(getAdviceUseCase)
    }
}