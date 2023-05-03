package com.filimonov.hexlayout.parameters

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset

/**
 *  Class that hold information about hexagons
 *  @param size hexagon size (
 */
@Immutable
internal data class HexParameters(
    val size: Int,
    val radius: Int
) {
    @Stable
    val offset: IntOffset = IntOffset(size, size) / 2f

    @Stable
    val constraints: Constraints = Constraints.fixed(size, size)
}
