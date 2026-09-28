// P01: Word Frequency — Scala

def wordFrequency(words: List[String]): Map[String, Int] = {
  words.groupMapReduce(identity)(_ => 1)(_ + _)
}

// Casos de prueba

// Test 1: palabras repetidas
assert(
  wordFrequency(List("the", "fox", "the")) ==
    Map("the" -> 2, "fox" -> 1)
)

// Test 2: lista vacía
assert(
  wordFrequency(List.empty[String]) ==
    Map.empty[String, Int]
)

// Test 3: una sola palabra
assert(
  wordFrequency(List("hello")) ==
    Map("hello" -> 1)
)

println("All tests passed")