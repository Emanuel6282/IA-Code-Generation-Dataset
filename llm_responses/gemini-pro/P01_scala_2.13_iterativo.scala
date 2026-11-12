import scala.collection.mutable

object WordFrequencyIterative {

  /**
   * Dada una lista de palabras, retorna un mapa con la frecuencia 
   * de cada palabra de forma estrictamente iterativa.
   */
  def wordFrequency(words: List[String]): Map[String, Int] = {
    // 1. Uso estricto de mutable.Map para mantener el estado
    val freqMap = mutable.Map.empty[String, Int]
    
    // 2. Uso de mutable.ListBuffer. El factory '.from' es una novedad estándar en Scala 2.13
    val buffer = mutable.ListBuffer.from(words)
    
    // 3. Uso explícito de 'var' como índice para el bucle
    var i = 0
    
    // 4. Bucle 'while' clásico (paradigma imperativo/iterativo)
    while (i < buffer.length) {
      val word = buffer(i)
      
      // 5. 'updateWith' es una novedad brillante de Scala 2.13 para Mapas mutables e inmutables,
      // que reemplaza los clásicos bloques if/else o freqMap.getOrElseUpdate.
      freqMap.updateWith(word) {
        case Some(count) => Some(count + 1)
        case None        => Some(1)
      }
      
      i += 1 // Mutación explícita de estado
    }
    
    // Retornamos la interfaz inmutable por convención en Scala
    freqMap.toMap
  }

  def main(args: Array[String]): Unit = {
    // ==========================================
    // CASOS DE PRUEBA
    // ==========================================

    // 1. Caso base del ejemplo proporcionado
    val result1 = wordFrequency(List("the", "fox", "the"))
    assert(result1 == Map("the" -> 2, "fox" -> 1), "Error en el caso base")

    // 2. Caso con una lista vacía (debe retornar un mapa vacío)
    val result2 = wordFrequency(List.empty[String])
    assert(result2 == Map.empty[String, Int], "Error en el caso de lista vacía")

    // 3. Caso con repeticiones múltiples
    val result3 = wordFrequency(List("scala", "var", "scala", "while", "var", "scala"))
    assert(result3 == Map("scala" -> 3, "var" -> 2, "while" -> 1), "Error en repeticiones múltiples")

    println("Todos los tests iterativos en Scala 2.13 pasaron correctamente.")
  }
}