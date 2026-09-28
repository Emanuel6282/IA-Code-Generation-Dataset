def wordFrequency(words: List[String]): Map[String, Int] = {
  words.groupBy(identity).transform((_, v) => v.size)
}

// --- Casos de prueba ---

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
  wordFrequency(List("scala", "functional", "code")) == Map("scala" -> 1, "functional" -> 1, "code" -> 1),
  "Falló el Caso 3"
)

println("¡Todos los casos de prueba pasaron correctamente!")