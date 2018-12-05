/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.wikipediaroulette.history.roulette

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import jp.toastkid.wikipediaroulette.R
import jp.toastkid.wikipediaroulette.libs.CustomTabsIntentFactory
import jp.toastkid.wikipediaroulette.libs.DateConverter
import jp.toastkid.wikipediaroulette.roulette.UriConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author toastkidjp
 */
class Adapter(
        private val context: Context,
        private val rouletteHistoryAccessor: RouletteHistoryDataAccessor
): RecyclerView.Adapter<ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private val items: List<RouletteHistory> = mutableListOf<RouletteHistory>()
            .also {
                GlobalScope.launch {
                    it.addAll(rouletteHistoryAccessor.getAll())
                    GlobalScope.launch (Dispatchers.Main) { notifyDataSetChanged() }
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(inflater.inflate(R.layout.item_roulette_history, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rouletteHistory: RouletteHistory = items.get(position)

        holder.also {
            it.setTitle(rouletteHistory.articleName)
            it.setTime(DateConverter(rouletteHistory.lastDisplayed))
            it.setTapAction { CustomTabsIntentFactory(context)
                    ?.launchUrl(context, UriConverter(context, it)) }
        }
    }


}