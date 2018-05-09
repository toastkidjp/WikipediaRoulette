/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.wikipediaroulette.libs

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Date converter.
 *
 * @author toastkidjp
 */
object DateConverter {

    private val DATE_FORMAT_HOLDER: ThreadLocal<DateFormat> = object: ThreadLocal<DateFormat>() {
        override fun initialValue(): DateFormat =
                SimpleDateFormat("yyyy/MM/dd(E) HH:mm:ss", Locale.getDefault())
    }

    operator fun invoke(ms: Long): String =
            DATE_FORMAT_HOLDER.get().format(Date().also { it.time = ms })
}