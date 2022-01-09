package com.kirvigen.instagram.stories.autosave

import android.webkit.CookieManager
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramInteractor
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramInteractorImpl
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepositoryImpl
import com.kirvigen.instagram.stories.autosave.utils.OkHttpClientCoroutine
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object MainModuleCreater {

    fun create() = module {
        single { OkHttpClientCoroutine() }
        single<InstagramRepository> { InstagramRepositoryImpl(get()) }
        single<InstagramInteractor> { InstagramInteractorImpl(get(), androidContext()) }
    }
}