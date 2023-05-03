package com.filimonov.hexlayout

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.util.lerp
import com.filimonov.hexlayout.parameters.LayoutParameters
import com.filimonov.hexlayout.parameters.ScalingParameters
import com.filimonov.hexlayout.positioning.HexPositionCalculator
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import kotlin.math.hypot

/**
 *  Helper class that store visible items positions and scale
 *  @param visibleBounds bounds in which items info will calculated
 *  @param layoutParameters layout parameters
 *  @param hexPositionCalculator hex position calculator
 *  @param scalingParameters describe how to scale items from center to edges
 */
internal class LayoutItemsInfo(
    private val visibleBounds: Bounds,
    private val layoutParameters: LayoutParameters,
    private val hexPositionCalculator: HexPositionCalculator,
    private val scalingParameters: ScalingParameters
) : IMeasureResultConsumer {
    private var dragged: IntOffset by mutableStateOf(IntOffset.Zero)
    private var centerHexPosition: HexPosition by mutableStateOf(HexPosition.Zero)

    val itemsInfo: ImmutableMap<HexPosition, ItemInfo> by derivedStateOf {
        buildMap {
            visibleBounds.horizontal.forEach { column ->
                visibleBounds.vertical.forEach { row ->
                    val centeredColumn = centerHexPosition.column + column
                    val centeredRow = centerHexPosition.row + row

                    val position = dragged + layoutParameters.center + hexPositionCalculator.getPosition(centeredColumn, centeredRow)

                    val distanceToCenterFromHex = hypot((position.x - layoutParameters.center.x).toFloat(), (position.y - layoutParameters.center.y).toFloat())
                    val fraction = 1f - distanceToCenterFromHex / layoutParameters.distanceToCenter
                    val scale = lerp(start = scalingParameters.minimum, stop = 1f, fraction = scalingParameters.easing.transform(fraction))

                    put(
                        key = HexPosition(centeredColumn, centeredRow),
                        value = ItemInfo(
                            position = position - hexPositionCalculator.hexParameters.offset,
                            scale = scale
                        )
                    )
                }
            }
        }.toImmutableMap()
    }

    override fun applyMeasureResult(measureResult: HexLayoutMeasureResult) {
        this.dragged = measureResult.dragged
        this.centerHexPosition = measureResult.centerHexPosition
    }
}

internal data class ItemInfo(
    val position: IntOffset,
    val scale: Float
)
