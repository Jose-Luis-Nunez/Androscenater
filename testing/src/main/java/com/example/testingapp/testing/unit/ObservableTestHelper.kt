package com.example.testingapp.testing.unit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.mockito.Mockito

class ObservableTestHelper<T> {

    /**
     * Start observing a given instance of [MutableLiveData] for a test case.
     * Result will be an instance of [Subscription] that van be validated using [Subscription.verify]
     */
    @Suppress("UNCHECKED_CAST")
    fun startObservation(observable: LiveData<T>): Subscription<T> {
        val observer = Mockito.mock(Observer::class.java) as Observer<T>
        observable.observeForever(observer)
        return Subscription(observer)
    }

    class Subscription<T>(private val observer: Observer<T>) {
        /**
         * Check if the observable emits the expected value
         */
        fun verify(expected: T?) {
            Mockito.verify(observer).onChanged(expected)
        }

        fun verifyNoMoreInteractions() {
            return Mockito.verifyNoMoreInteractions(observer)
        }

        fun verifyZeroInteractions() {
            return Mockito.verifyZeroInteractions(observer)
        }
    }

    /**
     * Check if a given observable emits the expected value
     */
    fun verifyObservable(observable: Observable<T>, expectedValue: T, message: String? = null) {
        val testSubscriber = TestObserver<T>()
        observable.subscribe(testSubscriber)

        if (message != null) {
            Assert.assertEquals(message, testSubscriber.values().last(), expectedValue)
        } else {
            Assert.assertEquals(testSubscriber.values().last(), expectedValue)
        }
    }

    /**
     * Check if a given completable is completed
     */
    fun verifyVoidObservableCompleted(completable: Completable) {
        val testSubscriber = TestObserver<Void>()
        completable.subscribe(testSubscriber)
        testSubscriber.assertComplete()
    }
}
