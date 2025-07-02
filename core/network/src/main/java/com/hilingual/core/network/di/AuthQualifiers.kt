package com.hilingual.core.network.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoginClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthClient