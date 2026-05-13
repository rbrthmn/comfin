package br.com.rbrthmn

import android.app.Application
import br.com.rbrthmn.data.di.dataModule
import br.com.rbrthmn.data.home.di.homeDataModule
import br.com.rbrthmn.data.misc.di.miscDataModule
import br.com.rbrthmn.data.operations.di.operationsDataModule
import br.com.rbrthmn.di.uiModule
import br.com.rbrthmn.home.di.homeModule
import br.com.rbrthmn.misc.di.miscModule
import br.com.rbrthmn.operations.di.operationsModule
import br.com.rbrthmn.settings.di.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ComFin : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ComFin)
            modules(
                dataModule,
                homeDataModule,
                operationsDataModule,
                miscDataModule,
                uiModule,
                homeModule,
                operationsModule,
                settingsModule,
                miscModule
            )
        }
    }
}
