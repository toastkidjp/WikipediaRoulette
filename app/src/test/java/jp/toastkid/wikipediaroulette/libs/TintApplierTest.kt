package jp.toastkid.wikipediaroulette.libs

import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import com.sys1yagi.kmockito.mock
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * It's mere smoke test.
 *
 * @author toastkidjp
 */
@RunWith(RobolectricTestRunner::class)
class TintApplierTest {

    @Test
    fun testInvoke() {
        val drawable: ShapeDrawable = mock()
        TintApplier.invoke(drawable, Color.BLACK)
    }
}