package settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rbrauwers.newsapp.auth.PlatformBiometricAuthenticatorSpecs
import com.rbrauwers.newsapp.auth.rememberBiometryAuthenticator
import com.rbrauwers.newsapp.auth.rememberPlatformBiometricAuthenticatorSpecs
import com.rbrauwers.newsapp.designsystem.BackNavigationIcon
import com.rbrauwers.newsapp.designsystem.BottomBarState
import com.rbrauwers.newsapp.designsystem.LocalAppState
import com.rbrauwers.newsapp.designsystem.NewsDefaultTopBar
import com.rbrauwers.newsapp.designsystem.TopBarState
import com.rbrauwers.newsapp.resources.MultiplatformResources
import dev.icerock.moko.biometry.BiometryAuthenticator
import dev.icerock.moko.biometry.compose.BiometryAuthenticatorFactory
import dev.icerock.moko.biometry.compose.rememberBiometryAuthenticatorFactory
import dev.icerock.moko.resources.compose.stringResource

@Composable
internal fun SettingsScreen(
    component: SettingsComponent,
    onNavigateBack: () -> Unit
) {
    val uiState: SettingsUiState by component.uiState.collectAsState()

    LocalAppState.current.apply {
        LaunchedEffect(Unit) {
            setTopBarState(
                topBarState = TopBarState(
                    title = { NewsDefaultTopBar(title = stringResource(MultiplatformResources.strings.settings)) },
                    navigationIcon = {
                        BackNavigationIcon(onBackClick = onNavigateBack)
                    }
                )
            )
            setBottomBarState(bottomBarState = BottomBarState(isVisible = false))
        }
    }

    SettingsScreenContent(
        uiState = uiState,
        onIcerockAuthentication = component::icerockAuthentication,
        onPropertyAuthentication = component::propertyAuthentication
    )
}

@Composable
private fun SettingsScreenContent(
    uiState: SettingsUiState,
    onIcerockAuthentication: (BiometryAuthenticator) -> Unit,
    onPropertyAuthentication: (PlatformBiometricAuthenticatorSpecs) -> Unit
) {
    val biometryFactory: BiometryAuthenticatorFactory = rememberBiometryAuthenticatorFactory()
    val biometryAuthenticator = rememberBiometryAuthenticator(biometryFactory)
    val specs = rememberPlatformBiometricAuthenticatorSpecs()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        BiometricData(
            data = uiState.icerockBiometricData,
            biometryAuthenticator = biometryAuthenticator,
            specs = specs,
            onIcerockAuthentication = onIcerockAuthentication,
            onPropertyAuthentication = onPropertyAuthentication
        )

        BiometricData(
            data = uiState.propertyBiometricData,
            biometryAuthenticator = biometryAuthenticator,
            specs = specs,
            onIcerockAuthentication = onIcerockAuthentication,
            onPropertyAuthentication = onPropertyAuthentication
        )
    }
}

@Composable
private fun BiometricData(
    data: SettingsUiState.BiometricData,
    biometryAuthenticator: BiometryAuthenticator,
    specs: PlatformBiometricAuthenticatorSpecs,
    onIcerockAuthentication: (BiometryAuthenticator) -> Unit,
    onPropertyAuthentication: (PlatformBiometricAuthenticatorSpecs) -> Unit
) {
    Text(text = data.type.title, style = MaterialTheme.typography.headlineSmall)
    Spacer(modifier = Modifier.height(8.dp))

    if (data.isAuthenticationButtonEnabled) {
        OutlinedButton(onClick = {
            when (data.type) {
                BiometricAuthenticatorType.Icerock -> {
                    onIcerockAuthentication(biometryAuthenticator)
                }

                BiometricAuthenticatorType.Property -> {
                    onPropertyAuthentication(specs)
                }
            }

        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = data.authenticationText)
        }
    } else {
        Text(text = data.authenticationText, modifier = Modifier.fillMaxWidth())
    }

    Divider(modifier = Modifier.padding(vertical = 20.dp))
}