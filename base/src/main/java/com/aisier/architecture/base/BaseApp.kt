package com.aisier.architecture.base

import android.app.Application
import androidx.lifecycle.*

/**
 * author : wutao
 * e-mail : 670831931@qq.com
 * time   : 2019/05/17
 * desc   :
 * version: 1.0
 */
open class BaseApp : Application(), ViewModelStoreOwner {

    override fun onCreate() {
        super.onCreate()
        instance = this
        ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationLifecycleObserver())
    }

    override fun getViewModelStore(): ViewModelStore = ViewModelStore()

    companion object {
        lateinit var instance: BaseApp
            private set
    }

    private inner class ApplicationLifecycleObserver : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        private fun onAppForeground() {
            // TODO: "ApplicationObserver: app moved to foreground"
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        private fun onAppBackground() {
            // TODO: "ApplicationObserver: app moved to background"
        }
    }
}