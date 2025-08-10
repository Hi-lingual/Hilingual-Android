package com.hilingual.presentation.otp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.hilingual.core.navigation.Route
import com.hilingual.presentation.otp.OtpScreen
import kotlinx.serialization.Serializable

fun NavController.navigateToOtp(
    navOptions: NavOptions? = null
) = navigate(route = Otp, navOptions = navOptions)

fun NavGraphBuilder.otpNavGraph(
    paddingValues: PaddingValues,
    navigateToOnboarding: () -> Unit,
) {
    composable<Otp> {
        OtpScreen(paddingValues = paddingValues)
    }
}

@Serializable
data object Otp : Route