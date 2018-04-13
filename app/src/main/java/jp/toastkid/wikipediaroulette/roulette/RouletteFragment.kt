/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.wikipediaroulette.roulette

import android.annotation.SuppressLint
import android.arch.persistence.room.Room
import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import jp.toastkid.wikipediaroulette.BuildConfig
import jp.toastkid.wikipediaroulette.R
import jp.toastkid.wikipediaroulette.db.DataBase
import jp.toastkid.wikipediaroulette.history.roulette.RouletteHistory
import jp.toastkid.wikipediaroulette.history.view.ViewHistory
import jp.toastkid.wikipediaroulette.libs.ShareIntentFactory
import kotlinx.android.synthetic.main.fragment_roulette.*
import okio.Okio
import java.io.InputStream
import java.util.*

/**
 * @author toastkidjp
 */
class RouletteFragment: Fragment() {

    private lateinit var dataBase: DataBase

    private val titles: List<String> by lazy {
        val titleStream: InputStream = activity?.assets?.open("titles.txt")
                ?: return@lazy Collections.emptyList<String>()
        Okio.buffer(Okio.source(titleStream))
                .use { return@lazy it.readUtf8().split("\n").toList() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val applicationContext: Context = context?.applicationContext ?: return
        dataBase = Room.databaseBuilder(
                applicationContext,
                DataBase::class.java,
                BuildConfig.APPLICATION_ID
        ).build()
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_roulette, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpActions()
        setNext()
    }

    private fun setNext() {
        val nextArticleName = titles.get((titles.size * Math.random()).toInt())
        article_title.text = nextArticleName
        val rouletteHistory = RouletteHistory().apply {
            articleName = nextArticleName
            lastDisplayed = System.currentTimeMillis()
        }
        Completable.fromAction { dataBase.rouletteHistoryAccessor().insert(rouletteHistory) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }


    private fun setUpActions() {
        article_title.setOnClickListener { setNext() }

        show_page.setOnClickListener {
            makeCustomTabsIntent()?.launchUrl(context, makeUri())

            val viewHistory = ViewHistory().also {
                it.articleName = article_title.text.toString()
                it.lastDisplayed = System.currentTimeMillis()
            }
            Completable.fromAction { dataBase.viewHistoryAccessor().insert(viewHistory) }
                    .subscribeOn(Schedulers.io())
                    .subscribe()
        }

        share.setOnClickListener {
            val intent = ShareIntentFactory(
                    "${article_title.text} - Wikipedia${System.getProperty("line.separator")}${makeUri()}"
            )
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    private fun makeCustomTabsIntent(): CustomTabsIntent? =
            context?.let {
                CustomTabsIntent.Builder()
                        .setStartAnimations(it, android.R.anim.fade_in, android.R.anim.fade_out)
                        .setExitAnimations(it, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .addDefaultShareMenuItem()
                        .build()
            }


    private fun makeUri(): Uri =
            (getString(R.string.base_url_wikipedia_article) + article_title.text).toUri()

}