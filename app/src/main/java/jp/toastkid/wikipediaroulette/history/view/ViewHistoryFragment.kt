/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.wikipediaroulette.history.view

import android.annotation.SuppressLint
import android.arch.persistence.room.Room
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import jp.toastkid.wikipediaroulette.BuildConfig
import jp.toastkid.wikipediaroulette.R
import jp.toastkid.wikipediaroulette.db.DataBase
import jp.toastkid.wikipediaroulette.libs.RecyclerViewScroller
import kotlinx.android.synthetic.main.fragment_roulette_history.*
import timber.log.Timber

/**
 * @author toastkidjp
 */
class ViewHistoryFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_roulette_history, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (context?.applicationContext == null) {
            Timber.e(NullPointerException("context is null"))
            return
        }

        val dataBase = Room.databaseBuilder(
                context?.applicationContext as Context,
                DataBase::class.java,
                BuildConfig.APPLICATION_ID
        ).build()
        items.adapter = Adapter(context as Context, dataBase.viewHistoryAccessor())
        items.layoutManager = LinearLayoutManager(context)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.fragment_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        items.layoutManager = LinearLayoutManager(context)
        return when (item?.itemId) {
            R.id.to_top -> {
                RecyclerViewScroller.toTop(items)
                return true
            }
            R.id.to_bottom -> {
                RecyclerViewScroller.toBottom(items)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}