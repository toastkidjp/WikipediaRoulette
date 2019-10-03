package jp.toastkid.wikipediaroulette.wikipedia

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * @author toastkidjp
 */
class HostGeneratorTest {

    @Test
    fun test() {
        assertEquals("https://ja.wikipedia.org/", HostGenerator().invoke(Locale.JAPAN.language))
    }
}