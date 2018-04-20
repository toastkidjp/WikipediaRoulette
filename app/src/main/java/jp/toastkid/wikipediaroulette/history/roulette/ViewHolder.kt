/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.wikipediaroulette.history.roulette

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import jp.toastkid.wikipediaroulette.R

/**
 * @author toastkidjp
 */
class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val title: TextView = itemView.findViewById(R.id.title)

    private val time: TextView = itemView.findViewById(R.id.time)

    fun setTitle(titleText: String) {
        title.text = titleText
    }

    fun setTime(timeText: String) {
        time.text = timeText
    }

    fun setTapAction(action: (String) -> Unit) {
        itemView.setOnClickListener{ action(title.text.toString()) }
    }
}