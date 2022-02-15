package com.kirvigen.instagram.stories.autosave

import android.content.Context
import androidx.room.Room
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramInteractor
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramInteractorImpl
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepositoryImpl
import com.kirvigen.instagram.stories.autosave.instagramUtils.db.InstagramDb
import com.kirvigen.instagram.stories.autosave.user.SessionInteractor
import com.kirvigen.instagram.stories.autosave.user.SessionInteractorImpl
import com.kirvigen.instagram.stories.autosave.utils.OkHttpClientCoroutine
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object MainModuleCreater {

    fun create() = module {
        single { OkHttpClientCoroutine() }
        single { androidContext().getSharedPreferences("LocalStorage", Context.MODE_PRIVATE) }
        single<InstagramRepository> { InstagramRepositoryImpl(get(), get(), get(), get()) }
        single<InstagramInteractor> { InstagramInteractorImpl(androidContext(), get()) }
        single<SessionInteractor> { SessionInteractorImpl(get(), get()) }
        single {
            Room.databaseBuilder(
                androidContext(),
                InstagramDb::class.java, "instagramDb"
            ).build()
        }
        single { get<InstagramDb>().ProfileDao() }
        single { get<InstagramDb>().StoriesDao() }
    }
}