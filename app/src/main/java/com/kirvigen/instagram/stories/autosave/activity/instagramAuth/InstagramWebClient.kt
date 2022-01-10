package com.kirvigen.instagram.stories.autosave.activity.instagramAuth

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import org.koin.java.KoinJavaComponent.inject

class InstagramWebClient(private val onAuthSuccessCallback: (String) -> Unit) : WebViewClient() {

    private val instagramRepository by inject(InstagramRepository::class.java)

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        val cookies: String = instagramRepository.getInstagramCookies()
        if (url == "https://www.instagram.com/" && cookies != "") {
            onAuthSuccessCallback(cookies)
        }
    }

    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
        if ("X-IG-WWW-Claim" in request?.requestHeaders?.keys ?: emptyList<String>()) {
            instagramRepository.saveAuthHeaders(
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