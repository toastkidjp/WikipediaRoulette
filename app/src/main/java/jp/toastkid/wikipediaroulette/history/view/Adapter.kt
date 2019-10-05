/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.wikipediaroulette.history.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import jp.toastkid.wikipediaroulette.R
import jp.toastkid.wikipediaroulette.libs.CustomTabsIntentFactory
import jp.toastkid.wikipediaroulette.libs.DateConverter
import jp.toastkid.wikipediaroulette.roulette.UriConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * View history's item adapter.
 *
 * @author toastkidjp
 */
class Adapter(
        private val context: Context,
        private val viewHistoryDataAccessor: ViewHistoryDataAccessor
): RecyclerView.Adapter<ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private val uriConverter = UriConverter()

    private val items: MutableList<ViewHistory> = mutableListOf<ViewHistory>()
            .also {
                GlobalScope.launch (Dispatchers.Main) {
                    withContext(Dispatchers.IO) { it.addAll(viewHistoryDataAccessor.getAll()) }
                    notifyDataSetChanged()
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(inflater.inflate(R.layout.item_roulette_history, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: ViewHistory = items.get(position)

        holder.also {
            it.setTitle(item.articleName)
            it.setTime(DateConverter(item.lastDisplayed))
            it.setTapAction { title ->
                CustomTabsIntentFactory(context)
                    ?.launchUrl(context, uriConverter(title, item.locale))
            }
            it.setTapDelete {
                GlobalScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO) {
                        viewHistoryDataAccessor.delete(item)
                        items.remove(item)
                    }
                    notifyItemRemoved(position)
                    Snackbar.make(it.itemView, "Delete ${item.articleName}.", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }


}