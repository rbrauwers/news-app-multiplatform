# News app (Multiplatform)

Kotlin Multiplatform project targeting Android and iOS.<br>
The app displays news and his sources.<br>
The implementation follows [app architecture](https://developer.android.com/topic/architecture) principles, such as modularization, dependency injection, single source of truth, etc.<br><br>

|                                        Solarized dark                                        |                                    Solarized Ocean                                    |
|:--------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------:|
| [Android](https://github.com/rbrauwers/news-app/blob/main/screenshots/headlines_android.png) | ![iOS](https://github.com/rbrauwers/news-app/blob/main/screenshots/headlines_ios.png) |

## How it works
Data is fetched from the [News API](https://newsapi.org/) and stored locally.

## Stack
- UI: [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) + [Material](https://m3.material.io/develop/android/jetpack-compose) + [Coil](https://github.com/Kamel-Media/Kamel)
- Navigation: [Voyager](https://voyager.adriel.cafe/)
- Resources management: [Icerock](https://github.com/icerockdev/moko-resources)
- Dependency injection: [Koin](https://insert-koin.io/)
- Network: [Ktor](https://ktor.io/)
- Local storage: [SQLDelight](https://github.com/cashapp/sqldelight)
- Build system: [Gradle Version Catalog](https://docs.gradle.org/current/userguide/platforms.html)
