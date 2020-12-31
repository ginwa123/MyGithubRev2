package com.m.ginwa.mygithubrev2

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedDiagnosingMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import com.m.ginwa.core.utils.EspressoIdlingResources
import com.m.ginwa.mygithubrev2.ui.MainActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
class ExampleInstrumentedTest {


    private lateinit var device: UiDevice

    @Rule
    @JvmField
    var mActivityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        getInstrumentation().targetContext.deleteDatabase("github.db")
        IdlingRegistry.getInstance().register(EspressoIdlingResources.getEspressoIdlingResource())
        device = UiDevice.getInstance(getInstrumentation())
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResources.getEspressoIdlingResource())
        getInstrumentation().targetContext.deleteDatabase("github.db")
    }

    @Test
    fun test1ClickDetail() {
        val appCompatImageView = onView(
            Matchers.allOf(
                withId(R.id.search_button),
                withContentDescription("Search"),
                isDisplayed()
            )
        )
        appCompatImageView.perform(ViewActions.click())

        val searchAutoComplete = onView(
            Matchers.allOf(
                withId(R.id.search_src_text),
                isDisplayed()
            )
        )
        searchAutoComplete.perform(
            ViewActions.replaceText("ginwa123"),
            ViewActions.closeSoftKeyboard()
        )

        val searchAutoComplete2 = onView(
            Matchers.allOf(
                withId(R.id.search_src_text), withText("ginwa123"),
                isDisplayed()
            )
        )
        searchAutoComplete2.perform(ViewActions.pressImeActionButton())

        val recyclerView = onView(
            Matchers.allOf(
                withId(R.id.recyclerView),
                childAtPosition(
                    withId(R.id.swipeRefreshLayout),
                    0
                )
            )
        )
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        checkDetail()


    }


    @Test
    fun test2ClickFavorite() {
        val appCompatImageView = onView(
            Matchers.allOf(
                withId(R.id.search_button),
                withContentDescription("Search"),
                isDisplayed()
            )
        )
        appCompatImageView.perform(ViewActions.click())

        // search  ginwa123
        val searchAutoComplete = onView(
            Matchers.allOf(
                withId(R.id.search_src_text),
                isDisplayed()
            )
        )
        searchAutoComplete.perform(
            ViewActions.replaceText("ginwa123"),
            ViewActions.closeSoftKeyboard()
        )

        val searchAutoComplete2 = onView(
            Matchers.allOf(
                withId(R.id.search_src_text),
                withText("ginwa123"),
                isDisplayed()
            )
        )
        searchAutoComplete2.perform(ViewActions.pressImeActionButton())

        val recyclerView = onView(
            Matchers.allOf(
                withId(R.id.recyclerView),
                childAtPosition(
                    withId(R.id.swipeRefreshLayout),
                    0
                )
            )
        )
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        checkDetail()
        val floatingActionButton = onView(
            Matchers.allOf(
                withId(R.id.bt_favorite),
                withContentDescription("Button favorite"),
                isDisplayed()
            )
        )
        floatingActionButton.perform(ViewActions.click())

        device.pressBack()


        // search ginwa
        appCompatImageView.perform(ViewActions.click())
        searchAutoComplete.perform(
            ViewActions.replaceText("ginwa"),
            ViewActions.closeSoftKeyboard()
        )

        val searchAutoComplete3 = onView(
            Matchers.allOf(
                withId(R.id.search_src_text),
                withText("ginwa"),
                isDisplayed()
            )
        )
        searchAutoComplete3.perform(ViewActions.pressImeActionButton())
        recyclerView.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        floatingActionButton.perform(ViewActions.click())
        device.pressBack()

        val actionMenuItemView = onView(
            Matchers.allOf(
                withId(R.id.action_searchFragment_to_favoriteFragment),
                withContentDescription("Favorites"),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(ViewActions.click())
        val recyclerView2 = onView(
            Matchers.allOf(
                withResourceName("recyclerView")
            )
        )
        recyclerView2.check(
            matches(
                atPosition(
                    0,
                    hasDescendant(withText("ginwa"))
                )
            )
        )

    }

    @Test
    fun test3DeleteFavorites() {
        val actionMenuItemView = onView(
            Matchers.allOf(
                withId(R.id.action_searchFragment_to_favoriteFragment),
                withContentDescription("Favorites"),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(ViewActions.click())
        val recyclerView2 = onView(
            Matchers.allOf(
                withResourceName("recyclerView")
            )
        )
        recyclerView2.check(
            matches(
                atPosition(
                    0,
                    hasDescendant(withText("ginwa"))
                )
            )
        )
        recyclerView2.check(matches(hasChildCount(2)))
        recyclerView2.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        val floatingActionButton = onView(
            Matchers.allOf(
                withResourceName("bt_favorite"),
                withContentDescription("Button favorite"),
                isDisplayed()
            )
        )
        floatingActionButton.perform(ViewActions.click())
        device.pressBack()
        recyclerView2.check(matches(hasChildCount(1)))
    }


    private fun checkDetail() {
        val name = getText(onView(withId(R.id.tv_name)))
        val followers = getText(onView(withId(R.id.tv_followers)))
        val followings = getText(onView(withId(R.id.tv_followings)))
        assertEquals(true, name.isNotEmpty())
        assertEquals(true, followers.isNotEmpty())
        assertEquals(true, followings.isNotEmpty())
    }

    private fun getText(matcher: ViewInteraction): String {
        var text = String()
        matcher.perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(TextView::class.java)
            }

            override fun getDescription(): String {
                return "Text of the view"
            }

            override fun perform(uiController: UiController, view: View) {
                val tv = view as TextView
                text = tv.text.toString()
            }
        })

        return text
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    private fun atPosition(position: Int, @NonNull itemMatcher: Matcher<View?>): Matcher<View?> {
        return object : BoundedDiagnosingMatcher<View?, RecyclerView>(RecyclerView::class.java) {


            override fun matchesSafely(
                item: RecyclerView?,
                mismatchDescription: Description?
            ): Boolean {
                val viewHolder = item?.findViewHolderForAdapterPosition(position)
                    ?: // has no item on such position
                    return false
                return itemMatcher.matches(viewHolder.itemView)
            }

            override fun describeMoreTo(description: Description?) {
                description?.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }
        }
    }
}