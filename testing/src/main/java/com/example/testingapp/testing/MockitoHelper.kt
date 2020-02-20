package com.example.testingapp.testing

import org.mockito.Mockito

@Suppress("UNCHECKED_CAST")
private fun <T> uninitialized(): T = null as T

/**
 * alternative to [Mockito.any] that can also be used for not-null values
 */
fun <T> any(): T {
    Mockito.any<T>()
    return uninitialized()
}
