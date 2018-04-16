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
import jp.toastkid.wikipediaroulette.R

/**
 * @author toastkidjp
 */
object UriConverter {

    operator fun invoke(context: Context?, title: CharSequence?): Uri =
            (context?.getString(R.string.base_url_wikipedia_article) + title).toUri()

}