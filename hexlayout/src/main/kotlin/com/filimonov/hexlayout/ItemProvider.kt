package com.filimonov.hexlayout

import androidx.compose.runtime.Composable
import com.filimonov.hexlayout.utils.CircularList
import com.filimonov.hexlayout.utils.getIndexByColumnRow

/**
 *  Class that helps to obtain composable item by position
 */
internal class ItemProvider(private val items: CircularList<@Composable () -> Unit>) {

    /**
     * Return composable item by position (convert position into index at Ulam's spiral)
     */
    fun item(position: HexPosition): @Composable () -> Unit {
        val index = getIndexByColumnRow(position.column, position.row)
        return  items[index]
    }
}
