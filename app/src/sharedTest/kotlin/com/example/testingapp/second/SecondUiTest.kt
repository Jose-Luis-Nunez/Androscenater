package com.example.testingapp.second

import android.app.Application
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.testingapp.main.MainRobot
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
class SecondUiTest {

    @Test
    fun testSubTitle() {
        with<SecondRobot> {
            val title = "sub"
            postText(title)
            verifyTextIsDisplayed(title)
        }
    }
}


