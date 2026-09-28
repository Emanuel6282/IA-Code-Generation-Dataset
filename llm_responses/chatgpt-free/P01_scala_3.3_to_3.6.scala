// P01: Word Frequency — Scala 3.6
//
// Características de Scala 3 utilizadas:
//
// 1. Sintaxis de indentación:
//    Elimina llaves innecesarias y mejora la legibilidad del código.
//
// 2. Sintaxis `if ... then`:
//    Forma moderna de expresar condiciones en Scala 3.
//
// 3. Sintaxis `for ... do`:
//    Permite expresar el recorrido iterativo de forma más clara.
//
// 4. Definiciones simplificadas:
//    Los métodos y objetos pueden utilizar bloques basados en
//    indentación.
//
// Nota:
// Scala 3.6 no introduce una característica específica que mejore
// significativamente este algoritmo de conteo. Por ello, no se
// agregan características artificiales que no aporten valor.
// Se mantiene mutable.Map y el algoritmo original para garantizar
// comportamiento idéntico.

import scala.collection.mutable

object P01WordFrequency_v36:

  def wordFrequency(words: List[String]): Map[String, Int] =
    val result = mutable.Map[String, Int]()

    for word <- words do
      if result.contains(word) then
        result(word) = result(word) + 1
      else
        result(word) = 1

    result.toMap

  def main(args: Array[String]): Unit =
    assert(
      wordFrequency(List("the", "fox", "the")) ==
        Map("the" -> 2, "fox" -> 1)
    )

    assert(
      wordFrequency(List.empty) ==
        Map.empty
    )

    assert(
      wordFrequency(List("hello")) ==
        Map("hello" -> 1)
    )

    println("All tests passed")


// ============================================================
// CAMBIOS REALIZADOS
// ============================================================
//
// 1. Actualización de la clase:
//
//      P01WordFrequency_v213
//
//    pasó a:
//
//      P01WordFrequency_v36
//
//    para identificar la versión objetivo.
//
// 2. Se utiliza la sintaxis de indentación de Scala 3:
//
//      object P01WordFrequency_v36:
//
//    en lugar de:
//
//      object P01WordFrequency_v213 {
//
//    Esto reduce las llaves y es una forma idiomática de escribir
//    código en Scala 3.
//
// 3. El método wordFrequency mantiene exactamente el mismo nombre:
//
//      def wordFrequency(words: List[String]): Map[String, Int]
//
// 4. El algoritmo se mantiene idéntico:
//
//      mutable.Map
//      for
//      contains
//      actualización mediante result(word)
//      result.toMap
//
//    Esto garantiza los mismos resultados que la versión original.
//
// 5. El bucle se adapta a la sintaxis moderna de Scala 3:
//
//      for word <- words do
//
//    en lugar de:
//
//      for (word <- words) {
//
// 6. El condicional utiliza la sintaxis:
//
//      if condicion then
//
//    propia de Scala 3.
//
// 7. Los tres casos de prueba se mantienen:
//
//      ["the", "fox", "the"] -> Map("the" -> 2, "fox" -> 1)
//      []                    -> Map.empty
//      ["hello"]             -> Map("hello" -> 1)
//
// 8. No se utilizan librerías externas.
//
// 9. No se cambia el paradigma del código original.
//    Continúa siendo una implementación imperativa basada en
//    mutable.Map y un recorrido mediante for.
//
// 10. No se introducen características específicas de Scala 3.6
//     que no tengan una ventaja real para este problema. La sintaxis
//     de indentación y las construcciones `if ... then` y `for ... do`
//     son suficientes para realizar una migración idiomática.
//
// 11. El comportamiento de wordFrequency permanece idéntico:
//     cada palabra se almacena como clave y su número de apariciones
//     como valor en el Map resultante.