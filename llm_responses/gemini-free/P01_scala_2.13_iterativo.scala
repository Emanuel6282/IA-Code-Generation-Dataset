import scala.collection.mutable

def wordFrequency(words: List[String]): Map[String, Int] = {
  val frequencyMap = mutable.Map[String, Int]()
  var current = words

  // Paradigma iterativo estricto: uso de bucle while con estado y mapa mutable
  while (current.nonEmpty) {
    val word = current.head
    val count = frequencyMap.getOrElse(word, 0)
    frequencyMap.put(word, count + 1)
    current = current.tail
  }

  // Conversión al mapa inmutable estándar de Scala
  frequencyMap.toMap
}

// --- Casos de prueba (Scala 2.13) ---

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
  wordFrequency(List("scala", "iterativo", "2.13")) == Map("scala" -> 1, "iterativo" -> 1, "2.13" -> 1),
  "Falló el Caso 3"
)

println("¡Todos los casos de prueba pasaron correctamente!")