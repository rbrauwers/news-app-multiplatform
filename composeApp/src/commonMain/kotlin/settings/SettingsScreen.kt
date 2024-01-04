package settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rbrauwers.newsapp.auth.rememberBiometryAuthenticator
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
        onAuthenticateClick = component::authenticate
    )
}

@Composable
private fun SettingsScreenContent(
    uiState: SettingsUiState,
    onAuthenticateClick: (BiometryAuthenticator) -> Unit
) {
    val biometryFactory: BiometryAuthenticatorFactory = rememberBiometryAuthenticatorFactory()
    val biometryAuthenticator = rememberBiometryAuthenticator(biometryFactory)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (uiState.isAuthenticationButtonEnabled) {
            OutlinedButton(onClick = {
                onAuthenticateClick(biometryAuthenticator)
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = uiState.authenticationText)
            }
        } else {
            Text(text = uiState.authenticationText, modifier = Modifier.fillMaxWidth())
        }
    }
}