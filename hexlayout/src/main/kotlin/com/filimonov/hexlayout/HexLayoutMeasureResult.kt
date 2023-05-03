package com.filimonov.hexlayout

import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.unit.IntOffset

class HexLayoutMeasureResult(
    val dragged: IntOffset,
    val centerHexPosition: HexPosition,
    val centerHexCoordinates: IntOffset,
    measureResult: MeasureResult
): MeasureResult by measureResult
