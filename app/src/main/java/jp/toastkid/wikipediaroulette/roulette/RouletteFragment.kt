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
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import jp.toastkid.wikipediaroulette.BuildConfig
import jp.toastkid.wikipediaroulette.R
import jp.toastkid.wikipediaroulette.db.DataBase
import jp.toastkid.wikipediaroulette.history.roulette.RouletteHistory
import jp.toastkid.wikipediaroulette.history.view.ViewHistory
import jp.toastkid.wikipediaroulette.libs.CustomTabsIntentFactory
import jp.toastkid.wikipediaroulette.libs.ShareIntentFactory
import kotlinx.android.synthetic.main.fragment_roulette.*
import okio.Okio
import timber.log.Timber
import java.io.InputStream
import java.util.*

/**
 * @author toastkidjp
 */
class RouletteFragment: Fragment() {

    private lateinit var dataBase: DataBase

    private val initialCapacity = 1_000_000

    private val titles: MutableList<String> = ArrayList(initialCapacity)

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val titleStream: InputStream? = activity?.assets?.open("titles.txt")
                ?: activity?.assets?.open("sample.txt")

        titleStream?.let {
            Single.fromCallable { Okio.buffer(Okio.source(titleStream))
                    .use { it.readUtf8().split("\n") } }
                    .subscribeOn(Schedulers.io())
                    .flatMapObservable { array -> Observable.fromIterable(array) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnTerminate({ setNext() })
                    .observeOn(Schedulers.io())
                    .subscribe({ if (titles.size < initialCapacity) titles.add(it) }, Timber::e)
                    .addTo(disposables)
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
            CustomTabsIntentFactory(context)
                    ?.launchUrl(context, UriConverter(context, article_title.text))

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

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

}