package br.com.rbrthmn.settings

import br.com.rbrthmn.settings.ui.SettingsContract
import br.com.rbrthmn.settings.ui.SettingsViewModel
import org.junit.Test
import kotlin.test.assertEquals

class SettingsViewModelTest {
    private val viewModel: SettingsContract.ViewModel = SettingsViewModel()

    @Test
    fun `doOnInit should keep defaults`() {
        val vm = (viewModel as SettingsViewModel).doOnInit()
        assertEquals(false, vm.uiState.value.showDarkModeDialog)
        assertEquals(SettingsContract.ThemeOption.System, vm.uiState.value.selectedTheme)
    }

    @Test
    fun `OnDarkModeClick should open dialog`() {
        viewModel.onIntent(SettingsContract.Intent.OnDarkModeClick)
        assertEquals(true, viewModel.uiState.value.showDarkModeDialog)
    }

    @Test
    fun `OnDismissDarkModeDialog should close dialog`() {
        viewModel.onIntent(SettingsContract.Intent.OnDarkModeClick)
        viewModel.onIntent(SettingsContract.Intent.OnDismissDarkModeDialog)
        assertEquals(false, viewModel.uiState.value.showDarkModeDialog)
    }

    @Test
    fun `OnThemeSelected should change theme and close dialog`() {
        viewModel.onIntent(SettingsContract.Intent.OnDarkModeClick)
        viewModel.onIntent(SettingsContract.Intent.OnThemeSelected(SettingsContract.ThemeOption.Dark))
        assertEquals(SettingsContract.ThemeOption.Dark, viewModel.uiState.value.selectedTheme)
        assertEquals(false, viewModel.uiState.value.showDarkModeDialog)
    }
}
