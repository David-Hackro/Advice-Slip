package com.david.hackro.adviceslip

import app.cash.turbine.test
import com.david.hackro.adviceslip.data.AdviceRepositoryImpl
import com.david.hackro.adviceslip.data.data.AdviceDao
import com.david.hackro.adviceslip.data.data.AdviceEntity
import com.david.hackro.adviceslip.data.remote.AdviceApi
import com.david.hackro.adviceslip.data.remote.ResponseAdvice
import com.david.hackro.adviceslip.data.remote.toEntity
import com.david.hackro.adviceslip.domain.AdviceRepository
import com.david.hackro.adviceslip.domain.toDomain
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.internal.wait
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import kotlin.test.assertEquals

class AdviceRepositoryTest {

    @RelaxedMockK
    private lateinit var remoteSource: AdviceApi

    @RelaxedMockK
    private lateinit var localSource: AdviceDao

    private lateinit var objectUnderTest: AdviceRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        setUpRepository()
    }

    @Test
    fun `should wrap Success Result from localSource when localSource is success and save the remote response when remoteSource is success`() =
        runTest {
            val localResponse = AdviceEntity()
            coEvery { localSource.getRandomAdvice() } returns localResponse

            objectUnderTest.getAdvice().test {
                val result = this.awaitItem()
                assertEquals(
                    expected = Result.success(localResponse.toDomain()),
                    actual = result
                )
                awaitComplete()
            }

            coVerifyOrder {
                localSource.getRandomAdvice()
                remoteSource.getAdvice()
                localSource.updateOrInsertAdvice(any())
            }
        }

    @Test
    fun `should wrap Success Result from localSource when localSource is success wrap Failed Result when remoteSource is failed`() =
        runTest {
            val localResponse = AdviceEntity()
            coEvery { localSource.getRandomAdvice() } returns localResponse

            val testException = Exception("Test message")
            coEvery { remoteSource.getAdvice() } throws testException

            objectUnderTest.getAdvice().test {
                val result = this.awaitItem()
                assertEquals(
                    expected = Result.success(localResponse.toDomain()),
                    actual = result
                )

                val secondResult = this.awaitItem()

                assertEquals(
                    expected = Result.failure(testException),
                    actual = secondResult
                )
                awaitComplete()
            }

            coVerifyOrder {
                localSource.getRandomAdvice()
                remoteSource.getAdvice()
            }
        }

    private fun setUpRepository() {
        objectUnderTest = AdviceRepositoryImpl(localSource, remoteSource)
    }

}