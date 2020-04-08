package com.example.testingapp.second

import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.MutableLiveData
import com.example.testingapp.R
import com.example.testingapp.setupKoinModule
import com.example.testingapp.stopKoin
import com.example.testingapp.testing.ui.BaseRobot
import org.junit.After
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.koin.androidx.viewmodel.dsl.viewModel

class SecondRobot : BaseRobot() {

    private val viewModel: SecondViewModel = mainViewModelMock()

    private lateinit var scenario: FragmentScenario<SecondFragment>

    private val textTitle = MutableLiveData<String>()

    override fun setupInjections() {
        setupKoinModule {
            viewModel { viewModel }
        }
    }

    override fun setupScenario() {
        scenario = setupFragmentScenario()
    }

    private fun mainViewModelMock(): SecondViewModel {
        val viewModel = mock(SecondViewModel::class.java)
        given(viewModel.textTitle).will { textTitle }
        return viewModel
    }

    fun postText(text: String?) {
        textTitle.postValue(text)
    }

    fun verifyTextIsDisplayed(message: String) {
        isDisplayedWithText(R.id.text, message)
    }

    @After
    fun cleanup() {
        stopKoin()
    }
}