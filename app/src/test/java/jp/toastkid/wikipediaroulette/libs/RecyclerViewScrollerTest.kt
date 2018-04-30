package jp.toastkid.wikipediaroulette.libs

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import com.sys1yagi.kmockito.any
import com.sys1yagi.kmockito.invoked
import com.sys1yagi.kmockito.mock
import com.sys1yagi.kmockito.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.internal.verification.VerificationModeFactory.times
import org.robolectric.RobolectricTestRunner

/**
 * @author toastkidjp
 */
@RunWith(RobolectricTestRunner::class)
class RecyclerViewScrollerTest {

    @Test
    fun toTop() {
        val recyclerView: RecyclerView = mock()
        val adapter: Adapter<RecyclerView.ViewHolder> = mock()
        recyclerView.adapter.invoked().thenReturn(adapter)
        recyclerView.adapter.itemCount.invoked().thenReturn(100)
        RecyclerViewScroller.toTop(recyclerView)
        recyclerView.verify(times(1)).scrollToPosition(0)

        recyclerView.adapter.itemCount.invoked().thenReturn(29)
        RecyclerViewScroller.toTop(recyclerView)
        recyclerView.verify(times(1)).post(any())
    }

    @Test
    fun toBottom() {
        val recyclerView: RecyclerView = mock()
        val adapter: Adapter<RecyclerView.ViewHolder> = mock()
        recyclerView.adapter.invoked().thenReturn(adapter)
        recyclerView.adapter.itemCount.invoked().thenReturn(100)
        RecyclerViewScroller.toBottom(recyclerView)
        recyclerView.verify(times(1)).scrollToPosition(99)

        recyclerView.adapter.itemCount.invoked().thenReturn(29)
        RecyclerViewScroller.toBottom(recyclerView)
        recyclerView.verify(times(1)).post(any())
    }
}