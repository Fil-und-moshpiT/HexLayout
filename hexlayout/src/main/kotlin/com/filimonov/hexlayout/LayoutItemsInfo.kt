package com.filimonov.hexlayout

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.util.lerp
import com.filimonov.hexlayout.parameters.HexParameters
import com.filimonov.hexlayout.parameters.LayoutParameters
import com.filimonov.hexlayout.parameters.ScalingParameters
import com.filimonov.hexlayout.positioning.HexPositionCalculator
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import kotlin.math.hypot

internal class LayoutItemsInfo(
    private val visibleBounds: Bounds,
    private val layoutParameters: LayoutParameters,
    private val hexPositionCalculator: HexPositionCalculator,
    private val scalingParameters: ScalingParameters
) {
    private val hexParameters: HexParameters = hexPositionCalculator.hexParameters
    private var dragged: IntOffset by mutableStateOf(IntOffset.Zero)
    private var centerHex: HexPosition by mutableStateOf(HexPosition.Zero)

    val itemsInfo: ImmutableMap<HexPosition, ItemInfo> by derivedStateOf {
        buildMap {
            visibleBounds.horizontal.forEach { column ->
                visibleBounds.vertical.forEach { row ->
                    val centeredColumn = centerHex.column + column
                    val centeredRow = centerHex.row + row

                    val position = dragged + layoutParameters.center + hexPositionCalculator.getPosition(centeredColumn, centeredRow)

                    val distanceToCenterFromHex = hypot((position.x - layoutParameters.center.x).toFloat(), (position.y - layoutParameters.center.y).toFloat())
                    val fraction = 1f - distanceToCenterFromHex / layoutParameters.distanceToCenter
                    val scale = lerp(start = scalingParameters.minimum, stop = 1f, fraction = scalingParameters.easing.transform(fraction))

                    put(
                        key = HexPosition(centeredColumn, centeredRow),
                        value = ItemInfo(
                            position = position - hexParameters.offset,
                            scale = scale
                        )
                    )
                }
            }
        }.toImmutableMap()
    }

    fun applyMeasureResult(dragged: IntOffset, centerHex: HexPosition) {
        this.dragged = dragged
        this.centerHex = centerHex
    }
}

internal data class ItemInfo(
    val position: IntOffset,
    val scale: Float
)
