package jp.toastkid.wikipediaroulette.libs

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * @author toastkidjp
 */
class DateConverterTest {

    /**
     * For environment whose default locale is not JA.
     */
    @Test
    fun test() {
        val dateTimeText = DateConverter(1523757192178L)
        assertTrue(dateTimeText.startsWith("2018/04/15"))
        assertTrue(dateTimeText.endsWith(" 10:53:12"))
    }
}