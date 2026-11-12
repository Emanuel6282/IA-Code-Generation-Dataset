// P03: Flatten Nested List — Scala 3.6
// Paradigma: Estrictamente iterativo
//
// Características utilizadas:
// 1. Sintaxis de indentación de Scala 3.
// 2. while ... do para el recorrido iterativo.
// 3. Pattern matching con el comodín List[?].
// 4. var para actualizar el estado de la pila.
// 5. mutable.ListBuffer para construir el resultado.
// 6. List inmutable como pila explícita.
//
// No utiliza map(), filter(), foldLeft() ni recursión.
// Biblioteca estándar únicamente.

import scala.collection.mutable.ListBuffer

object P03Flatten_v36:

  def flatten(nested: List[Any]): List[Int] =
    val result = ListBuffer.empty[Int]

    // Pila de elementos pendientes de procesar.
    var stack: List[Any] = nested

    while stack.nonEmpty do
      val item = stack.head
      stack = stack.tail

      item match
        case sublist: List[?] =>
          // Colocar la sublista delante de los pendientes.
          // Se conserva el orden original de aparición.
          stack = sublist ::: stack

        case number: Int =>
          result += number

        case _ =>
          // Ignorar elementos que no sean listas ni enteros.
          ()

    result.toList

  def main(args: Array[String]): Unit =

    // Test 1: Lista con varios niveles de anidamiento
    assert(
      flatten(List(1, List(2, 3), List(4, List(5, 6)))) ==
        List(1, 2, 3, 4, 5, 6)
    )

    // Test 2: Lista vacía
    assert(
      flatten(List.empty[Any]) == List.empty[Int]
    )

    // Test 3: Lista con anidamiento profundo
    assert(
      flatten(
        List(
          1,
          List(List(2)),
          List.empty[Int],
          List(3, List(4, List(5)))
        )
      ) == List(1, 2, 3, 4, 5)
    )

    println("All tests passed")