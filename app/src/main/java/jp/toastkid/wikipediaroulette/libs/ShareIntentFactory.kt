/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.wikipediaroulette.libs

import android.content.Intent

/**
 * Make plain text sharing [Intent].
 *
 * @author toastkidjp
 */
object ShareIntentFactory {

    operator fun invoke(message: String): Intent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
}