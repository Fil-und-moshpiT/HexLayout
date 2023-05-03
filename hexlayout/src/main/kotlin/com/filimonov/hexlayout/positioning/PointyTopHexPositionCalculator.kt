package com.filimonov.hexlayout.positioning

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.round
import com.filimonov.hexlayout.HexPosition
import com.filimonov.hexlayout.parameters.HexParameters
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 *  Class that calculate positions hexagons in pointy-top hexagonal grid
 *  @param layoutSize size of layout.
 *  @param circles number of circles at hexagon grid around central hex.
 */
internal class PointyTopHexPositionCalculator(layoutSize: IntSize, circles: Int) : HexPositionCalculator() {
    override val hexParameters: HexParameters

    init {
        val halfHexSize = innerSize(layoutSize, circles)

        val size = halfHexSize * 2
        val radius = outerSize(layoutSize, circles)

        hexParameters = HexParameters(size, radius)
    }

    override fun getPosition(column: Int, row: Int): IntOffset {
        val x = hexParameters.radius * sqrt(3f) * (column + 0.5f * (row and 1))
        val y = hexParameters.radius * 1.5f * row

        return Offset(x, y).round()
    }

    override fun getCenterHex(offset: IntOffset): HexPosition {
        // ⎡q⎤     ⎡ sqrt(3)/3     -1/3 ⎤   ⎡x⎤
        // ⎢ ⎥  =  ⎢                    ⎥ × ⎢ ⎥ ÷ size
        // ⎣r⎦     ⎣     0          2/3 ⎦   ⎣y⎦

        // axial fractured
        val qf = (sqrt(3f) / 3f * offset.x - offset.y / 3f) / hexParameters.radius
        val rf = (2 / 3f * offset.y) / hexParameters.radius
        val sf = -qf - rf

        // axial round
        var q = qf.roundToInt()
        var r = rf.roundToInt()
        val s = sf.roundToInt()

        val qDiff = abs(q - qf)
        val rDiff = abs(r - rf)
        val sDiff = abs(s - sf)

        if (qDiff > rDiff && qDiff > sDiff)
            q = -r - s
        else if (rDiff > sDiff)
            r = -q - s

        // axial to offset
        val column = q + (r - (r and 1)) / 2
        val row = r

        return HexPosition(column, row)
    }

    private fun innerSize(layoutSize: IntSize, count: Int): Int {
        val maxLayoutSize = max(layoutSize.width, layoutSize.height)

        val halves = (count * 2 + 1) * 2f
        val halfSize = maxLayoutSize / halves

        return (halfSize / sqrt(3.0) * 2).roundToInt()
    }

    private fun outerSize(layoutSize: IntSize, count: Int): Int {
        val maxLayoutSize = max(layoutSize.width, layoutSize.height)

        val quarters = 4 + (3 * count * 2).toFloat()
        val quarterSize = maxLayoutSize / quarters

        return (quarterSize * 2).roundToInt()
    }
}