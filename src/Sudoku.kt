import java.io.File
import java.io.IOException

class Sudoku(private val grid: Array<IntArray>) {
    companion object {
        fun fromFile(filePath: String): Sudoku? {
            return try {
                val lines = File(filePath).readLines()
                val grid = Array(9) { IntArray(9) }

                for (i in lines.indices) {
                    val numbers = lines[i].split(" ").map { it.toIntOrNull() ?: 0 }
                    require(numbers.size == 9) { "Каждая строка должна содержать ровно 9 чисел." }
                    grid[i] = numbers.toIntArray()
                }

                Sudoku(grid)
            } catch (e: IOException) {
                println("Ошибка чтения файла: ${e.message}")
                null
            } catch (e: IllegalArgumentException) {
                println("Ошибка формата файла: ${e.message}")
                null
            }
        }
    }

    fun printGrid() {
        for (row in grid.indices) {
            if (row % 3 == 0 && row != 0) {
                println("- - - - - - - - - - - -")
            }

            for (col in grid[row].indices) {
                if (col % 3 == 0 && col != 0) {
                    print("| ")
                }
                print(if (grid[row][col] == 0) ". " else "${grid[row][col]} ")
            }
            println()
        }
    }

    fun getBox(boxNum: Int): Array<IntArray> {
        val row = ((boxNum - 1) / 3) * 3
        val col = ((boxNum - 1) % 3) * 3

        val box = Array(3) { IntArray(3) }

        for (i in row..<row + 3) {
            for (j in col..<col + 3) {
                box[i - row][j - col] = grid[i][j]
            }
        }

        return box
    }

    fun validateInput(): Boolean {
        for (row in 0..<9) {
            val seen = emptySet<Int>().toHashSet()
            for (col in 0..<9) {
                val num = grid[row][col]
                if (num != 0) {
                    if (seen.contains(num)) {
                        return false
                    }
                    seen.add(num)
                }
            }
        }

        // Проверяем столбцы
        for (col in 0..<9) {
            val seen = emptySet<Int>().toHashSet()
            for (row in 0..<9) {
                val num = grid[row][col]
                if (num != 0) {
                    if (seen.contains(num)) {
                        return false
                    }
                    seen.add(num)
                }
            }
        }

        // Проверяем квадраты 3x3
        for (i in 1..9) {
            val box = this.getBox(i)
            val seen = emptySet<Int>().toHashSet()
            for (j in 0..<3) {
                for (k in 0..<3) {
                    val num = box[j][k]
                    if (num != 0)
                    {
                        if(seen.contains(num)){
                            return false
                        }
                        seen.add(num)
                    }

                }
            }
        }

        return true
    }

    fun validate(): Boolean {
        for (i in 0..<9) {
            if (grid[i].toSet().size != 9 || grid[i].toSet().contains(0) ) {
                return false
            }
        }

        for (i in 0..<9)
        {
            val values = emptyList<Int>().toMutableList()
            for (j in 0..<9) {
                values.add(grid[j][i])
            }
            if(values.toSet().size != 9 || values.toSet().contains(0)) {
                return false
            }
        }

        // Проверяем 3x3 квадрат
        for (i in 1..9) {
            val box = this.getBox(i)
            val values = emptyList<Int>().toMutableList()
            for (j in 0..<3) {
                for (k in 0..<3) {
                    values.add(box[j][k])
                }
            }
            if(values.toSet().size != 9 || values.toSet().contains(0)) {
                return false
            }
        }

        return true
    }

    fun solve():Boolean {
        for (row in 0..<9) {
            for (col in 0..<9) {
                if (grid[row][col] == 0) {
                    for (num in 1..9) {
                        grid[row][col] = num
                        if (solve()) {
                            return true
                        }
                        grid[row][col] = 0
                    }
                    return false
                }
            }
        }
        return validateInput()
    }

    fun solveModifier(): Boolean {
        for (row in 0..<9) {
            for (col in 0..<9) {
                if (grid[row][col] == 0) { // Найти пустую ячейку
                    for (num in 1..9) {
                        if (validateInput()) {
                            grid[row][col] = num // Пробуем число

                            if (solveModifier()) { // Рекурсивный вызов
                                return true
                            }

                            grid[row][col] = 0 // Возвращаемся назад
                        }
                    }
                    return false // Не удалось решить
                }
            }
        }
        return true // Судоку решено
    }

}
