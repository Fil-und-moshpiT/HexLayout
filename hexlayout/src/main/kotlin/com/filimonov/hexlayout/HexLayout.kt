package com.filimonov.hexlayout

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import com.filimonov.hexlayout.parameters.LayoutParameters
import com.filimonov.hexlayout.parameters.ScalingParameters
import com.filimonov.hexlayout.positioning.PointyTopHexPositionCalculator
import com.filimonov.hexlayout.utils.circularList
import com.filimonov.hexlayout.utils.findMatrixSizeMoreThan
import kotlin.math.max

/**
 * Hexagonal grid layout.
 * The [content] block defines a DSL which allows you to emit items of different types.
 * You can use [HexViewScope.items] to add a list of items.
 *
 * @param modifier the modifier to apply to this layout.
 * @param circles number of circles at hexagon grid around central hex to fill screen.
 * @param scalingParameters describes how to scale items from screen center to edges.
 * @param content a block which describes the content. Inside this block you can use method [HexViewScope.items] to add a list of items.
 */
@Composable
fun HexLayout(
    modifier: Modifier = Modifier,
    circles: Int,
    scalingParameters: ScalingParameters = ScalingParameters.Default,
    content: HexViewScope.() -> Unit,
) {
    val state = remember { HexViewState() }

    // get items from content
    val items = remember(content) { HexViewScopeImpl().apply(content).items }
    val itemProvider = remember(items) { ItemProvider(items) }

    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {
        val width = with(LocalDensity.current) { maxWidth.roundToPx() }
        val height = with(LocalDensity.current) { maxHeight.roundToPx() }

        val layoutParameters = remember(width, height) { LayoutParameters.create(width, height) }

        val positionCalculator = remember(layoutParameters, circles) { PointyTopHexPositionCalculator(layoutParameters.size, circles) }

        val visibleBounds = remember(layoutParameters, circles) { Bounds.create(layoutParameters.size, circles) }

        // this list helps to normalize possible infinite count of hexes
        // (because we calculate current hex as center + column and row from bounds)
        // to finite hex field n * n (n - max bound)
        // it helps us to use final set of keys for subcomposition
        val normalizerList = remember(items, visibleBounds) {
            val maximumBoundsCount = max(visibleBounds.verticalCount, visibleBounds.horizontalCount)
            circularList(
                size = findMatrixSizeMoreThan(items.size, maximumBoundsCount, maximumBoundsCount),
                init = { it }
            )
        }

        val layoutItemsInfo =
            remember(
                visibleBounds,
                scalingParameters,
                layoutParameters,
                positionCalculator
            ) {
                LayoutItemsInfo(
                    visibleBounds = visibleBounds,
                    scalingParameters = scalingParameters,
                    layoutParameters = layoutParameters,
                    hexPositionCalculator = positionCalculator
                )
            }

        SubcomposeLayout(
            modifier = Modifier.fillMaxSize().clipToBounds().then(state.modifier),
            measurePolicy = remember(itemProvider) {
                { constraints ->
                    with(CachingMeasurementScope(itemProvider, layoutItemsInfo, this)) {
                        val dragged = state.dragged
                        val center = state.centerHexPosition

                        val newCenterHex = positionCalculator.getCenterHex(-dragged)

                        HexLayoutMeasureResult(
                            dragged = dragged,
                            centerHexPosition = newCenterHex,
                            centerHexCoordinates = positionCalculator.getPosition(newCenterHex.column, newCenterHex.row),
                            measureResult = layout(width = constraints.maxWidth, height = constraints.maxHeight) {
                                visibleBounds.horizontal.forEach { column ->
                                    visibleBounds.vertical.forEach { row ->
                                        val centeredColumn = center.column + column
                                        val centeredRow = center.row + row
                                        val hexColumnRow = HexPosition(centeredColumn, centeredRow)

                                        val position = layoutItemsInfo.itemsInfo[hexColumnRow]?.position
                                        if (position != null) {
                                            val normalizedColumn = normalizerList[centeredColumn]
                                            val normalizedRow = normalizerList[centeredRow]
                                            val key = HexPosition(normalizedColumn, normalizedRow)

                                            measure(
                                                key = key,
                                                position = hexColumnRow,
                                                constraints = positionCalculator.hexParameters.constraints
                                            ).map { it.place(position) }
                                        }
                                    }
                                }
                            }
                        )
                    }.also {
                        // update center hex in state
                        state.applyMeasureResult(it)

                        // update position and scale of hexes
                        layoutItemsInfo.applyMeasureResult(it)
                    }
                }
            }
        )
    }
}
