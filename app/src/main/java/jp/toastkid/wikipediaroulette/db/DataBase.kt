/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.wikipediaroulette.db

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.toastkid.wikipediaroulette.history.view.ViewHistory
import jp.toastkid.wikipediaroulette.history.view.ViewHistoryDataAccessor

/**
 * @author toastkidjp
 */
@Database(entities = [ViewHistory::class], version = 1)
abstract class DataBase: RoomDatabase() {

    abstract fun viewHistoryAccessor(): ViewHistoryDataAccessor
}