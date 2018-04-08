package jp.toastkid.wikipediaroulette

import android.arch.persistence.room.Room
import android.content.ActivityNotFoundException
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.customtabs.CustomTabsIntent
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import androidx.core.net.toUri
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import jp.toastkid.wikipediaroulette.db.DataBase
import jp.toastkid.wikipediaroulette.history.roulette.RouletteHistory
import jp.toastkid.wikipediaroulette.history.view.ViewHistory
import jp.toastkid.wikipediaroulette.libs.ShareIntentFactory
import kotlinx.android.synthetic.main.activity_main.*
import okio.Okio


/**
 * Main activity.
 *
 * @author toastkidjp
 */
class MainActivity : AppCompatActivity() {

    private val titles by lazy {
        Okio.buffer(Okio.source(assets.open("titles.txt")))
                .use { it.readUtf8().split("\n").toList() }
    }

    private lateinit var dataBase: DataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpActions()

        initToolbar(toolbar)

        dataBase = Room.databaseBuilder(
                applicationContext,
                DataBase::class.java,
                BuildConfig.APPLICATION_ID
        ).build()

        setNext()
    }

    private fun initToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        toolbar.run {
            setNavigationIcon(R.drawable.ic_close)
            setNavigationOnClickListener { finish() }
            setTitleTextColor(Color.WHITE)
        }
        applyTint(toolbar.overflowIcon, Color.WHITE)
    }

    /**
     * Apply tint to passed drawable.
     *
     * @param icon Drawable
     * @param fontColor color int
     */
    private fun applyTint(icon: Drawable?, @ColorInt fontColor: Int) =
            icon?.let { DrawableCompat.setTint(it, fontColor) }

    private fun setUpActions() {
        roulette.setOnClickListener { setNext() }

        show_page.setOnClickListener {
            makeCustomTabsIntent().launchUrl(this, makeUri())

            val viewHistory = ViewHistory().also {
                it.articleName = roulette.text.toString()
                it.lastDisplayed = System.currentTimeMillis()
            }
            Completable.fromAction { dataBase.viewHistoryAccessor().insert(viewHistory) }
                    .subscribeOn(Schedulers.io())
                    .subscribe()
        }

        share.setOnClickListener {
            val intent = ShareIntentFactory(
                    "${roulette.text} - Wikipedia${System.getProperty("line.separator")}${makeUri()}"
            )
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    private fun setNext() {
        val nextArticleName = titles.get((titles.size * Math.random()).toInt())
        roulette.text = nextArticleName
        val rouletteHistory = RouletteHistory().apply {
            articleName = nextArticleName
            lastDisplayed = System.currentTimeMillis()
        }
        Completable.fromAction { dataBase.rouletteHistoryAccessor().insert(rouletteHistory) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    private fun makeCustomTabsIntent(): CustomTabsIntent =
            CustomTabsIntent.Builder()
                    .setStartAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
                    .setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .addDefaultShareMenuItem()
                    .build()

    private fun makeUri(): Uri =
            (getString(R.string.base_url_wikipedia_article) + roulette.text).toUri()

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
            when (item?.itemId) {
                R.id.menu_settings -> {
                    // TODO implements
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
