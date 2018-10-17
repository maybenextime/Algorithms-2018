package lesson1

import org.junit.jupiter.api.Assertions.assertEquals
import java.io.File
import kotlin.test.assertEquals

abstract class AbstractFileTests {
    protected fun assertFileContent(name: String, expectedContent: String) {
        val file = File(name)
        val content = file.readLines().joinToString("\n")
        assertEquals(expectedContent.trim(), content.trim())
    }
}