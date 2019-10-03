/*
 * Copyright (c) 2019 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.wikipediaroulette.api

import android.support.annotation.WorkerThread
import jp.toastkid.wikipediaroulette.api.model.Article
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * @author toastkidjp
 */
class WikipediaApi {

    @WorkerThread
    fun invoke(): Array<Article>? {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://ja.wikipedia.org/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        val service = retrofit.create(WikipediaService::class.java)
        val call = service.call()
        return call.execute().body()?.query?.random
    }
}