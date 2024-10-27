package com.tps.challenge.application

import android.app.Application


/**
 * The application class - an entry point into our app where we initialize Dagger.
 */
class TCApplication : Application() {
    private lateinit var appComponent: AppComponent

    fun getAppComponent(): AppComponent {
        return appComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DefaultAppComponent()
    }
}