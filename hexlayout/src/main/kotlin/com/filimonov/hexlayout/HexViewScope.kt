package com.filimonov.hexlayout

import androidx.compose.runtime.Composable
import com.filimonov.hexlayout.utils.CircularList
import com.filimonov.hexlayout.utils.emptyCircularList

interface HexViewScope {
    fun item(itemsContent: @Composable () -> Unit)
}

internal class HexViewScopeImpl : HexViewScope {
    val items: CircularList<@Composable () -> Unit> = emptyCircularList()

    override fun item(itemsContent: @Composable () -> Unit) {
        this.items.add(itemsContent)
    }
}

/**
 *  Adds list of items
 *
 *  @param itemContent the content displayed by a single item
 */
fun <T> HexViewScope.items(items: List<T>, itemContent: @Composable (T) -> Unit) =
    items.forEach { item { itemContent.invoke(it) } }
