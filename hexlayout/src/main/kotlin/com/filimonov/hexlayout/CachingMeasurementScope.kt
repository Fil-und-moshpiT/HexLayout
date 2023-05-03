package com.filimonov.hexlayout

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeMeasureScope
import androidx.compose.ui.unit.Constraints

// todo: there will be huge problems when i want to change size on drag (workaround: cache Measurables instead of Placeables?)

/**
 * Measure scope that caches [Placeable] by key
 */
internal class CachingMeasurementScope(
    private val itemProvider: ItemProvider,
    private val layoutItemsInfo: LayoutItemsInfo,
    private val subcomposeMeasureScope: SubcomposeMeasureScope
) : MeasureScope by subcomposeMeasureScope {

    private val cache = mutableMapOf<HexPosition, List<Placeable>>()

    /**
     * Return cached list of [Placeable]s by key
     * @param key unique [HexPosition] of [Placeable]
     */
    fun measure(key: HexPosition, position: HexPosition, constraints: Constraints): List<Placeable> {
        val cached = cache[key]
        return if (cached == null) {
            val measurables =
                subcomposeMeasureScope.subcompose(key) {
                    ScalingItemWrapper(position = position, layoutItemsInfo = layoutItemsInfo, content = itemProvider.item(position))
                }
            measurables.map { it.measure(constraints) }.also { cache[key] = it }
        } else cached
    }
}

@Composable
private fun ScalingItemWrapper(
    position: HexPosition,
    layoutItemsInfo: LayoutItemsInfo,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.graphicsLayer {
            val scale = layoutItemsInfo.itemsInfo[position]?.scale
            if (scale != null) {
                scaleX = scale
                scaleY = scale
            }
        },
        content = { content.invoke() }
    )
}
