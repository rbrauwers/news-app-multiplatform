package settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rbrauwers.newsapp.auth.PlatformBiometricAuthenticatorSpecs
import com.rbrauwers.newsapp.auth.rememberBiometryAuthenticator
import com.rbrauwers.newsapp.auth.rememberPlatformBiometricAuthenticatorSpecs
import com.rbrauwers.newsapp.designsystem.BackNavigationIcon
import com.rbrauwers.newsapp.designsystem.BottomBarState
import com.rbrauwers.newsapp.designsystem.CenteredError
import com.rbrauwers.newsapp.designsystem.LocalAppState
import com.rbrauwers.newsapp.designsystem.NewsAppDefaultProgressIndicator
import com.rbrauwers.newsapp.designsystem.NewsDefaultTopBar
import com.rbrauwers.newsapp.designsystem.TopBarState
import com.rbrauwers.newsapp.designsystem.providers.PlatformInsetProvider
import com.rbrauwers.newsapp.model.Article
import com.rbrauwers.newsapp.resources.MultiplatformResources
import dev.icerock.moko.biometry.BiometryAuthenticator
import dev.icerock.moko.biometry.compose.BiometryAuthenticatorFactory
import dev.icerock.moko.biometry.compose.rememberBiometryAuthenticatorFactory
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch

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
        onPropertyAuthentication = component::propertyAuthentication,
        onRemoveLikes = component::onRemoveLikes,
        platformInsetProvider = component.platformInsetProvider
    )
}

@Composable
private fun SettingsScreenContent(
    uiState: SettingsUiState,
    onIcerockAuthentication: (BiometryAuthenticator) -> Unit,
    onPropertyAuthentication: (PlatformBiometricAuthenticatorSpecs) -> Unit,
    onRemoveLikes: (List<Article>) -> Unit,
    platformInsetProvider: PlatformInsetProvider
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is SettingsUiState.Loading -> {
                NewsAppDefaultProgressIndicator(placeOnCenter = true)
            }

            is SettingsUiState.Error -> {
                CenteredError(text = "Something went wrong.")
            }

            is SettingsUiState.Success -> {
                Success(
                    uiState = uiState,
                    onIcerockAuthentication = onIcerockAuthentication,
                    onPropertyAuthentication = onPropertyAuthentication,
                    onRemoveLikes = onRemoveLikes,
                    platformInsetProvider = platformInsetProvider
                )
            }
        }
    }
}

@Composable
private fun Success(
    uiState: SettingsUiState.Success,
    onIcerockAuthentication: (BiometryAuthenticator) -> Unit,
    onPropertyAuthentication: (PlatformBiometricAuthenticatorSpecs) -> Unit,
    onRemoveLikes: (List<Article>) -> Unit,
    platformInsetProvider: PlatformInsetProvider
) {
    val biometryFactory: BiometryAuthenticatorFactory = rememberBiometryAuthenticatorFactory()
    val biometryAuthenticator = rememberBiometryAuthenticator(biometryFactory)
    val specs = rememberPlatformBiometricAuthenticatorSpecs()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }

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

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Likes count: ${uiState.likesCount ?: "N/A"}")

            Spacer(modifier = Modifier.weight(1f))

            TextButton(onClick = {
                isSheetOpen = true
            }) {
                Text("SEE")
            }
        }
    }

    if (isSheetOpen) {
        LikedArticlesBottomSheet(
            uiState = uiState,
            onDismissRequest = {
                println("qqq onDismissRequest")
                isSheetOpen = false
            },
            onRemoveLikes = onRemoveLikes,
            platformInsetProvider = platformInsetProvider
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun LikedArticlesBottomSheet(
    uiState: SettingsUiState.Success,
    onDismissRequest: () -> Unit,
    onRemoveLikes: (List<Article>) -> Unit,
    platformInsetProvider: PlatformInsetProvider
) {
    val selectedArticles = remember {
        mutableStateListOf<Article>()
    }

    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val insets = platformInsetProvider.remember()

    println("qqq insets: ${insets.hashCode()} - $insets")

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .heightIn(min = 300.dp),
        windowInsets = insets.bottomSheetInsets
    ) {
        Text(
            text = "Liked articles",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(20.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            contentPadding = PaddingValues(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
        ) {
            items(
                items = uiState.likedArticles,
                key = { it.id }
            ) { article ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = article.title ?: "N/A",
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Checkbox(
                        checked = selectedArticles.contains(article),
                        onCheckedChange = { checked ->
                            if (checked) {
                                selectedArticles.add(article)
                            } else {
                                selectedArticles.remove(article)
                            }
                        }
                    )
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }

        TextButton(
            onClick = {
                coroutineScope.launch {
                    sheetState.hide()
                    onDismissRequest()
                    onRemoveLikes(selectedArticles)
                }
            },
            modifier = Modifier.padding(horizontal = 4.dp),
            enabled = selectedArticles.isNotEmpty()
        ) {
            Text(text = "REMOVE")
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}