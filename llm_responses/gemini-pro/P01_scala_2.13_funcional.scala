import scala.annotation.tailrec

object WordFrequencyFunctional {

  /**
   * Dada una lista de palabras, retorna un mapa con la frecuencia 
   * de cada palabra utilizando un enfoque estrictamente funcional e inmutable.
   */
  def wordFrequency(words: List[String]): Map[String, Int] = {
    
    // Uso de recursión de cola (@tailrec) para iterar sin mutar estado (sin variables 'var').
    @tailrec
    def countWords(remaining: List[String], acc: Map[String, Int]): Map[String, Int] = remaining match {
      case Nil => acc
      case word :: tail =>
        // Novedad Scala 2.13: 'updatedWith' permite actualizar un Map inmutable 
        // evaluando si la clave ya existe (Some) o no (None), devolviendo un nuevo Map.
        val nextAcc = acc.updatedWith(word) {
          case Some(count) => Some(count + 1)
          case None        => Some(1)
        }
        countWords(tail, nextAcc)
    }

    countWords(words, Map.empty[String, Int])
    
    /* 
     * NOTA IDIOMÁTICA SOBRE SCALA 2.13:
     * Si bien usamos @tailrec para cumplir con los requisitos exactos, la forma funcional 
     * más idiomática, directa y exclusiva de Scala 2.13 es usar 'groupMapReduce', 
     * que condensa foldLeft, map y groupBy en una sola pasada:
     * 
     * words.groupMapReduce(identity)(_ => 1)(_ + _)
     */
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

    // 3. Caso con repeticiones múltiples comprobando la pureza funcional
    val result3 = wordFrequency(List("tailrec", "map", "tailrec", "fold", "map", "tailrec"))
    assert(
      result3 == Map("tailrec" -> 3, "map" -> 2, "fold" -> 1), 
      "Error en repeticiones múltiples funcionales"
    )

    println("Todos los tests funcionales en Scala 2.13 pasaron correctamente.")
  }
}