fun main() {
    print("Введите путь к файлу или нажмите Enter для случайного ввода: ")
    val inputStr = readln()

    val sudoku: Sudoku? = if (inputStr.isEmpty()) Sudoku.input() else Sudoku.input(inputStr) // Определяем метод ввода

    if (sudoku != null) {
        println("Введенное Судоку:")
        println(sudoku.toString())

        if (sudoku.validateInput()) {
            if (sudoku.solveModifier()) {

                print("Введите путь к файлу для сохранения решения: ")
                val filePath = readln()
                sudoku.saveToFile(filePath)

            }
            else {
                println("Нет решения")
            }
        }
        else {
            println("Нет решения")
        }
    }
}

