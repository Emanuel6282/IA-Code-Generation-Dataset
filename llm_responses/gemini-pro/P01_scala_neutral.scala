object WordFrequencyTest {

  /**
   * Dada una lista de palabras, retorna un mapa con la frecuencia de cada palabra.
   */
  def wordFrequency(words: List[String]): Map[String, Int] = {
    // Agrupamos la lista por cada palabra, creando un Map[String, List[String]]
    // Luego transformamos los valores (las listas) en su longitud (el conteo).
    words.groupBy(word => word).map { case (word, occurrences) =>
      word -> occurrences.length
    }
  }

  def main(args: Array[String]): Unit = {
    // ==========================================
    // CASOS DE PRUEBA
    // ==========================================

    // 1. Caso base del ejemplo proporcionado
    assert(
      wordFrequency(List("the", "fox", "the")) == Map("the" -> 2, "fox" -> 1),
      "Error en el caso base"
    )

    // 2. Caso con una lista vacía (debe retornar un mapa vacío)
    assert(
      wordFrequency(List.empty[String]) == Map.empty[String, Int],
      "Error en el caso de lista vacía"
    )

    // 3. Caso con múltiples repeticiones y palabras únicas
    assert(
      wordFrequency(List("scala", "java", "scala", "python", "java", "scala")) == Map("scala" -> 3, "java" -> 2, "python" -> 1),
      "Error en el conteo de repeticiones múltiples"
    )

    println("Todos los tests pasaron correctamente.")
  }
}