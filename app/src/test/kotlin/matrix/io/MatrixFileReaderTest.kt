package matrix.io

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path

class MatrixFileReaderTest {

    @TempDir
    lateinit var tempDir: Path

    @Test
    @DisplayName("Успешное чтение валидного файла")
    fun testSuccessfulRead() {
        val file = tempDir.resolve("valid.txt")
        Files.writeString(file, "2\n1.5 2.0\n3.0 4.0\n")

        val result = MatrixFileReader.read(file)
        assertTrue(result.isSuccess)
        val matrix = result.getOrThrow()
        assertEquals(2, matrix.size)
        assertEquals(1.5, matrix[0, 0])
        assertEquals(4.0, matrix[1, 1])
    }

    @Test
    @DisplayName("Ошибка при нехватке строк в файле")
    fun testMissingRows() {
        val file = tempDir.resolve("short.txt")
        Files.writeString(file, "3\n1 2 3\n4 5 6\n")

        val result = MatrixFileReader.read(file)
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("раньше времени") == true)
    }

    @Test
    @DisplayName("Ошибка при поврежденных числах (буквы вместо цифр)")
    fun testCorruptedNumbers() {
        val file = tempDir.resolve("corrupted.txt")
        Files.writeString(file, "2\n1.0 ABC\n3.0 4.0\n")

        val result = MatrixFileReader.read(file)
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("Некорректное число") == true)
    }
    @Test
    @DisplayName("Ошибка при отсутствии файла")
    fun testNonExistentFile() {
        val file = tempDir.resolve("does_not_exist.txt")
        val result = MatrixFileReader.read(file)
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("не существует") == true)
    }

    @Test
    @DisplayName("Ошибка если первая строка не число")
    fun testInvalidNFormat() {
        val file = tempDir.resolve("bad_n.txt")
        Files.writeString(file, "three\n1 2\n3 4\n")
        val result = MatrixFileReader.read(file)
        assertTrue(result.isFailure)
    }

    @Test
    @DisplayName("Ошибка при лишних элементах в строке")
    fun testExtraElementsInRow() {
        val file = tempDir.resolve("extra_cols.txt")
        Files.writeString(file, "2\n1 2 3\n4 5\n")
        val result = MatrixFileReader.read(file)
        assertTrue(result.isFailure)
    }

    @Test
    @DisplayName("Ошибка при лишних строках в конце файла")
    fun testExtraRows() {
        val file = tempDir.resolve("extra_rows.txt")
        Files.writeString(file, "2\n1 2\n3 4\n5 6\n")
        val result = MatrixFileReader.read(file)
        assertTrue(result.isFailure)
    }
}