package matrix

import matrix.io.MatrixFileReader
import matrix.math.MatrixMath
import java.nio.file.Paths
import java.util.Locale
import kotlin.math.abs
import kotlin.math.round
import kotlin.system.exitProcess

/**
 * Преобразует вычисленный определитель в компактную строку:
 * целые значения выводятся без плавающей точки, дробные — с обрезкой лишних нулей.
 */
fun formatDeterminant(value: Double): String {
    val rounded = round(value)
    return if (abs(value - rounded) < 1e-9) {
        rounded.toLong().toString()
    } else {
        String.format(Locale.US, "%.6f", value).trimEnd('0').trimEnd('.')
    }
}

/**
 * Запуск CLI-утилиты. В качестве аргумента передается путь к входному файлу.
 */
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        System.err.println("Использование: java -jar matrix-det.jar <путь_к_файлу>")
        exitProcess(1)
    }

    val path = Paths.get(args[0])

    MatrixFileReader.read(path).fold(
        onSuccess = { matrix ->
            val det = MatrixMath.determinant(matrix)
            println(formatDeterminant(det))
        },
        onFailure = { err ->
            System.err.println("Ошибка обработки: ${err.message}")
            exitProcess(2)
        }
    )
}