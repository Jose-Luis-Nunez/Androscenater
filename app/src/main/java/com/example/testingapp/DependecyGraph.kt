package com.example.testingapp

import com.example.testingapp.main.MainViewModel
import com.example.testingapp.second.SecondViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object DependencyGraph {

    fun get() = mutableListOf(
        appModule
    )

    private val appModule = module {
        single { get() as MyApplication }
        viewModel { MainViewModel() }
        viewModel { SecondViewModel() }
        single<TextUtil> { TextUtilImpl() }
    }
}