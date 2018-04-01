package jp.toastkid.wikipediaroulette.libs

import android.content.Intent
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Test of [ShareIntentFactory].
 *
 * @author toastkidjp
 */
@RunWith(RobolectricTestRunner::class)
class ShareIntentFactoryTest {

    /**
     * Check generated [Intent].
     */
    @Test
    operator fun invoke() {
        val intent: Intent = ShareIntentFactory("tomato")
        assertEquals(Intent.ACTION_SEND, intent.action)
        assertEquals("text/plain", intent.type)
        assertEquals("tomato", intent.getStringExtra(Intent.EXTRA_TEXT))
    }

}