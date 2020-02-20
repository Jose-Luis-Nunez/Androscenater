package com.example.testingapp.testing.ui

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object TextViewMatcher {
    fun containsHtmlText(@StringRes textId: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>(View::class.java) {

            public override fun matchesSafely(actualObject: View): Boolean {
                if (actualObject !is TextView) {
                    return false
                }

                val resources = actualObject.getContext().resources
                val expectedSequence = HtmlCompat.fromHtml(
                    resources.getString(textId),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                return actualObject.text.contains(expectedSequence)
            }

            override fun describeTo(description: Description) {
                description.appendText("char sequence doesn't match")
            }
        }
    }
}
