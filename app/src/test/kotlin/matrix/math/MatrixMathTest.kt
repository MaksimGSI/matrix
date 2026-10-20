package matrix.math

import matrix.model.Matrix
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MatrixMathTest {

    private val delta = 1e-9

    @Test
    @DisplayName("Определитель матрицы 1x1 равен единственному элементу")
    fun testMatrix1x1() {
        val m = Matrix(1, doubleArrayOf(42.5))
        assertEquals(42.5, MatrixMath.determinant(m), delta)
    }

    @Test
    @DisplayName("Определитель единичной матрицы равен 1.0")
    fun testIdentityMatrix() {
        val n = 4
        val m = Matrix(n)
        for (i in 0 until n) {
            m[i, i] = 1.0
        }
        assertEquals(1.0, MatrixMath.determinant(m), delta)
    }

    @Test
    @DisplayName("Метод Гаусса с частичным выбором справляется с нулевым элементом на диагонали")
    fun testZeroPivotHandling() {
        // [ 0  1 ]
        // [ 1  0 ] -> det = 0*0 - 1*1 = -1.0
        val m = Matrix(2, doubleArrayOf(0.0, 1.0, 1.0, 0.0))
        assertEquals(-1.0, MatrixMath.determinant(m), delta)
    }

    @Test
    @DisplayName("Определитель треугольной матрицы равен произведению элементов диагонали")
    fun testUpperTriangularMatrix() {
        // [ 2  3  4 ]
        // [ 0  5  6 ]
        // [ 0  0  7 ] -> det = 2 * 5 * 7 = 70
        val m = Matrix(3, doubleArrayOf(
            2.0, 3.0, 4.0,
            0.0, 5.0, 6.0,
            0.0, 0.0, 7.0
        ))
        assertEquals(70.0, MatrixMath.determinant(m), delta)
    }

    @Test
    @DisplayName("Вырожденная матрица (линейно зависимые строки) дает определитель 0.0")
    fun testSingularMatrix() {
        // Строка 3 = Строка 1 + Строка 2
        val m = Matrix(3, doubleArrayOf(
            1.0, 2.0, 3.0,
            4.0, 5.0, 6.0,
            5.0, 7.0, 9.0
        ))
        assertEquals(0.0, MatrixMath.determinant(m), delta)
    }

    @Test
    @DisplayName("Матрица общего вида 3x3 вычисляется корректно")
    fun testGeneralMatrix3x3() {
        // [  2  -1   0 ]
        // [ -1   2  -1 ]
        // [  0  -1   2 ] -> det = 4
        val m = Matrix(3, doubleArrayOf(
            2.0, -1.0,  0.0,
            -1.0,  2.0, -1.0,
            0.0, -1.0,  2.0
        ))
        assertEquals(4.0, MatrixMath.determinant(m), delta)
    }
    @Test
    @DisplayName("Инвариант: det(A) == det(A^T)")
    fun testTransposeInvariant() {
        val original = Matrix(3, doubleArrayOf(
            1.0, 5.0, 3.0,
            2.0, 4.0, 7.0,
            4.0, 6.0, 2.0
        ))
        val transposed = Matrix(3)
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                transposed[j, i] = original[i, j]
            }
        }
        assertEquals(MatrixMath.determinant(original), MatrixMath.determinant(transposed), delta)
    }
}