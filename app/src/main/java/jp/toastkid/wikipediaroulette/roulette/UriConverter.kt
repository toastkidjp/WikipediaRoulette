/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.wikipediaroulette.roulette

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import jp.toastkid.wikipediaroulette.wikipedia.HostGenerator
import java.util.*

/**
 * @author toastkidjp
 */
object UriConverter {

    private val hostGenerator = HostGenerator()

    operator fun invoke(context: Context?, title: CharSequence?): Uri =
            "${hostGenerator.invoke(Locale.getDefault())}wiki/$title".toUri()

}