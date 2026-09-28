// P01: Word Frequency — Scala 3.3
//
// Características de Scala 3 utilizadas:
// 1. Sintaxis de indentación significativa:
//    Permite eliminar llaves innecesarias y hace más legible
//    la estructura de métodos, bloques y condicionales.
//
// 2. Sintaxis simplificada de métodos:
//    Scala 3 permite definir métodos con `=` y bloques basados
//    exclusivamente en indentación.
//
// 3. Sintaxis moderna de if:
//    Se utiliza `then`, característica de la sintaxis de Scala 3,
//    para hacer explícita la condición.
//
// Nota:
// No se introducen características como enums, union types,
// extension methods o match types porque no aportan una mejora
// real a este algoritmo y alterarían innecesariamente el código.
// El comportamiento se mantiene idéntico al código original.

import scala.collection.mutable

object P01WordFrequency_v33:

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


// CAMBIOS REALIZADOS:
//
// 1. Nombre de la clase:
//    P01WordFrequency_v213 -> P01WordFrequency_v33
//    para identificar correctamente la versión de Scala.
//
// 2. Se adoptó la sintaxis de indentación de Scala 3.
//    Por ejemplo:
//
//      object P01WordFrequency_v33:
//
//    reemplaza:
//
//      object P01WordFrequency_v213 {
//
//    Esto reduce el uso de llaves y es idiomático en Scala 3.
//
// 3. El método wordFrequency conserva exactamente el mismo nombre
//    y la misma firma lógica:
//
//      def wordFrequency(words: List[String]): Map[String, Int]
//
// 4. El algoritmo no fue modificado.
//    Continúa utilizando mutable.Map, for, contains, actualización
//    mediante result(word) y finalmente toMap.
//
// 5. El comportamiento es idéntico:
//    - ["the", "fox", "the"] -> Map("the" -> 2, "fox" -> 1)
//    - lista vacía -> Map.empty
//    - ["hello"] -> Map("hello" -> 1)
//
// 6. El bucle for utiliza la sintaxis de Scala 3:
//
//      for word <- words do
//
//    en lugar de la sintaxis con llaves de Scala 2.13.
//
// 7. El condicional utiliza:
//
//      if condicion then
//
//    que corresponde a la sintaxis moderna de Scala 3.
//
// 8. Los tres tests originales se conservan y solamente se adaptan
//    a la sintaxis de Scala 3.
//
// 9. No se agregan librerías externas.
//
// 10. No se cambia el paradigma del programa.
//     El código continúa siendo imperativo con mutable.Map y for,
//     ya que el objetivo de esta migración es cambiar de Scala 2.13
//     a Scala 3.3 manteniendo la misma lógica y comportamiento.