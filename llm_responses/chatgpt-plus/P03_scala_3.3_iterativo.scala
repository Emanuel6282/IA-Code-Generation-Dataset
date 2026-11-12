// P03: Flatten Nested List — Scala 3.3
// Paradigma: Estrictamente iterativo
//
// Características utilizadas:
// 1. Sintaxis de indentación de Scala 3.
// 2. Pattern matching para distinguir listas y enteros.
// 3. mutable.ListBuffer para construir el resultado.
// 4. var y while para mantener un recorrido iterativo.
// 5. List inmutable como pila explícita.
//
// No utiliza map(), filter(), foldLeft() ni recursión.
// Biblioteca estándar únicamente.

import scala.collection.mutable.ListBuffer

object P03Flatten_v33:

  def flatten(nested: List[Any]): List[Int] =
    val result = ListBuffer.empty[Int]

    // Pila de elementos pendientes.
    var stack: List[Any] = nested

    while stack.nonEmpty do
      val item = stack.head
      stack = stack.tail

      item match
        case sublist: List[?] =>
          // Procesar la sublista antes de los elementos restantes.
          // La concatenación conserva el orden original.
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