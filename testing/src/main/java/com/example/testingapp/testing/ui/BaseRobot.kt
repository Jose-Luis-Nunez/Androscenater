package com.example.testingapp.testing.ui

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat as espressoAssertThat
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.example.testingapp.testing.ui.TextViewMatcher.containsHtmlText


abstract class BaseRobot : BaseRobotSetup() {

    fun clickOnView(@IdRes viewId: Int): ViewInteraction = getView(viewId).perform(click())

    fun clickOnText(@StringRes textId: Int, withScrolling: Boolean = true): ViewInteraction {
        if (withScrolling) {
            scrollToWithText(textId)
        }
        return getViewWithText(textId).perform(click())
    }

    private fun getView(@IdRes viewId: Int): ViewInteraction = onView(withId(viewId))

    private fun getViewWithText(@StringRes textId: Int): ViewInteraction = onView(withText(textId))

    private fun getViewWithText(text: String): ViewInteraction = onView(withText(text))

    fun swipeRight(@IdRes viewId: Int): ViewInteraction? = getView(viewId).perform(swipeRight())

    fun swipeDown(@IdRes viewId: Int): ViewInteraction? = getView(viewId).perform(swipeDown())

    fun swipeUp(@IdRes viewId: Int): ViewInteraction? = getView(viewId).perform(swipeUp())

    fun swipeLeft(@IdRes viewId: Int): ViewInteraction? = getView(viewId).perform(swipeLeft())

    fun viewDisabled(@IdRes viewId: Int): ViewInteraction =
        getView(viewId).check(matches(not(isEnabled())))

