import scala.annotation.tailrec

/**
 * Dada una lista de palabras, retorna un mapa con la frecuencia 
 * de cada palabra utilizando un enfoque estrictamente funcional en Scala 3.
 */
def wordFrequency(words: List[String]): Map[String, Int] =
  // Función anidada pura utilizando recursión de cola (@tailrec)
  @tailrec
  def countWords(remaining: List[String], acc: Map[String, Int]): Map[String, Int] =
    // Pattern matching con la sintaxis limpia (sin llaves) de Scala 3
    remaining match
      case Nil => acc
      case word :: tail =>
        // 'updatedWith' permite modificar el Map inmutable devolviendo uno nuevo.
        // Scala 3 permite omitir las llaves también en bloques anónimos de pattern matching.
        val nextAcc = acc.updatedWith(word):
          case Some(count) => Some(count + 1)
          case None        => Some(1)
        
        countWords(tail, nextAcc)

  // Llamada inicial con estado inmutable vacío
  countWords(words, Map.empty[String, Int])

  /* 
   * NOTA SOBRE FUNCIONES DE ORDEN SUPERIOR:
   * Aunque se requiere demostrar el uso explícito de '@tailrec', el enfoque
   * funcional más idiomático y compacto (foldLeft) en Scala 3 sería:
   * words.foldLeft(Map.empty[String, Int]) { (acc, word) =>
   *   acc.updatedWith(word)(opt => Some(opt.getOrElse(0) + 1))
   * }
   */


// ==========================================
// CASOS DE PRUEBA
// ==========================================
// En Scala 3, usamos @main como punto de entrada directo
@main def runFunctionalTests(): Unit =
  // 1. Caso base del ejemplo proporcionado
  val result1 = wordFrequency(List("the", "fox", "the"))
  assert(result1 == Map("the" -> 2, "fox" -> 1), "Error en el caso base")

  // 2. Caso con una lista vacía (debe retornar un mapa vacío)
  val result2 = wordFrequency(List.empty[String])
  assert(result2 == Map.empty[String, Int], "Error en el caso de lista vacía")

  // 3. Caso con repeticiones múltiples evaluando la pureza funcional
  val result3 = wordFrequency(List("funcional", "scala3", "funcional", "tailrec", "scala3", "funcional"))
  assert(
    result3 == Map("funcional" -> 3, "scala3" -> 2, "tailrec" -> 1), 
    "Error en repeticiones múltiples funcionales"
  )

  println("Todos los tests funcionales en Scala 3.3 pasaron correctamente.")