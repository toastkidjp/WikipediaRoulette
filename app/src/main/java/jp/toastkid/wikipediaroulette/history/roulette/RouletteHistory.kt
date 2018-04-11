/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.wikipediaroulette.history.roulette

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * @author toastkidjp
 */
@Entity
class RouletteHistory {

    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0

    var articleName: String = ""

    var lastDisplayed: Long = 0
}