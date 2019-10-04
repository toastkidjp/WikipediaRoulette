/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.wikipediaroulette.roulette

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.room.Room
import jp.toastkid.wikipediaroulette.BuildConfig
import jp.toastkid.wikipediaroulette.R
import jp.toastkid.wikipediaroulette.api.WikipediaApi
import jp.toastkid.wikipediaroulette.db.DataBase
import jp.toastkid.wikipediaroulette.history.view.ViewHistory
import jp.toastkid.wikipediaroulette.libs.CustomTabsIntentFactory
import jp.toastkid.wikipediaroulette.libs.ShareIntentFactory
import kotlinx.android.synthetic.main.fragment_roulette.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

/**
 * @author toastkidjp
 */
class RouletteFragment: Fragment() {

    private lateinit var dataBase: DataBase

    private val wikipediaApi = WikipediaApi()

    private val titles: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            GlobalScope.async {
                return@async wikipediaApi.invoke()?.filter { it.ns == 0 }?.map { it.title }
            }
                    .await()
                    ?.forEach { titles.add(it) }
            setNext()
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
        return inflater.inflate(LAYOUT_ID, null)
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
        if (titles.isEmpty()) {
            return
        }
        val nextArticleName = titles.get((titles.size * Math.random()).toInt())
        article_title.text = nextArticleName
    }

    private fun setUpActions() {
        article_title.setOnClickListener { setNext() }

        show_page.setOnClickListener {
            val uri = UriConverter(article_title.text)
            CustomTabsIntentFactory(context)?.launchUrl(context, uri)

            val viewHistory = ViewHistory().also {
                it.articleName = article_title.text.toString()
                it.lastDisplayed = System.currentTimeMillis()
                it.locale = uri.host?.let { host -> host.substring(0, host.indexOf(".")) }
                        ?: Locale.getDefault().language
            }
            GlobalScope.launch { dataBase.viewHistoryAccessor().insert(viewHistory) }
        }

        share.setOnClickListener {
            val intent = ShareIntentFactory(
                    "${article_title.text} - Wikipedia${System.getProperty("line.separator")}"
                            + "${UriConverter(article_title.text)}"
            )
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        @LayoutRes
        private const val LAYOUT_ID = R.layout.fragment_roulette
    }
}