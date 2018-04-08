/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.wikipediaroulette.history.roulette

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
 * @author toastkidjp
 */
@Dao
interface RouletteHistoryDataAccessor {

    @Query("SELECT * FROM roulettehistory")
    fun getAll(): List<RouletteHistory>

    @Insert
    fun insert(entity: RouletteHistory)

    @Insert
    fun insertAll(vararg entities: RouletteHistory)

    @Query("DELETE FROM roulettehistory")
    fun deleteAll()
}