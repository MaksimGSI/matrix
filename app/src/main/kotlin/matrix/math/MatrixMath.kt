package matrix.math

import matrix.model.Matrix
import kotlin.math.abs

/**
 * Методы линейной алгебры и численного анализа.
 */
object MatrixMath {

    /**
     * Порог отсечения для численной устойчивости при сравнении с нулем.
     */
    const val EPSILON: Double = 1e-12

    /**
     * Вычисляет определитель матрицы методом Гаусса за O(N^3).
     *
     * Для избежания деления на ноль и подавления вычислительной погрешности
     * используется частичный выбор ведущего элемента (partial pivoting).
     * Исходный объект [matrix] не мутируется.
     */
    fun determinant(matrix: Matrix): Double {
        val n = matrix.size
        if (n == 1) return matrix[0, 0]

        val work = matrix.copy()
        var det = 1.0
        var sign = 1.0

        for (col in 0 until n) {
            // Ищем максимальный ведущий элемент в текущем столбце
            var pivotRow = col
            var maxPivot = abs(work[col, col])

            for (row in col + 1 until n) {
                val currentAbs = abs(work[row, col])
                if (currentAbs > maxPivot) {
                    maxPivot = currentAbs
                    pivotRow = row
                }
            }

            // Столбец околонулевой — матрица вырождена
            if (maxPivot < EPSILON) {
                return 0.0
            }

            // Перестановка строк меняет знак определителя
            if (pivotRow != col) {
                work.swapRows(col, pivotRow)
                sign = -sign
            }

            val pivot = work[col, col]
            det *= pivot

            // Прямой ход: вычитаем пропорциональную строку
            for (row in col + 1 until n) {
                val factor = work[row, col] / pivot
                for (j in col + 1 until n) {
                    work[row, j] -= factor * work[col, j]
                }
            }
        }

        val result = det * sign
        // Нивелируем возможный -0.0 из-за особенностей IEEE 754
        return if (abs(result) < EPSILON) 0.0 else result
    }
}