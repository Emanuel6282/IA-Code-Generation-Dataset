import scala.annotation.tailrec

// Implementación funcional en Scala 3.3 usando sintaxis basada en sangría (indentation-based syntax)
def wordFrequency(words: List[String]): Map[String, Int] =
  words.foldLeft(Map.empty[String, Int]): (acc, word) =>
    acc.updated(word, acc.getOrElse(word, 0) + 1)

// Alternativa funcional pura mediante recursión de cola con @tailrec
def wordFrequencyTailRec(words: List[String]): Map[String, Int] =
  @tailrec
  def loop(remaining: List[String], acc: Map[String, Int]): Map[String, Int] =
    remaining match
      case Nil => acc
      case head :: tail => loop(tail, acc.updated(head, acc.getOrElse(head, 0) + 1))
  
  loop(words, Map.empty[String, Int])


// --- Casos de prueba (Scala 3.3) ---

@main def runTests(): Unit =
  // Caso 1: Ejemplo provisto con palabras repetidas
  assert(
    wordFrequency(List("the", "fox", "the")) == Map("the" -> 2, "fox" -> 1),
    "Falló el Caso 1"
  )

  // Caso 2: Lista de entrada vacía
  assert(
    wordFrequency(List.empty[String]) == Map.empty[String, Int],
    "Falló el Caso 2"
  )

  // Caso 3: Palabras únicas sin repeticiones
  assert(
    wordFrequency(List("scala", "functional", "3.3")) == Map("scala" -> 1, "functional" -> 1, "3.3" -> 1),
    "Falló el Caso 3"
  )

  println("¡Todos los casos de prueba pasaron correctamente!")