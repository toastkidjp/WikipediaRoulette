/*
 * Copyright (c) 2019 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.wikipediaroulette.ad

import android.content.Context
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

/**
 * @author toastkidjp
 */
class BannerAd(context: Context) {

    private var adView: AdView

    /**
     * Initialize banner AD.
     */
    init {
        MobileAds.initialize(context, APPLICATION_AD_ID)
        adView = AdView(context)
        adView.adSize = AdSize.LARGE_BANNER
        adView.adUnitId = AD_UNIT_ID
    }

    fun addTo(parent: ViewGroup) {
        parent.addView(adView)
    }

    /**
     * Load AD.
     */
    fun loadAd() {
        adView.loadAd(
                AdRequest.Builder()
                        .addTestDevice("B4F1033D07067316E4ED247D9F18E7D7")
                        .build()
        )
    }

    fun destroy() {
        adView.destroy()
    }

    companion object {

        private const val APPLICATION_AD_ID = "ca-app-pub-5751262573448755~9615709377"

        private const val AD_UNIT_ID = "ca-app-pub-5751262573448755/8384735799"

    }
}