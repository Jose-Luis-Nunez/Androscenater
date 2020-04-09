package com.example.testingapp.main

import android.app.Application
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.testingapp.testing.ui.with
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@SmallTest
@RunWith(AndroidJUnit4::class)
//fixes infinite loop with databinding and roboelectric
@LooperMode(LooperMode.Mode.PAUSED)
@Config(application = Application::class, sdk = [Build.VERSION_CODES.P])
class MainUiTest {

    @Test
    fun welcomeTestCalled() {
        with<MainRobot> {
            verifyTextUtilCalled()
        }
    }

    @Test
    fun testSubTitle() {
        with<MainRobot> {
            val title = "sub"
            postSubTitleText(title)
            verifySubTitleIsDisplayed(title)
        }
    }

    @Test
    fun testTitle() {
        with<MainRobot> {
            val title = "title"
            postTitleText(title)
            verifyTitleIsDisplayed(title)
        }
    }

    @Test
    fun button(){
        with<MainRobot> {
            click()
            verifySecondActivity()
        }
    }
}


