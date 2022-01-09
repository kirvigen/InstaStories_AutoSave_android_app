package com.kirvigen.instagram.stories.autosave.activity.instagramAuth

import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient

class InstagramWebClient(private val onAuthSuccessCallback: (String) -> Unit) : WebViewClient() {

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        url?.let {
            view?.loadUrl(it)
        }

        val cookies: String = CookieManager.getInstance().getCookie(url)
        if (url == "https://www.instagram.com/" && cookies != "") {
            onAuthSuccessCallback(cookies)
        }

        return true
    }
}