/*
 * Copyright (c) 2019 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.wikipediaroulette.api

import jp.toastkid.wikipediaroulette.api.model.Response
import retrofit2.Call
import retrofit2.http.GET

/**
 * @author toastkidjp
 */
interface WikipediaService {

    @GET("w/api.php?action=query&list=random&rnlimit=50&format=json")
    fun call(): Call<Response>
}