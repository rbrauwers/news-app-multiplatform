package settings

import androidx.compose.runtime.Immutable
import com.arkivanov.decompose.ComponentContext
import com.rbrauwers.newsapp.auth.BiometricStatus
import com.rbrauwers.newsapp.auth.IcerockBiometricAuthenticator
import com.rbrauwers.newsapp.auth.PlatformBiometricAuthenticatorSpecs
import com.rbrauwers.newsapp.auth.PropertyBiometricAuthenticator
import com.rbrauwers.newsapp.common.Result
import com.rbrauwers.newsapp.common.asResult
import com.rbrauwers.newsapp.common.coroutineScope
import com.rbrauwers.newsapp.data.repository.HeadlineRepository
import com.rbrauwers.newsapp.designsystem.providers.CustomInsets
import com.rbrauwers.newsapp.designsystem.providers.PlatformInsetProvider
import com.rbrauwers.newsapp.model.Article
import dev.icerock.moko.biometry.BiometryAuthenticator
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal interface SettingsComponent {
    val uiState: StateFlow<SettingsUiState>
    val platformInsetProvider: PlatformInsetProvider
    fun icerockAuthentication(authenticator: BiometryAuthenticator)
    fun propertyAuthentication(specs: PlatformBiometricAuthenticatorSpecs)
    fun onRemoveLikes(likes: List<Article>)
}

internal class DefaultSettingsComponent(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    override val platformInsetProvider: PlatformInsetProvider,
    private val headlineRepository: HeadlineRepository,
    private val icerockBiometricAuthenticator: IcerockBiometricAuthenticator,
    private val propertyBiometricAuthenticator: PropertyBiometricAuthenticator,
) : SettingsComponent, ComponentContext by componentContext {

    // The scope is automatically cancelled when the component is destroyed
    private val scope = coroutineScope(mainContext + SupervisorJob())

    private val headlinesFlow = headlineRepository
        .getHeadlines()
        .asResult()

    private val icerockBiometricsFlow = MutableStateFlow(
        BiometricStatus.NotRequested.toBiometricData(
            type = BiometricAuthenticatorType.Icerock
        )
    )

    private val propertyBiometricsFlow = MutableStateFlow(
        BiometricStatus.NotRequested.toBiometricData(
            type = BiometricAuthenticatorType.Property
        )
    )

    override val uiState = combine(
        headlinesFlow,
        icerockBiometricsFlow,
        propertyBiometricsFlow
    ) { headlineResult, icerockBiometricData, propertyBiometricData ->
        headlineResult.toSettingsUiState(
            icerockBiometricData = icerockBiometricData,
            propertyBiometricData = propertyBiometricData
        )
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState.Loading
    )

    override fun icerockAuthentication(authenticator: BiometryAuthenticator) {
        scope.launch {
            val result = icerockBiometricAuthenticator.authenticate(
                biometryAuthenticator = authenticator
            )

            icerockBiometricsFlow.update {
                result.toBiometricData(
                    type = BiometricAuthenticatorType.Icerock
                )
            }
        }
    }

    override fun propertyAuthentication(specs: PlatformBiometricAuthenticatorSpecs) {
        scope.launch {
            val result = propertyBiometricAuthenticator.authenticate(specs = specs)

            propertyBiometricsFlow.update {
                result.toBiometricData(
                    type = BiometricAuthenticatorType.Property
                )
            }
        }
    }

    override fun onRemoveLikes(likes: List<Article>) {
        scope.launch {
            headlineRepository.updateLikes(likes = likes.associate {
                Pair(it.id.toLong(), false)
            })
        }
    }
}

@Immutable
internal sealed interface SettingsUiState {

    @Immutable
    data object Loading : SettingsUiState

    @Immutable
    data object Error : SettingsUiState

    @Immutable
    data class Success(
        val likedArticles: List<Article> = emptyList(),
        val likesCount: String? = likedArticles.size.toString(),
        val icerockBiometricData: BiometricData,
        val propertyBiometricData: BiometricData,
    ) : SettingsUiState

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

private fun Result<List<Article>?>.toSettingsUiState(
    icerockBiometricData: SettingsUiState.BiometricData,
    propertyBiometricData: SettingsUiState.BiometricData
): SettingsUiState {
    return when (this) {
        is Result.Loading -> SettingsUiState.Loading
        is Result.Error -> SettingsUiState.Error
        is Result.Success -> SettingsUiState.Success(
            likedArticles = data?.filter { it.liked } ?: emptyList(),
            icerockBiometricData = icerockBiometricData,
            propertyBiometricData = propertyBiometricData
        )
    }
}