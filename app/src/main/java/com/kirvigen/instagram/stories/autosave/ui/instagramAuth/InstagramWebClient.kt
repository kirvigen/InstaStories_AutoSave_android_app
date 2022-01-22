package com.kirvigen.instagram.stories.autosave.ui.instagramAuth

import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepositoryImpl.Companion.INSTAGRAM_MAIN_URL
import com.kirvigen.instagram.stories.autosave.user.SessionInteractor
import org.koin.java.KoinJavaComponent.inject

class InstagramWebClient(private val onAuthSuccessCallback: (String) -> Unit) : WebViewClient() {

    private val sessionInteractor by inject(SessionInteractor::class.java)

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        val cookies: String = CookieManager.getInstance()?.getCookie(INSTAGRAM_MAIN_URL) ?: ""
        if (url == "https://www.instagram.com/" && cookies != "") {
            sessionInteractor.saveAuthCookies(cookies)
            onAuthSuccessCallback(cookies)
        }
    }

    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
        if ("X-IG-WWW-Claim" in request?.requestHeaders?.keys ?: emptyList<String>()) {
            sessionInteractor.saveAuthHeaders(
                request?.requestHeaders ?: return super.shouldInterceptRequest(view, request)
            )
        }
        return super.shouldInterceptRequest(view, request)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        url?.let {
            view?.loadUrl(it)
        }
        return true
    }

}