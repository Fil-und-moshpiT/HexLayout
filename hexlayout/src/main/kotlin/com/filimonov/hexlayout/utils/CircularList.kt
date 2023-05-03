package com.filimonov.hexlayout.utils
import kotlin.collections.ArrayList

internal class CircularList<E>(size: Int) : ArrayList<E>(size) {
    constructor() : this(1)

    override fun get(index: Int): E {
        return when {
            index < 0 -> get(size + index)
            index >= size -> get(index % size)
            else -> super.get(index)
        }
    }

    override fun set(index: Int, element: E): E {
        return when {
            index < 0 -> set(size + index, element)
            index >= size -> set(index % size, element)
            else -> super.set(index, element)
        }
    }
}

internal fun <T> circularList(size: Int, init: (index: Int) -> T): CircularList<T> {
    val list = CircularList<T>(size)
    repeat(size) { index -> list.add(init(index)) }
    return list
}

internal fun <T> emptyCircularList(): CircularList<T> = CircularList()

/**
 * @param size size of matrix [size x size]
 */
internal fun <T> circularMatrix(size: Int, init: (column: Int, row: Int) -> T) : CircularList<CircularList<T>> {
    return circularList(size) { column ->
        circularList(size) { row ->
            init.invoke(column, row)
        }
    }
}

/**
 * Find size of matrix for given [number] of elements
 *
 * @param number number of elements that should fit in matrix
 * @return [Int] size of matrix
 */
internal fun findMatrixSize(number: Int): Int {
    // x = sqrt(y)
    var y = 1

    while (true) {
        val result = y * y
        if (result % number == 0) return y

        y++
    }
}

/**
 * Find size of matrix for given [number] of elements which bigger that [vertical] x [horizontal]
 *
 * @param number number of elements that should fit in matrix
 * @return [Int] size of matrix
 */
internal fun findMatrixSizeMoreThan(number: Int, vertical: Int, horizontal: Int): Int {
    val minimum = vertical * horizontal

    var size = 1

    while (true) {
        val result = size * size
        if (result > minimum && result % number == 0) return size

        size++
    }
}

internal fun <E> List<E>.toCircularArray(): CircularList<E> = CircularList<E>(this.size).apply { addAll(this@toCircularArray) }
