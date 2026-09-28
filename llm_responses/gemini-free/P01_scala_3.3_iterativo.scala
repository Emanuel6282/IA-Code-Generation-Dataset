import scala.collection.mutable

// Implementación en Scala 3.3 usando la nueva sintaxis opcional sin llaves (Significant Indentation)
def wordFrequency(words: List[String]): Map[String, Int] =
  val frequencyMap = mutable.Map[String, Int]()
  var current = words

  // Paradigma iterativo estricto: uso de bucle while con estado mutable
  while current.nonEmpty do
    val word = current.head
    val count = frequencyMap.getOrElse(word, 0)
    frequencyMap.put(word, count + 1)
    current = current.tail

  // Conversión al mapa inmutable de la biblioteca estándar
  frequencyMap.toMap


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
    wordFrequency(List("scala", "iterativo", "3.3")) == Map("scala" -> 1, "iterativo" -> 1, "3.3" -> 1),
    "Falló el Caso 3"
  )

  println("¡Todos los casos de prueba pasaron correctamente!")