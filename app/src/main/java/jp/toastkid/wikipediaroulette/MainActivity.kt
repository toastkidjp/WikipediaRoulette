package jp.toastkid.wikipediaroulette

import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v7.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        roulette.setOnClickListener { setNext() }
        show_page.setOnClickListener { makeCustomTabsIntent().launchUrl(this, makeUrl()) }
        setNext()
    }

    private fun setNext() {
        roulette.text = titles.get((titles.size * Math.random()).toInt())
    }

    private fun makeCustomTabsIntent(): CustomTabsIntent =
            CustomTabsIntent.Builder()
                    .setStartAnimations(this, android.R.anim.fade_in, android.R.anim.fade_out)
                    .setExitAnimations(this, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .addDefaultShareMenuItem()
                    .build()

    private fun makeUrl() = Uri.parse("https://ja.wikipedia.org/wiki/" + roulette.text)


}
