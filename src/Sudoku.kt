import java.io.File

// Класс для работы с сеткой Судоку
class Sudoku(private var grid: Array<IntArray> = Array(9) { IntArray(9) { 0 } }) {
    companion object {
        // Метод ввода через файл
        fun input(filePath: String): Sudoku? {
            return try {
                val lines = File(filePath).readLines()
                val grid = Array(9) { IntArray(9) { 0 } }

                for (i in lines.indices) {
                    val numbers = lines[i].split(" ").map { it.toIntOrNull() ?: 0 }
                    grid[i] = numbers.toIntArray()
                }

                Sudoku(grid)
            } catch (e: Exception) {
                println(e.message.toString())
                null
            }
        }

        // Случайная генерация Судоку
        fun input(): Sudoku {

            val grid = Array(9) { IntArray(9) { 0 } }
            for (i in 1..9) {
                val indexRow = (0..8).random()
                val indexCol = (0..8).random()
                val randNum = (1..9).random()

                grid[indexRow][indexCol] = randNum
            }
            return Sudoku(grid)
        }
    }

    // Перевод сетки Судоку в строку
    override fun toString(): String {
        var str = ""
        for (row in grid.indices) {
            if (row % 3 == 0 && row != 0) {
                str += "-----------------------\n"
            }

            for (col in grid[row].indices) {
                if (col % 3 == 0 && col != 0) {
                    str += "| "
                }
                str += if (grid[row][col] == 0) ". " else grid[row][col].toString() + " "
            }
            str += "\n"
        }
        return str
    }

    // Решение через полный перебор
    fun solve(): Boolean {
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

    // Решение с оптимизацией
    fun solveModifier(): Boolean {
        for (row in 0..<9) {
            for (col in 0..<9) {
                if (grid[row][col] == 0) {
                    for (num in 1..9) {
                        if (validateNum(num, row, col)) {
                            grid[row][col] = num
                            if (solveModifier()) {
                                return true
                            }
                            grid[row][col] = 0
                        }
                    }
                    return false
                }
            }
        }
        return true
    }

    // Проверка правильности сетки Судоку по правилам
    fun validateInput(): Boolean {
        // Проверка строк
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

        // Проверка столбцов
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

        // Проверяем квадратов
        for (i in 1..9) {
            val box = this.getBox(i)
            val seen = emptySet<Int>().toHashSet()
            for (j in 0..<3) {
                for (k in 0..<3) {
                    val num = box[j][k]
                    if (num != 0) {
                        if (seen.contains(num)) {
                            return false
                        }
                        seen.add(num)
                    }
                }
            }
        }
        return true
    }

    // Сохранение в файл
    fun saveToFile(filePath: String) {
        if (filePath.isEmpty()) {
            File("sudokuOutput.txt").createNewFile()
            File("sudokuOutput.txt").writeText(this.toString())
        } else {
            if (File(filePath).exists()) {
                File(filePath).writeText(this.toString())
            }
            else{
                File(filePath).createNewFile()
                File(filePath).writeText(this.toString())
            }
            File(filePath).createNewFile()
        }
    }

    // Получение одного из девяти квадратов Судоку по его номеру(от 1 до 9)
    private fun getBox(boxNum: Int): Array<IntArray> {
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

    // Валидация, что число может быть вставлено в ячейку сетки
    private fun validateNum(num: Int, row: Int, col: Int): Boolean {
        // Проверка строки
        var seen = emptySet<Int>().toHashSet()
        for (i in 0..<9) {
            seen.add(grid[row][i])
        }
        if (seen.contains(num)) {
            return false
        }

        // Проверка столбца
        seen = emptySet<Int>().toHashSet()
        for (i in 0..<9) {
            seen.add(grid[i][col])
        }
        if (seen.contains(num)) {
            return false
        }

        // Проверка квадрата
        val index = (row / 3) * 3 + col / 3 + 1
        val box = this.getBox(index)
        seen = emptySet<Int>().toHashSet()
        for (j in 0..<3) {
            for (k in 0..<3) {
                seen.add(box[j][k])
            }
        }
        return !seen.contains(num)
    }




}
