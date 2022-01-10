package com.kirvigen.instagram.stories.autosave

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.kirvigen.instagram.stories.autosave.activity.mainScreen.MainViewModel
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramInteractor
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramInteractorImpl
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepositoryImpl
import com.kirvigen.instagram.stories.autosave.instagramUtils.db.InstagramDb
import com.kirvigen.instagram.stories.autosave.utils.OkHttpClientCoroutine
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object MainModuleCreater {

    fun create() = module {
        single { OkHttpClientCoroutine() }
        single { androidContext().getSharedPreferences("LocalStorage", Context.MODE_PRIVATE) }
        single<InstagramRepository> { InstagramRepositoryImpl(get(), get(), get(), get()) }
        single<InstagramInteractor> { InstagramInteractorImpl(get(), androidContext()) }
        viewModel { MainViewModel(get(), get()) }
        single<InstagramDb> {
            Room.databaseBuilder(
                androidContext(),
                InstagramDb::class.java, "instagramDb"
            ).build()
        }
        single { get<InstagramDb>().ProfileDao() }
        single { get<InstagramDb>().StoriesDao() }
    }
}