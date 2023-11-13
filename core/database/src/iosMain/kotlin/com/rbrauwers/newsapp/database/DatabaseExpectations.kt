package com.rbrauwers.newsapp.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DatabaseDriverFactory actual constructor() {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(NewsMultiplatformDatabase.Schema, "news-multiplatform.db")
    }
}