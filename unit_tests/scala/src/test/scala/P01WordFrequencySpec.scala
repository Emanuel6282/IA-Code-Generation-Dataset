import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/**
 * Unit Tests - P01: Word Frequency
 * La IA debe implementar: def wordFrequency(words: List[String]): Map[String, Int]
 * en un objeto llamado Solution
 */
class P01WordFrequencySpec extends AnyFunSuite with Matchers {

  test("TC01 - Palabra repetida basica") {
    Solution.wordFrequency(List("the", "fox", "the")) shouldBe Map("the" -> 2, "fox" -> 1)
  }

  test("TC02 - Lista vacia") {
    Solution.wordFrequency(List.empty) shouldBe Map.empty
  }

  test("TC03 - Una sola palabra") {
    Solution.wordFrequency(List("hello")) shouldBe Map("hello" -> 1)
  }

  test("TC04 - Multiples frecuencias") {
    Solution.wordFrequency(List("a","b","a","c","b","a")) shouldBe Map("a"->3,"b"->2,"c"->1)
  }

  test("TC05 - Tres palabras distintas") {
    Solution.wordFrequency(List("cat","dog","cat","bird","dog","cat")) shouldBe Map("cat"->3,"dog"->2,"bird"->1)
  }
}
