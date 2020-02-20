package com.example.testingapp.testing.ui

import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.Matcher

/**
 * ScrollTo action to fix an issue with the default scrollTo action of espresso that checks for
 * the layout class which has to be a [ScrollView] or [HorizontalScrollView].
 *
 * see: https://stackoverflow.com/questions/35272953/espresso-scrolling-not-working-when-nestedscrollview-or-recyclerview-is-in-coor
 */
class ScrollToAction(
    private val original: androidx.test.espresso.action.ScrollToAction = androidx.test.espresso.action.ScrollToAction()
) : ViewAction by original {

    override fun getConstraints(): Matcher<View> = anyOf(
        allOf(
            withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
            isDescendantOfA(isAssignableFrom(NestedScrollView::class.java))
        ),
        original.constraints
    )
}
