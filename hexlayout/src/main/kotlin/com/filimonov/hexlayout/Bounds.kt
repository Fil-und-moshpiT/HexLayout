package com.filimonov.hexlayout

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.IntSize
import java.math.RoundingMode
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Represents bounds - horizontal and vertical ranges in which hexes can fill screen (and a little bit outside)
 */
@Immutable
internal data class Bounds(
    val horizontal: IntRange,
    val vertical: IntRange
) {
    @Stable
    val horizontalCount: Int = horizontal.count()
    @Stable
    val verticalCount: Int = vertical.count()

    companion object {
        fun create(layoutSize: IntSize, circles: Int) =
            Bounds(
                calculateHorizontalItemsRange(layoutSize, circles),
                calculateVerticalItemsRange(layoutSize, circles)
            )
    }
}

private fun calculateHorizontalItemsRange(layoutSize: IntSize, count: Int): IntRange {
    val hexDiameter = hexRadius(max(layoutSize.width, layoutSize.height), count) * 2
    val horizontal = (layoutSize.width / hexDiameter).toBigDecimal().setScale(1, RoundingMode.HALF_UP).toInt()

    return IntRange(-horizontal, horizontal)
}

private fun calculateVerticalItemsRange(layoutSize: IntSize, count: Int): IntRange {
    val hexDiameter = hexRadius(max(layoutSize.width, layoutSize.height), count) * 2
    val vertical = (layoutSize.height / hexDiameter).toBigDecimal().setScale(1, RoundingMode.HALF_UP).toInt()

    return IntRange(-vertical, vertical)
}

private fun hexRadius(layoutSize: Int, count: Int): Int {
    val quarters = 4 + (3 * count * 2).toFloat()
    val quarterSize = layoutSize / quarters

    return (quarterSize * 2).roundToInt()
}

private fun IntRange.count(): Int = abs(last - first) + 1 // + 1 - to compensate zero
