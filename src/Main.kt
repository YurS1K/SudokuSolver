fun main() {
    val sudoku = Sudoku.fromFile("sudoku.txt")

    if (sudoku != null) {
        //for (i in 1..9) {
        //    val box = sudoku.getBox(i)
        //    val values = emptyList<Int>().toMutableList()
        //    for (j in 0..<3) {
        //        println()
        //        for (k in 0..<3) {
        //            values.add(box[j][k])
        //            print(box[j][k].toString() + " ")
        //        }
        //    }
        //    println((values.toSet().size != 9 && values.toSet().contains(0)).toString() + " " + values.toSet().toString())
        //}
        //println(sudoku.validate())

        println("Исходная сетка:")
        sudoku.printGrid()

        if (sudoku.solve()) {
            println("\nРешенная сетка:")
            sudoku.printGrid()
        } else {
            println("Судоку не имеет решения.")
        }
    } else {
        println("Не удалось загрузить судоку из файла.")
    }
}
