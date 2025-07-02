package com.hilingual.core.network

import javax.inject.Inject
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator @Inject constructor() : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        // TODO: 토큰 리프레시 로직 추가 (민재)
        return null
    }
}