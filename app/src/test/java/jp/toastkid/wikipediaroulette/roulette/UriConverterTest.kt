package jp.toastkid.wikipediaroulette.roulette

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/**
 * @author toastkidjp
 */
@RunWith(RobolectricTestRunner::class)
internal class UriConverterTest {

    @Test
    fun test() {
        val uri = UriConverter(RuntimeEnvironment.application, "トマト")
        assertEquals("https://ja.wikipedia.org/wiki/トマト", uri.toString())
    }
}