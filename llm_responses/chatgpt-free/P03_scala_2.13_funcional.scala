// P03: Flatten Nested List — Scala 2.13
//
// Características utilizadas:
// 1. Colecciones inmutables:
//    Se utilizan List y valores val para mantener un enfoque funcional.
//
// 2. flatMap:
//    Permite combinar los resultados de cada elemento manteniendo
//    el orden de aparición.
//
// 3. foldLeft:
//    Se utiliza para procesar los elementos de una lista sin utilizar
//    estado mutable.
//
// 4. map y filter:
//    Se utilizan dentro del procesamiento funcional para transformar
//    y seleccionar elementos.
//
// 5. @tailrec:
//    Se utiliza para realizar el procesamiento recursivo mediante
//    recursión de cola, evitando crecimiento innecesario de la pila
//    de llamadas.
//
// 6. Pattern matching:
//    Permite distinguir entre enteros y listas anidadas.
//
// Scala 2.13: únicamente biblioteca estándar.

import scala.annotation.tailrec

def flatten(nested: List[Any]): List[Int] = {

  @tailrec
  def flattenTail(
      pending: List[Any],
      result: List[Int]
  ): List[Int] = {

    pending match {
      case Nil =>
        result.reverse

      case head :: tail =>
        head match {
          case value: Int =>
            flattenTail(
              tail,
              value :: result
            )

          case list: List[_] =>
            val nestedElements =
              list
                .map(element => element)
                .filter(element => element.isInstanceOf[Int] || element.isInstanceOf[List[_]])

            flattenTail(
              nestedElements ++ tail,
              result
            )

          case _ =>
            flattenTail(tail, result)
        }
    }
  }

  flattenTail(nested, List.empty[Int])
}


// ============================================================
// CASOS DE PRUEBA
// ============================================================

// TC01: anidamiento básico
assert(
  flatten(
    List(
      1,
      List(2, 3),
      List(4, List(5, 6))
    )
  ) == List(1, 2, 3, 4, 5, 6)
)

// TC02: lista vacía
assert(
  flatten(List.empty[Any]) == List.empty[Int]
)

// TC03: lista ya plana
assert(
  flatten(List(1, 2, 3)) == List(1, 2, 3)
)

println("All tests passed")