package matrix.model

/**
 * Квадратная матрица размерности [size] x [size].
 *
 * Значения хранятся в непрерывном массиве (row-major order),
 * что исключает лишние аллокации в куче и обеспечивает хорошую локальность по кэшу.
 */
class Matrix(val size: Int, initialData: DoubleArray? = null) {

    init {
        require(size > 0) { "Размер матрицы должен быть положительным, передано: $size" }
        if (initialData != null) {
            require(initialData.size == size * size) {
                "Размер переданного буфера (${initialData.size}) не совпадает с ожидаемым ($size x $size)"
            }
        }
    }

    private val data: DoubleArray = initialData?.copyOf() ?: DoubleArray(size * size)

    /**
     * Чтение элемента по индексам [row] и [col].
     */
    operator fun get(row: Int, col: Int): Double {
        checkBounds(row, col)
        return data[row * size + col]
    }

    /**
     * Запись значения [value] в ячейку ([row], [col]).
     */
    operator fun set(row: Int, col: Int, value: Double) {
        checkBounds(row, col)
        data[row * size + col] = value
    }

    /**
     * Построчный обмен элементов между [row1] и [row2].
     */
    fun swapRows(row1: Int, row2: Int) {
        if (row1 == row2) return
        checkBounds(row1, 0)
        checkBounds(row2, 0)

        val offset1 = row1 * size
        val offset2 = row2 * size
        for (col in 0 until size) {
            val tmp = data[offset1 + col]
            data[offset1 + col] = data[offset2 + col]
            data[offset2 + col] = tmp
        }
    }

    /**
     * Создает независимую копию матрицы для вычислений.
     */
    fun copy(): Matrix = Matrix(size, data)

    private fun checkBounds(row: Int, col: Int) {
        if (row !in 0 until size || col !in 0 until size) {
            throw IndexOutOfBoundsException("Индекс ($row, $col) вне диапазона матрицы $size x $size")
        }
    }
}