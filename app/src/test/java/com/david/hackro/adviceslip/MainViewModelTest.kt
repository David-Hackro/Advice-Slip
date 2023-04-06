package com.david.hackro.adviceslip

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.david.hackro.adviceslip.domain.Advice
import com.david.hackro.adviceslip.domain.GetAdviceUseCase
import com.david.hackro.adviceslip.presentation.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val mockRule = MainDispatcherRule()

    /*@get:Rule
    val rule = InstantTaskExecutorRule()
*/
    @RelaxedMockK
    private lateinit var getAdviceUseCase: GetAdviceUseCase

    private lateinit var objectUnderTest: MainViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        setUpMainViewModel()
    }

    @Test
    fun `should update state with sucess response when getAdviceUseCase is success`() {
        val getAdviceUseCaseResponse = Advice("advice", 1)
        coEvery { getAdviceUseCase.invoke() } returns flowOf(Result.success(getAdviceUseCaseResponse))

        objectUnderTest.loadData()

        assertEquals(
            expected = MutableLiveData(MainViewModel.State(getAdviceUseCaseResponse)).value,
            actual = objectUnderTest.state.value
        )
    }


    private fun setUpMainViewModel() {
        objectUnderTest = MainViewModel(getAdviceUseCase)
    }
}