package br.com.rbrthmn.settings

import br.com.rbrthmn.data.auth.repository.AuthRepository
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsScreenViewModelTest {
    private val authRepository: AuthRepository = mockk()
    private lateinit var viewModel: SettingsScreenViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = SettingsScreenViewModel(authRepository = authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `doOnInit should keep default state`() {
        viewModel.doOnInit()

        assertEquals(SettingsScreenContract.UiState(), viewModel.uiState.value)
    }

    @Test
    fun `onOpenDarkModeDialog should show dark mode dialog`() {
        viewModel.onIntent(SettingsScreenContract.Intent.OnOpenDarkModeDialog)

        assertTrue(viewModel.uiState.value.showDarkModeDialog)
    }

    @Test
    fun `onDismissDarkModeDialog should hide dark mode dialog`() {
        viewModel.onIntent(SettingsScreenContract.Intent.OnOpenDarkModeDialog)

        viewModel.onIntent(SettingsScreenContract.Intent.OnDismissDarkModeDialog)

        assertFalse(viewModel.uiState.value.showDarkModeDialog)
    }

    @Test
    fun `onSelectDarkModeOption should update option and emit ShowThemeUpdated`() = runTest {
        viewModel.onIntent(SettingsScreenContract.Intent.OnOpenDarkModeDialog)

        viewModel.onIntent(
            SettingsScreenContract.Intent.OnSelectDarkModeOption(DarkModeOption.DARK.label)
        )

        assertEquals(DarkModeOption.DARK.label, viewModel.uiState.value.selectedDarkModeOption)
        assertFalse(viewModel.uiState.value.showDarkModeDialog)
        assertEquals(SettingsScreenContract.Effect.ShowThemeUpdated, viewModel.effects.first())
    }

    @Test
    fun `onLogoutClick should show logout dialog`() {
        viewModel.onIntent(SettingsScreenContract.Intent.OnLogoutClick)

        assertTrue(viewModel.uiState.value.showLogoutDialog)
    }

    @Test
    fun `onDismissLogoutDialog should hide logout dialog`() {
        viewModel.onIntent(SettingsScreenContract.Intent.OnLogoutClick)

        viewModel.onIntent(SettingsScreenContract.Intent.OnDismissLogoutDialog)

        assertFalse(viewModel.uiState.value.showLogoutDialog)
    }

    @Test
    fun `onConfirmLogout should sign out and emit NavigateToSignIn`() = runTest {
        justRun { authRepository.signOut() }
        viewModel.onIntent(SettingsScreenContract.Intent.OnLogoutClick)

        viewModel.onIntent(SettingsScreenContract.Intent.OnConfirmLogout)

        verify(exactly = 1) { authRepository.signOut() }
        assertFalse(viewModel.uiState.value.showLogoutDialog)
        assertEquals(SettingsScreenContract.Effect.NavigateToSignIn, viewModel.effects.first())
    }
}
