package com.example.testingapp.testing.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.Intents
import com.example.testingapp.testing.R
import org.mockito.ArgumentMatchers

/**
 * This class configures the screen robot for every test
 * to avoid flaky tests, data binding idling resource is registered and unregisterd in each test
 */
abstract class BaseRobotSetup {
    /**
     * An Idling Resource that waits for Data Binding to have no pending bindings
     */
    val dataBindingIdlingResource = DataBindingIdlingResource()

    /**
     * Will be called for each test to register the idling resources and to
     * start the test setup
     */
    fun setup() {
        registerIdlingResource()
        setupInjections()
        setupScenario()
    }

    /**
     * Setting up first the view model(s)
     */
    abstract fun setupInjections()

    /**
     * Setting up the fragment or activity scenario
     * for each screen robot
     */
    abstract fun setupScenario()

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    private fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    /**
     * Setting up the fragment for testing (It is not necessary to initialize an activity)
     */
    inline fun <reified T : Fragment> setupFragmentScenario(
        bundle: Bundle? = null,
        theme: Int = R.style.Theme_AppCompat
    ): FragmentScenario<T> {
        //Setting the theme inside this would break the roboelectric tests
        val scenario = launchFragmentInContainer<T>(fragmentArgs = bundle, themeResId = theme)

        dataBindingIdlingResource.monitorFragment(scenario)
        return scenario
    }

    inline fun <reified T : Activity> setupActivityScenario(intent: Intent? = null): ActivityScenario<T> {
        val scenario = if (intent == null) {
            ActivityScenario.launch(T::class.java)
        } else {
            ActivityScenario.launch<T>(intent)
        }
        dataBindingIdlingResource.monitorActivity(scenario)
        return scenario
    }

    /**
     * IS Called after each test to clear Idling Resource and to stop the
     * current StandAlone Koin application
     */
    @Suppress("unused")
    fun clearTestResources() {
        unregisterIdlingResource()
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    private fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }
}

inline fun <T : BaseRobot> T.check(func: T.() -> Unit = {}) {
    try {
        this.apply {
            Intents.init()
            setup()
            func()
            Intents.release()
        }
    } finally {
        clearTestResources()
    }
}

inline fun <reified R : BaseRobot> with(func: R.() -> Unit) {
    R::class.constructors.first().call().check(func)
}

fun <T : Any> safeEq(value: T): T = ArgumentMatchers.eq(value) ?: value
