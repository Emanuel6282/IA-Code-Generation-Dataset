// P03: Flatten Nested List — Scala 3.6
// Paradigma: Estrictamente funcional
//
// CARACTERÍSTICAS UTILIZADAS:
//
// 1. Sintaxis de indentación de Scala 3:
//    Permite escribir bloques sin llaves.
//
// 2. val y colecciones inmutables:
//    Evitan el estado mutable.
//
// 3. map(), filter(), flatMap() y foldLeft():
//    Permiten transformar, seleccionar y acumular elementos.
//
// 4. @tailrec:
//    Garantiza que la recursión sea de cola y que el
//    compilador pueda optimizarla.
//
// 5. Pattern matching con List[?]:
//    Distingue listas anidadas y enteros.
//
// 6. List y operador ::::
//    Permiten gestionar los elementos pendientes
//    sin modificar colecciones existentes.
//
// No utiliza var, mutable.*, for ni while.
// Biblioteca estándar únicamente.

import scala.annotation.tailrec

object P03Flatten_v36:

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
          // flatMap transforma cada elemento en una lista.
          val expanded: List[Any] =
            sublist.flatMap(item => List(item))

          // filter elimina las sublistas vacías.
          val filtered: List[Any] =
            expanded.filter(_ != Nil)

          // Continuar con los elementos de la sublista.
          process(filtered ::: rest, result)

        case (number: Int) :: rest =>
          // map transforma el entero.
          val mapped: List[Int] =
            List(number).map(identity)

          // foldLeft acumula los enteros en orden inverso.
          val updated: List[Int] =
            mapped.foldLeft(result): (acc, value) =>
              value :: acc

          process(rest, updated)

        case _ :: rest =>
          // Ignorar elementos que no sean listas ni enteros.
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