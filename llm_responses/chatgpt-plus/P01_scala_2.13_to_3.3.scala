/*
 * P01: Word Frequency — Scala 3.3
 *
 * CARACTERÍSTICAS MODERNAS UTILIZADAS:
 *
 * 1. Sintaxis de indentación (Scala 3):
 *    Permite definir bloques sin llaves, mejorando la legibilidad.
 *
 * 2. if ... then ... else (Scala 3):
 *    Utiliza una sintaxis más expresiva para las condiciones.
 *
 * 3. for ... do (Scala 3):
 *    Permite recorrer colecciones con una sintaxis más clara.
 *
 * 4. Inferencia de tipos:
 *    Evita declaraciones redundantes sin perder seguridad de tipos.
 *
 * 5. Colecciones de la biblioteca estándar:
 *    Se conserva mutable.Map para mantener el algoritmo original.
 *
 * NOTA:
 * Estas características son compatibles con Scala 3.3,
 * aunque algunas fueron introducidas en Scala 3.0.
 */

import scala.collection.mutable

object P01WordFrequency_v33:

  def wordFrequency(words: List[String]): Map[String, Int] =

    // Mapa mutable para almacenar las frecuencias
    val result = mutable.Map.empty[String, Int]

    // Recorrido de las palabras
    for word <- words do

      if result.contains(word) then
        result(word) = result(word) + 1
      else
        result(word) = 1

    // Convertir el resultado en un mapa inmutable
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
 * 1. Eliminación de llaves:
 *    Se utiliza la sintaxis de indentación de Scala 3.
 *    Esto reduce el ruido visual del código.
 *
 * 2. Condicionales modernos:
 *    Se reemplaza if (...) { ... } else { ... }
 *    por if ... then ... else.
 *
 * 3. Bucle for moderno:
 *    Se reemplaza for (word <- words) { ... }
 *    por for word <- words do.
 *
 * 4. Inicialización del mapa:
 *    Se reemplaza mutable.Map[String, Int]()
 *    por mutable.Map.empty[String, Int].
 *    La intención de crear un mapa vacío queda más explícita.
 *
 * 5. Conservación de mutable.Map:
 *    Se mantiene el enfoque imperativo original.
 *    No se introducen transformaciones funcionales adicionales.
 *
 * 6. Conservación de result.toMap:
 *    La función continúa devolviendo un Map inmutable.
 *
 * 7. Conservación de wordFrequency:
 *    Se mantiene exactamente el nombre y la firma del método.
 *
 * 8. Conservación de los tests:
 *    Se mantienen los tres casos originales:
 *    - Palabras repetidas.
 *    - Lista vacía.
 *    - Una sola palabra.
 *
 * 9. Compatibilidad con Scala 3.3:
 *    El código utiliza sintaxis moderna de Scala 3
 *    sin incorporar dependencias externas.
 */