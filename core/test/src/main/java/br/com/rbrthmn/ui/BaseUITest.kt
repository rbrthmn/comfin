
package br.com.rbrthmn.ui

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class BaseUITest {
    @get:Rule
    abstract val composeTestRule: ComposeContentTestRule
    @Before
    abstract fun setup()
}