import java.io.File

fun main(args: Array<String>) {
    args.forEach {

        val name = "Day$it.txt"
        val testName = "Day${it}_test.txt"

        val file = File("input/$name")
        file.createNewFile()

        val testFile = File("input/$testName")
        testFile.createNewFile()
    }
}
