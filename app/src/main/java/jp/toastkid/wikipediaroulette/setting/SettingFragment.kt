/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.wikipediaroulette.setting

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import jp.toastkid.wikipediaroulette.BuildConfig
import jp.toastkid.wikipediaroulette.R
import jp.toastkid.wikipediaroulette.libs.CustomTabsIntentFactory
import kotlinx.android.synthetic.main.fragment_setting.*

/**
 * Setting fragment.
 *
 * @author toastkidjp
 */
class SettingFragment: Fragment() {

    @SuppressLint("InflateParams")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(LAYOUT_ID, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settings_app_version.text = BuildConfig.VERSION_NAME

        licenses.setOnClickListener {
            context?.also { activityContext ->
                val title = activityContext.getString(R.string.title_licenses)
                startActivity(
                        Intent(activityContext, OssLicensesMenuActivity::class.java)
                                .also { it.putExtra("title", title) }
                )
            }
        }

        settings_privacy_policy.setOnClickListener { showUrl("https://tmblr.co/ZDG7Be2NVdctY") }

        version.setOnClickListener { showUrl("market://details?id=" + BuildConfig.APPLICATION_ID) }

        copyright.setOnClickListener { showUrl("market://search?q=pub:toastkidjp") }
    }

    private fun showUrl(url: String) {
        val activityContext: Context? = context
        CustomTabsIntentFactory.invoke(activityContext)
                ?.launchUrl(activityContext, url.toUri())
    }

    companion object {

        @LayoutRes
        private const val LAYOUT_ID = R.layout.fragment_setting
    }
}