    fun isNotVisible(@IdRes viewId: Int): ViewInteraction {
        return getView(viewId).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    fun isDisplayed(@IdRes viewId: Int): ViewInteraction =
        getView(viewId).check(matches(isDisplayed()))

    fun isDisplayedWithText(@IdRes viewId: Int, @StringRes textId: Int): ViewInteraction {
        return getView(viewId).check(matches(isDisplayed())).check(matches(withText(textId)))
    }

    fun isDisplayedWithText(@IdRes viewId: Int, text: String): ViewInteraction {
        return getView(viewId).check(matches(isDisplayed())).check(matches(withText(text)))
    }

    fun isDisplayedWithHint(@IdRes viewId: Int, @StringRes hintId: Int) {
        getView(viewId).check(matches(allOf(isDisplayed(), withHint(hintId))))
    }

    fun clickAndSelectSpinnerItemWithText(@IdRes viewId: Int, @IdRes itemId: Int, text: String) {
        onView(withId(viewId)).perform(click())
        onView(allOf(withId(itemId), withText(text))).perform(click())
        getView(viewId).check(matches(allOf(isDisplayed(), withSpinnerText(containsString(text)))))
    }

    fun isDisplayedWithHtmlText(@IdRes viewId: Int, @StringRes textId: Int): ViewInteraction {
        return getView(viewId).check(matches(isDisplayed()))
            .check(matches(containsHtmlText(textId)))
    }

    fun isCheckedWithText(@StringRes textId: Int) {
        getViewWithText(textId).check(matches(isChecked()))
    }

    fun isChecked(@IdRes viewId: Int) {
        getView(viewId).check(matches(isChecked()))
    }

    fun isNotChecked(@IdRes viewId: Int) {
        getView(viewId).check(matches(isNotChecked()))
    }

    fun isNotDisplayed(@IdRes viewId: Int): ViewInteraction =
        getView(viewId).check(matches(not(isDisplayed())))

    fun isDisabled(@IdRes viewId: Int): ViewInteraction =
        getView(viewId).check(matches(not(isEnabled())))

    fun isEnabled(@IdRes viewId: Int): ViewInteraction =
        getView(viewId).check(matches(isEnabled()))

    fun typeTextInView(@IdRes view: Int, value: String) {
        clickOnView(view).perform(replaceText(value))
    }

    fun verifyViewShowsText(@IdRes view: Int, value: String) {
        getView(view).check(matches(withText(value)))
    }

    fun hasIntentStarted(clazz: Class<*>) {
        Intents.intended(IntentMatchers.hasComponent(clazz.name))
    }

    private fun isKeyboardShown(): Boolean {
        val inputMethodManager =
            getInstrumentation().targetContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.isAcceptingText
    }

    fun isDialogNotDisplayed(@StringRes textId: Int): ViewInteraction =
        onView(withText(textId)).check(doesNotExist())

    fun isDialogDisplayedWithText(@StringRes textId: Int): ViewInteraction {
        return onView(withText(textId)).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    fun clickOnDialogButtonCancel(@StringRes text: Int) {
        onView(withId(android.R.id.button2)).inRoot(isDialog()).check(matches(withText(text)))
            .check(matches(isDisplayed()))
        clickOnView(android.R.id.button2)
    }

    fun clickOnDialogButtonOkay(@StringRes text: Int) {
        onView(withId(android.R.id.button1)).inRoot(isDialog()).check(matches(withText(text)))
            .check(matches(isDisplayed()))
        clickOnView(android.R.id.button1)
    }

    fun clickOnDialogButton(@StringRes text: Int) {
        onView(withText(text)).inRoot(isDialog()).perform(click())
    }

    fun scrollTo(@IdRes view: Int) {
        onView(withId(view)).perform(ScrollToAction())
    }

    private fun scrollToWithText(@StringRes textId: Int) {
        onView(withText(textId)).perform(ScrollToAction())
    }

    /**
     * TODO
     * does not seem to work. have to be revisited
     */
    fun verifyKeyboardShown() {
        // assertThat(isKeyboardShown()).isTrue()
    }

    fun checkTextInsideRecyclerView(recyclerView: Int, position: Int, element: Int, text: String) {
        onView(withId(recyclerView)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                position
            )
        ).check(
            matches(
                withViewAtPosition(
                    position,
                    hasDescendant(allOf(withId(element), withText(text)))
                )
            )
        )
    }

    fun checkRecyclerViewNumberOfEntries(recyclerView: Int, numberOfEntries: Int) {
        onView(withId(recyclerView))
            .check(RecyclerViewItemCountAssertion(numberOfEntries))
    }

    fun clickOnItemInRecyclerView(recyclerView: Int, position: Int) {
        onView(withId(recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click())
        )
    }

    //for matching element of recyclerview
    private fun withViewAtPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView)
            }
        }
    }

    inner class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {
        override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val recyclerView = view as RecyclerView
            val adapter = recyclerView.adapter
            espressoAssertThat(adapter!!.itemCount, `is`(expectedCount))
        }
    }

    fun matchToolbarWithText(text: String?): ViewInteraction {
        return onView(ViewMatchers.isAssignableFrom(Toolbar::class.java))
            .check(matches(withToolbarTitle(Matchers.`is`(text))))
    }

    fun matchWebViewWithUrl(@IdRes webViewId: Int, url: String?): ViewInteraction {
        return onView(withId(webViewId))
            .check(matches(withLoadedUrl(Matchers.`is`(url))))
    }

    private fun withToolbarTitle(textMatcher: Matcher<CharSequence>): Matcher<Any> {
        return object : BoundedMatcher<Any, Toolbar>(Toolbar::class.java) {
            override fun matchesSafely(toolbar: Toolbar): Boolean {
                return textMatcher.matches(toolbar.title)
            }

            override fun describeTo(description: Description) {
                description.appendText("with toolbar title: ")
                textMatcher.describeTo(description)
            }
        }
    }

    private fun withLoadedUrl(textMatcher: Matcher<CharSequence>): Matcher<Any> {
        return object : BoundedMatcher<Any, WebView>(WebView::class.java) {

            override fun matchesSafely(webView: WebView): Boolean {
                return textMatcher.matches(webView.url)
            }

            override fun describeTo(description: Description) {
                description.appendText("WebView with loaded url: ")
                textMatcher.describeTo(description)
            }
        }
    }
}
