package com.filimonov.hexlayout

import androidx.compose.runtime.Immutable

/**
 * Represents column and row of hex (in 2d array way of storage)
 */
@Immutable
data class HexPosition(
    val column: Int,
    val row: Int,
) {
    operator fun plus(other: HexPosition): HexPosition = HexPosition(column + other.column, row + other.row)
    operator fun minus(other: HexPosition): HexPosition = HexPosition(column - other.column, row - other.row)

    companion object {
        val Zero = HexPosition(0, 0)
    }
}
