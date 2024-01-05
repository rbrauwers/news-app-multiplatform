package settings

import com.arkivanov.decompose.ComponentContext
import com.rbrauwers.newsapp.auth.BiometricStatus
import com.rbrauwers.newsapp.auth.IcerockBiometricAuthenticator
import com.rbrauwers.newsapp.auth.PlatformBiometricAuthenticatorSpecs
import com.rbrauwers.newsapp.auth.PropertyBiometricAuthenticator
import com.rbrauwers.newsapp.common.coroutineScope
import dev.icerock.moko.biometry.BiometryAuthenticator
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal interface SettingsComponent {
    val uiState: StateFlow<SettingsUiState>
    fun icerockAuthentication(authenticator: BiometryAuthenticator)
    fun propertyAuthentication(specs: PlatformBiometricAuthenticatorSpecs)
}

internal class DefaultSettingsComponent(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    private val icerockBiometricAuthenticator: IcerockBiometricAuthenticator,
    private val propertyBiometricAuthenticator: PropertyBiometricAuthenticator
) : SettingsComponent, ComponentContext by componentContext {

    // The scope is automatically cancelled when the component is destroyed
    private val scope = coroutineScope(mainContext + SupervisorJob())

    private val _uiState =
        MutableStateFlow(
            SettingsUiState(
                icerockBiometricData = BiometricStatus.NotRequested.toBiometricData(
                    type = BiometricAuthenticatorType.Icerock),
                propertyBiometricData = BiometricStatus.NotRequested.toBiometricData(
                    type = BiometricAuthenticatorType.Property)
            )
        )
    override val uiState = _uiState.asStateFlow()

    override fun icerockAuthentication(authenticator: BiometryAuthenticator) {
        scope.launch {
            val result = icerockBiometricAuthenticator.authenticate(
                biometryAuthenticator = authenticator
            )
            _uiState.update {
                it.copy(
                    icerockBiometricData = result.toBiometricData(
                        type = BiometricAuthenticatorType.Icerock
                    )
                )
            }
        }
    }

    override fun propertyAuthentication(specs: PlatformBiometricAuthenticatorSpecs) {
        scope.launch {
            val result = propertyBiometricAuthenticator.authenticate(specs = specs)
            _uiState.update {
                it.copy(
                    propertyBiometricData = result.toBiometricData(
                        type = BiometricAuthenticatorType.Property
                    )
                )
            }
        }
    }
}

internal data class SettingsUiState(
    val icerockBiometricData: BiometricData,
    val propertyBiometricData: BiometricData
) {
    data class BiometricData(
        val type: BiometricAuthenticatorType,
        val authenticationText: String,
        val isAuthenticationButtonEnabled: Boolean = false,
    )
}

private fun BiometricStatus.toBiometricData(type: BiometricAuthenticatorType): SettingsUiState.BiometricData {
    return when (this) {
        is BiometricStatus.Error -> {
            SettingsUiState.BiometricData(
                type = type,
                authenticationText = "Biometric error: ${error ?: "N/A"}"
            )
        }

        is BiometricStatus.Success -> {
            SettingsUiState.BiometricData(
                type = type,
                authenticationText = "Authenticated"
            )
        }

        is BiometricStatus.NotAvailable -> {
            SettingsUiState.BiometricData(
                type = type,
                authenticationText = "Biometry not available"
            )
        }

        is BiometricStatus.Failure -> {
            SettingsUiState.BiometricData(
                type = type,
                authenticationText = "Authentication failed"
            )
        }

        is BiometricStatus.NotRequested -> {
            SettingsUiState.BiometricData(
                type = type,
                authenticationText = "Access sensitive data",
                isAuthenticationButtonEnabled = true
            )
        }
    }
}

internal enum class BiometricAuthenticatorType(val title: String) {
    Icerock(title = "Icerock biometric authentication"),
    Property(title = "Property biometric authentication")
}