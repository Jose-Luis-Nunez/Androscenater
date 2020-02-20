package com.example.testingapp.testing.ui

import android.app.Activity
import androidx.test.core.app.ActivityScenario

abstract class BaseActivityScenario<T : Activity> : BaseRobot() {
// setting up the scenario here and making it possible to access it from the robot
//    override fun setupTestComponents() {
//        scenario = setupActivityScenario()
//        setupKoinComponents()
//    }
//
//    abstract fun setupKoinComponents()
//
//    private lateinit var scenario: ActivityScenario<T>
}



