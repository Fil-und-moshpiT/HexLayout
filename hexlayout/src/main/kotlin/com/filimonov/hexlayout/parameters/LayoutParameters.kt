package com.filimonov.hexlayout.parameters

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.center
import kotlin.math.hypot

/**
 *  Holds current layout parameters: size, center, distance to center.
 */
@Immutable
internal data class LayoutParameters(
    val size: IntSize,
    val center: IntOffset
) {
    @Stable
    val distanceToCenter = hypot(center.x.toFloat(), center.y.toFloat())

    companion object {
        fun create(width: Int, height: Int): LayoutParameters {
            val size = IntSize(width, height)
            return LayoutParameters(size, center = size.center)
        }
    }
}
