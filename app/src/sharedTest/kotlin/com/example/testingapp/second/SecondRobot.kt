package com.example.testingapp.second

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.MutableLiveData
import com.example.testingapp.R
import com.example.testingapp.setupKoinModule
import com.example.testingapp.stopKoin
import com.example.testingapp.testing.ui.BaseRobot
import com.google.common.truth.Truth
import org.junit.After
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.koin.androidx.viewmodel.dsl.viewModel

class SecondRobot : BaseRobot() {

    private val viewModel: SecondViewModel = mainViewModelMock()

    private lateinit var scenario: FragmentScenario<SecondFragment>

    private val textTitle = MutableLiveData<String>()

    private val testText = "test"

    override fun setupInjections() {
        setupKoinModule {
            viewModel { (text: String) ->
                // Testing that adding a bundle worked
                Truth.assertThat(text).isEqualTo(testText)
                viewModel
            }
        }
    }

    override fun setupScenario() {
        val bundle = Bundle()
        bundle.putString(SecondFragment.KEY_TEXT, "test");
        scenario = setupFragmentScenario(bundle = bundle)
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