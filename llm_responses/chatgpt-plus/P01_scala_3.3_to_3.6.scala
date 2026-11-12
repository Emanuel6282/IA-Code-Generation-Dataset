/*
 * P01: Word Frequency — Scala 3.6
 *
 * CARACTERÍSTICAS MODERNAS UTILIZADAS:
 *
 * 1. Sintaxis de indentación de Scala 3:
 *    Permite definir objetos, métodos y bloques sin llaves.
 *    Mejora la legibilidad y reduce código repetitivo.
 *
 * 2. Condicionales if ... then ... else:
 *    Proporcionan una sintaxis clara y moderna.
 *
 * 3. Bucle for ... do:
 *    Permite recorrer colecciones manteniendo
 *    el paradigma imperativo original.
 *
 * 4. Inferencia de tipos:
 *    Reduce anotaciones redundantes sin perder
 *    la seguridad de tipos.
 *
 * 5. mutable.Map.empty:
 *    Expresa claramente la creación de un mapa vacío.
 *
 * NOTA:
 * Estas características son compatibles con Scala 3.6,
 * aunque no fueron introducidas exclusivamente en esa versión.
 * No se requieren funcionalidades nuevas específicas de Scala 3.6
 * para mejorar este algoritmo.
 */

import scala.collection.mutable

object P01WordFrequency_v36:

  def wordFrequency(words: List[String]): Map[String, Int] =

    // Crear un mapa mutable para almacenar las frecuencias
    val result = mutable.Map.empty[String, Int]

    // Recorrer las palabras utilizando un bucle imperativo
    for word <- words do

      if result.contains(word) then
        result(word) = result(word) + 1
      else
        result(word) = 1

    // Devolver un mapa inmutable
    result.toMap

  def main(args: Array[String]): Unit =

    // Test 1: Palabras repetidas
    assert(
      wordFrequency(List("the", "fox", "the")) ==
        Map("the" -> 2, "fox" -> 1)
    )

    // Test 2: Lista vacía
    assert(
      wordFrequency(List.empty) == Map.empty
    )

    // Test 3: Una sola palabra
    assert(
      wordFrequency(List("hello")) ==
        Map("hello" -> 1)
    )

    println("All tests passed")

/*
 * CAMBIOS REALIZADOS:
 *
 * 1. Actualización del nombre del objeto:
 *    P01WordFrequency_v213 -> P01WordFrequency_v36.
 *    Refleja la versión de destino.
 *
 * 2. Sintaxis de indentación:
 *    Se sustituyen las llaves por bloques definidos
 *    mediante indentación, siguiendo el estilo de Scala 3.
 *
 * 3. Condicionales modernos:
 *    Se reemplaza if (...) { ... } else { ... }
 *    por if ... then ... else.
 *
 * 4. Bucle for moderno:
 *    Se reemplaza for (word <- words) { ... }
 *    por for word <- words do.
 *
 * 5. Inicialización del mapa:
 *    Se reemplaza mutable.Map[String, Int]()
 *    por mutable.Map.empty[String, Int].
 *    Esta forma comunica mejor la intención.
 *
 * 6. Conservación de mutable.Map:
 *    Se mantiene el mismo mecanismo de conteo
 *    basado en modificaciones de un mapa mutable.
 *
 * 7. Conservación de result.toMap:
 *    El método sigue devolviendo un mapa inmutable.
 *
 * 8. Conservación de wordFrequency:
 *    Se mantiene exactamente el nombre y la firma.
 *
 * 9. Conservación de los tests:
 *    Se mantienen los tres casos originales,
 *    adaptando únicamente el formato a Scala 3.
 *
 * 10. Compatibilidad con Scala 3.6:
 *     Se utiliza la sintaxis idiomática de Scala 3
 *     sin librerías externas ni cambios de comportamiento.
 */