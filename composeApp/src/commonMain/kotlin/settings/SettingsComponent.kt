package settings

import com.arkivanov.decompose.ComponentContext
import com.rbrauwers.newsapp.auth.BiometricAuthenticator
import com.rbrauwers.newsapp.common.coroutineScope
import dev.icerock.moko.biometry.BiometryAuthenticator
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal interface SettingsComponent {
    val uiState: StateFlow<SettingsUiState>
    fun authenticate(authenticator: BiometryAuthenticator)
}

internal class DefaultSettingsComponent(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    private val biometricAuthenticator: BiometricAuthenticator
) : SettingsComponent, ComponentContext by componentContext {

    // The scope is automatically cancelled when the component is destroyed
    private val scope = coroutineScope(mainContext + SupervisorJob())

    private val _uiState =
        MutableStateFlow(BiometricAuthenticator.BiometricStatus.NotRequested.toUiState())
    override val uiState = _uiState.asStateFlow()

    override fun authenticate(authenticator: BiometryAuthenticator) {
        scope.launch {
            val result = biometricAuthenticator.authenticate(
                biometryAuthenticator = authenticator
            )

            _uiState.value = result.toUiState()
        }
    }
}

internal data class SettingsUiState(
    val isAuthenticationButtonEnabled: Boolean = false,
    val authenticationText: String
)

private fun BiometricAuthenticator.BiometricStatus.toUiState(): SettingsUiState {
    return when (this) {
        is BiometricAuthenticator.BiometricStatus.Error -> {
            SettingsUiState(authenticationText = "Biometric error: ${error ?: "N/A"}")
        }

        is BiometricAuthenticator.BiometricStatus.Success -> {
            SettingsUiState(authenticationText = "Authenticated")
        }

        is BiometricAuthenticator.BiometricStatus.NotAvailable -> {
            SettingsUiState(authenticationText = "Biometry not available")
        }

        is BiometricAuthenticator.BiometricStatus.Failure -> {
            SettingsUiState(authenticationText = "Authentication failed")
        }

        is BiometricAuthenticator.BiometricStatus.NotRequested -> {
            SettingsUiState(
                authenticationText = "Access sensitive data",
                isAuthenticationButtonEnabled = true
            )
        }
    }
}