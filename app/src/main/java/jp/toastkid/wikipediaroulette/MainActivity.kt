package jp.toastkid.wikipediaroulette

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import jp.toastkid.wikipediaroulette.history.view.ViewHistoryFragment
import jp.toastkid.wikipediaroulette.libs.TintApplier
import jp.toastkid.wikipediaroulette.roulette.RouletteFragment
import jp.toastkid.wikipediaroulette.setting.SettingFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Main activity.
 *
 * @author toastkidjp
 */
class MainActivity : AppCompatActivity() {

    private val rouletteFragment: Fragment = RouletteFragment()

    private val viewHistoryFragment by lazy { ViewHistoryFragment() }

    private val settingFragment by lazy { SettingFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar(toolbar)

        replaceFragment(rouletteFragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager?.beginTransaction()?.also {
            it.setCustomAnimations(R.anim.slide_in_right, 0, 0, android.R.anim.slide_out_right)
            it.replace(R.id.container, fragment)
            it.addToBackStack("${fragment.hashCode()}")
            it.commitAllowingStateLoss()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager?.fragments?.size == 0) {
            finish()
        }
    }

    private fun initToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        toolbar.run {
            setNavigationIcon(R.drawable.ic_close)
            setNavigationOnClickListener { finish() }
            setTitleTextColor(Color.WHITE)
        }
        TintApplier(toolbar.overflowIcon, Color.WHITE)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
            when (item?.itemId) {
                R.id.menu_view_history -> {
                    replaceFragment(viewHistoryFragment)
                    true
                }
                R.id.menu_settings -> {
                    replaceFragment(settingFragment)
                    true
                }
                R.id.menu_about_app -> {
                    // TODO implements
                    true
                }
                R.id.menu_exit -> {
                    finish()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

}
