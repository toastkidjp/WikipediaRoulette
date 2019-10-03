package jp.toastkid.wikipediaroulette.roulette

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.*

/**
 * @author toastkidjp
 */
@RunWith(RobolectricTestRunner::class)
internal class UriConverterTest {

    @Test
    fun test() {
        assertEquals(
                "https://${Locale.getDefault().language}.wikipedia.org/wiki/トマト",
                UriConverter(RuntimeEnvironment.application, "トマト").toString()
        )
    }
}