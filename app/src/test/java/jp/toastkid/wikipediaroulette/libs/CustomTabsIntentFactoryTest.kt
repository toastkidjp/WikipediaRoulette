package jp.toastkid.wikipediaroulette.libs

import android.content.Intent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/**
 * @author toastkidjp
 */
@RunWith(RobolectricTestRunner::class)
class CustomTabsIntentFactoryTest {

    @Test
    fun test() {
        val customTabsIntent = CustomTabsIntentFactory.invoke(RuntimeEnvironment.application)
        customTabsIntent?.intent?.also {
            assertEquals(Intent.ACTION_VIEW, it.action)
            assertNull(it.extras.get("android.support.customtabs.extra.SESSION"))
        }
        customTabsIntent?.startAnimationBundle?.also {
            assertEquals("jp.toastkid.wikipediaroulette", it.get("android:activity.packageName"))
        }
    }
}