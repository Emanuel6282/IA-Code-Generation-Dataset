// P03: Flatten Nested List — Scala 3.3
// Paradigma: Estrictamente funcional
//
// Características utilizadas:
// 1. Sintaxis de indentación de Scala 3.
// 2. val y List inmutables para evitar estado mutable.
// 3. map() para transformar elementos.
// 4. filter() para seleccionar valores.
// 5. flatMap() para expandir colecciones.
// 6. foldLeft() para acumular resultados.
// 7. @tailrec para garantizar recursión de cola.
// 8. Pattern matching con List[?].
//
// Sin var, mutable.*, for ni while.
// Biblioteca estándar únicamente.

import scala.annotation.tailrec

object P03Flatten_v33:

  def flatten(nested: List[Any]): List[Int] =

    @tailrec
    def process(
        pending: List[Any],
        result: List[Int]
    ): List[Int] =

      pending match

        case Nil =>
          result.reverse

        case (sublist: List[?]) :: rest =>
          // flatMap expande los elementos de la sublista.
          // filter descarta sublistas vacías.
          val expanded: List[Any] =
            sublist
              .flatMap(item => List(item))
              .filter(_ != Nil)

          process(expanded ::: rest, result)

        case (number: Int) :: rest =>
          // map transforma el entero en una colección.
          val mapped: List[Int] =
            List(number).map(identity)

          // foldLeft incorpora los valores al acumulador.
          val updated: List[Int] =
            mapped.foldLeft(result): (acc, value) =>
              value :: acc

          process(rest, updated)

        case _ :: rest =>
          process(rest, result)

    process(nested, Nil)

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