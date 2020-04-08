package com.example.testingapp.main

import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.example.testingapp.R
import com.example.testingapp.TextUtil
import com.example.testingapp.stopKoin
import com.example.testingapp.testing.ui.BaseRobot
import org.junit.After
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock

class MainRobot : BaseRobot() {

    private val viewModel: MainViewModel = mainViewModelMock()
    private val textUtil = textUtilMock()

    private lateinit var scenario: ActivityScenario<MainActivity>

    private val textTitle = MutableLiveData<String>()
    private val textSubTitle = MutableLiveData<String>()

    override fun setupInjections() {
    // A better looking version that needs an extension function
//        setupKoinModule {
//            viewModel { viewModel }
//            single { textUtil }
//        }
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(module {
                viewModel { viewModel }
                single { textUtil }
            })
        }
    }

    override fun setupScenario() {
        scenario = setupActivityScenario()
    }

    private fun mainViewModelMock(): MainViewModel {
        val viewModel = mock(MainViewModel::class.java)
        given(viewModel.textTitle).will { textTitle }
        given(viewModel.textSubTitle).will { textSubTitle }
        return viewModel
    }

    private fun textUtilMock(): TextUtil {
        val textUtil = mock(TextUtil::class.java)
        given(textUtil.welcomeText()).will { "Mock" }
        return textUtil
    }

    fun postTitleText(text: String?) {
        textTitle.postValue(text)
    }

    fun postSubTitleText(text: String?) {
        textSubTitle.postValue(text)
    }

    fun verifyTitleIsDisplayed(message: String) {
        isDisplayedWithText(R.id.title, message)
    }

    fun verifySubTitleIsDisplayed(message: String) {
        isDisplayedWithText(R.id.sub_title, message)
    }

    fun click() {
        clickOnView(R.id.button)
    }

    @After
    fun cleanup() {
        stopKoin()
    }
}