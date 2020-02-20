package com.example.testingapp.testing.ui

import androidx.test.core.app.ApplicationProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.ModuleDeclaration
import org.koin.dsl.module

/**
 * Setting up several passed mocked view models to be used by the fragment or activity
 * The module declaration allows to use the dsl from view model like -> viewModel { mockedViewmodel }
 */
fun setupKoinModule(moduleDeclaration: ModuleDeclaration) {
    startKoin {
        androidContext(ApplicationProvider.getApplicationContext())
        loadKoinModules(module(moduleDeclaration = moduleDeclaration))
    }
}
