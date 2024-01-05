package com.rbrauwers.newsapp.auth

sealed interface BiometricStatus {
    data object Failure : BiometricStatus
    data object NotRequested : BiometricStatus
    data object NotAvailable : BiometricStatus
    data object Success : BiometricStatus
    class Error(val error: String?) : BiometricStatus
}