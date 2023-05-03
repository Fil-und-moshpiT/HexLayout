package com.filimonov.hexlayout.positioning

import androidx.compose.ui.unit.IntOffset
import com.filimonov.hexlayout.HexPosition
import com.filimonov.hexlayout.parameters.HexParameters

/**
 *  Interface that calculate position of hexagons
 */
internal abstract class HexPositionCalculator {
    abstract val hexParameters: HexParameters

    abstract fun getPosition(column: Int, row: Int): IntOffset

    abstract fun getCenterHex(offset: IntOffset = IntOffset.Zero): HexPosition
}
