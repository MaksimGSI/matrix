package matrix.io

import matrix.model.Matrix
import java.nio.file.Files
import java.nio.file.Path

/**
 * Чтение и валидация структуры матрицы из текстового файла.
 */
object MatrixFileReader {

    /**
     * Верхняя граница размера N для защиты от нехватки памяти при чтении битых файлов.
     */
    const val MAX_SUPPORTED_SIZE: Int = 10_000

    /**
     * Считывает матрицу из указанного [path].
     *
     * Ожидается формат: первая строка — размерность N, следующие N строк — по N чисел.
     * Возвращает [Result.failure], если нарушена структура данных или файл недоступен.
     */
    fun read(path: Path): Result<Matrix> = runCatching {
        require(Files.exists(path)) { "Файл не существует: $path" }
        require(Files.isReadable(path)) { "Нет прав на чтение файла: $path" }

        Files.newBufferedReader(path).use { reader ->
            val lines = reader.lineSequence()
                .map(String::trim)
                .filter(String::isNotEmpty)
                .iterator()

            require(lines.hasNext()) { "Файл пуст" }

            val header = lines.next()
            val n = header.toIntOrNull()
                ?: throw IllegalArgumentException("Первая строка должна содержать число N, получено: '$header'")

            require(n in 1..MAX_SUPPORTED_SIZE) {
                "Размер матрицы N должен быть в диапазоне [1, $MAX_SUPPORTED_SIZE], получено: $n"
            }

            val flatData = DoubleArray(n * n)
            var rowsRead = 0

            while (lines.hasNext()) {
                if (rowsRead >= n) {
                    throw IllegalArgumentException("Файл содержит больше строк, чем заявлено (N = $n)")
                }

                val tokens = lines.next().split("\\s+".toRegex())
                if (tokens.size != n) {
                    throw IllegalArgumentException(
                        "Строка ${rowsRead + 1} содержит ${tokens.size} чисел вместо ожидаемых $n"
                    )
                }

                val rowOffset = rowsRead * n
                for (col in 0 until n) {
                    val value = tokens[col].toDoubleOrNull()
                        ?: throw IllegalArgumentException(
                            "Некорректное число '${tokens[col]}' в строке ${rowsRead + 1}, столбце ${col + 1}"
                        )
                    flatData[rowOffset + col] = value
                }
                rowsRead++
            }

            require(rowsRead == n) {
                "Файл завершился раньше времени: прочитано $rowsRead строк из $n"
            }

            Matrix(n, flatData)
        }
    }
}