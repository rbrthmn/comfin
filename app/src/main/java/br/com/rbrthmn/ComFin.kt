
package br.com.rbrthmn

import android.app.Application
import br.com.rbrthmn.di.uiModule
import br.com.rbrthmn.home.di.homeModule
import br.com.rbrthmn.operations.di.operationsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ComFin : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ComFin)
            modules(uiModule, homeModule, operationsModule)
        }
    }
}
