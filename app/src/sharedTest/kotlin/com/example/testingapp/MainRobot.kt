package com.example.testingapp

import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ActivityScenario
import com.example.testingapp.main.MainActivity
import com.example.testingapp.main.MainViewModel
import com.example.testingapp.testing.ui.BaseRobot
import com.example.testingapp.testing.ui.setupKoinModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock

class MainRobot : BaseRobot() {

    private val viewModel: MainViewModel = mainViewModelMock()
    private val textUtil = textUtilMock()

    private lateinit var scenario: ActivityScenario<MainActivity>

    private val textTitle = MutableLiveData<String>()
    private val textSubTitle = MutableLiveData<String>()

    override fun setupInjections() {
        setupKoinModule {
            viewModel { viewModel }
            single { textUtil }
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


}