package com.filimonov.hexlayout.parameters

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.runtime.Immutable

@Immutable
data class ScalingParameters(
    @FloatRange(0.0, 1.0)
    val minimum: Float,
    val easing: Easing
) {
    companion object {
        val Default = ScalingParameters(minimum = .1f, easing = LinearEasing)
    }
}
