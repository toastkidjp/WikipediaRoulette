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
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.toastkid.wikipediaroulette.BuildConfig
import jp.toastkid.wikipediaroulette.R
import jp.toastkid.wikipediaroulette.db.DataBase
import jp.toastkid.wikipediaroulette.history.roulette.RouletteHistory
import jp.toastkid.wikipediaroulette.history.view.ViewHistory
import jp.toastkid.wikipediaroulette.libs.CustomTabsIntentFactory
import jp.toastkid.wikipediaroulette.libs.ShareIntentFactory
import kotlinx.android.synthetic.main.fragment_roulette.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import okio.Okio
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.*

/**
 * @author toastkidjp
 */
class RouletteFragment: Fragment() {

    private lateinit var dataBase: DataBase

    private val initialCapacity = 1_000_000

    private val titles: MutableList<String>? = ArrayList(initialCapacity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val titleStream: InputStream? = try {
            activity?.assets?.open("titles.txt")
        } catch (e: FileNotFoundException) {
            activity?.assets?.open("sample.txt")
        }

        titleStream?.let {
            launch (UI) {
                async {
                    Okio.buffer(Okio.source(titleStream))
                            .use { it.readUtf8().split("\n") }
                            .forEach {
                                launch (UI) {
                                    if (titles?.size ?: 0 < initialCapacity) {
                                        titles?.add(it)
                                    }
                                }
                            }
                }.await()
                setNext()
            }
        }

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
    }

    override fun onResume() {
        super.onResume()
        setNext()
    }

    private fun setNext() {
        if (titles?.size == 0) {
            return
        }
        val nextArticleName = titles?.get((titles.size * Math.random()).toInt()) ?: return
        article_title.text = nextArticleName
        val rouletteHistory = RouletteHistory().apply {
            articleName = nextArticleName
            lastDisplayed = System.currentTimeMillis()
        }
        launch { dataBase.rouletteHistoryAccessor().insert(rouletteHistory) }
    }

    private fun setUpActions() {
        article_title.setOnClickListener { setNext() }

        show_page.setOnClickListener {
            CustomTabsIntentFactory(context)
                    ?.launchUrl(context, UriConverter(context, article_title.text))

            val viewHistory = ViewHistory().also {
                it.articleName = article_title.text.toString()
                it.lastDisplayed = System.currentTimeMillis()
            }
            launch { dataBase.viewHistoryAccessor().insert(viewHistory) }
        }

        share.setOnClickListener {
            val intent = ShareIntentFactory(
                    "${article_title.text} - Wikipedia${System.getProperty("line.separator")}"
                            + "${UriConverter(context, article_title.text)}"
            )
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
    }

}