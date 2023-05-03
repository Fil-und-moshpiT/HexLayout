package com.filimonov.hexlayout.utils

import androidx.compose.ui.unit.IntOffset
import kotlin.math.ceil
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * Return data index from Ulam's spiral by coordinates
 *
 * Inspired by: Ronald L. Graham, Donald E. Knuth, Oren Patashnik, Concrete Mathematics, Addison-Wesley, 1989,
 * chapter 3, Integer Functions, exercise 40 page 99 and answer page 498.
 *
 * @param x: column
 * @param y: row
 *
 * @return index
 */
internal fun getIndexByColumnRow(x: Int, y: Int): Int {
    return if (x * x > y * y) {
        var index = 4 * x * x - x - y
        if (y < x) {
            index += 2 * (y - x)
        }
        index
    } else {
        var index = 4 * y * y - x - y
        if (y < x) {
            index -= 2 * (y - x)
        }
        index
    }
}

/**
 * https://oeis.org/A296030
 */
internal fun getColumnRowByIndex(i: Int): IntOffset {
    val index = i + 1f
    val k = ceil((sqrt(index) - 1) / 2f)
    var t = 2 * k + 1
    var m = t * t
    t -= 1

    if (index >= m - t)
        return  IntOffset((k - (m - index)).roundToInt(), -k.roundToInt())
    else
        m -= t


    if (index >= m - t)
        return IntOffset(-k.roundToInt(), ((m - index) - k).roundToInt())
    else
        m -= t

    return if (index >= m - t)
        IntOffset(((m - index) - k).roundToInt(),  k.roundToInt())
    else
        IntOffset(k.roundToInt(),  (k - (m - index - t)).roundToInt())
}
