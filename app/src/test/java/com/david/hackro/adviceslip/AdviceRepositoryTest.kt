package com.david.hackro.adviceslip

import app.cash.turbine.test
import com.david.hackro.adviceslip.data.AdviceRepositoryImpl
import com.david.hackro.adviceslip.data.data.AdviceDao
import com.david.hackro.adviceslip.data.data.AdviceEntity
import com.david.hackro.adviceslip.data.remote.AdviceApi
import com.david.hackro.adviceslip.domain.AdviceRepository
import com.david.hackro.adviceslip.domain.toDomain
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
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
    fun `should wrap Success Result from localSource when is success and save the remote success response`() =
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
    fun `should wrap Success Result from localSource when is success and remoteSource is failure `() =
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
                awaitComplete()
            }

            coVerifyOrder {
                localSource.getRandomAdvice()
                remoteSource.getAdvice()
            }
        }


    @Test
    fun `should wrap Failure Result when localSource is failure and remoteSource is failure`() =
        runTest {

            val localException = Exception("Test message")
            coEvery { localSource.getRandomAdvice() } throws localException

            val remoteException = Exception("Test message")
            coEvery { remoteSource.getAdvice() } throws remoteException

            objectUnderTest.getAdvice().test {
                val result = this.awaitItem()

                assertEquals(
                    expected = Result.failure(remoteException),
                    actual = result
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