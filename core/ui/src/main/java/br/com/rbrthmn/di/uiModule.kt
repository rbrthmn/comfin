package br.com.rbrthmn.di

import br.com.rbrthmn.ui.utils.DecimalFormatter
import br.com.rbrthmn.ui.utils.DecimalInputFieldFormatter
import br.com.rbrthmn.ui.utils.ResourceStringProvider
import br.com.rbrthmn.ui.utils.SnackBarProvider
import br.com.rbrthmn.ui.utils.SnackBarProviderImpl
import br.com.rbrthmn.ui.utils.StringProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val uiModule = module {
    single<StringProvider> { ResourceStringProvider(context = androidContext()) }
    singleOf<SnackBarProvider>(::SnackBarProviderImpl)
    factoryOf<DecimalInputFieldFormatter>(::DecimalFormatter)
}
