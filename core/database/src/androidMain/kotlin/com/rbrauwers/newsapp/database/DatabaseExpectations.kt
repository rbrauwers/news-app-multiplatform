package com.rbrauwers.newsapp.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class DatabaseDriverFactory actual constructor(): KoinComponent {
    actual fun createDriver(): SqlDriver {
        val context: Context by inject()
        return AndroidSqliteDriver(NewsMultiplatformDatabase.Schema, context, "news-multiplatform.db")
    }
}