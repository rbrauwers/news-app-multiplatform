# News app (Multiplatform)

Kotlin Multiplatform project targeting Android and iOS.<br>
The app displays news and its sources.<br>
The implementation follows [app architecture](https://developer.android.com/topic/architecture) principles, such as modularization, dependency injection, single source of truth, etc.<br><be>

## Android
https://github.com/rbrauwers/news-app-multiplatform/assets/3301123/4d56f395-5395-4494-852b-ba82d3e0e00f

## iOS
https://github.com/rbrauwers/news-app-multiplatform/assets/3301123/16a08e22-362d-460d-b592-029b98adb733

## How it works
Data is fetched from the [News API](https://newsapi.org/), stored locally and observed by UI.

## Setup
Create an [API Key](https://newsapi.org/account) and place it at `local.properties`:
```
newsApiKey=YOUR_API_KEY
```

## Stack
- UI: [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) + [Material](https://m3.material.io/develop/android/jetpack-compose) + [Coil](https://github.com/Kamel-Media/Kamel)
- Navigation: [Voyager](https://voyager.adriel.cafe/)
- Resources management: [Icerock](https://github.com/icerockdev/moko-resources)
- Dependency injection: [Koin](https://insert-koin.io/)
- Network: [Ktor](https://ktor.io/)
- Local storage: [SQLDelight](https://github.com/cashapp/sqldelight)
- Build system: [Gradle Version Catalog](https://docs.gradle.org/current/userguide/platforms.html)
