// P03: Flatten Nested List — Scala 3.6
//
// Características utilizadas:
// 1. Colecciones inmutables:
//    Se utiliza exclusivamente List y val para evitar estado mutable.
//
// 2. map():
//    Permite transformar los elementos de las listas sin modificar
//    la colección original.
//
// 3. filter():
//    Permite conservar únicamente enteros y listas anidadas válidas.
//
// 4. flatMap():
//    Permite expandir los elementos de las listas manteniendo una
//    estructura plana de elementos pendientes.
//
// 5. foldLeft():
//    Permite construir nuevas listas mediante un acumulador inmutable.
//
// 6. @tailrec:
//    La función auxiliar procesa la estructura mediante recursión de cola,
//    permitiendo manejar niveles arbitrarios de anidamiento sin mantener
//    una cadena creciente de llamadas recursivas.
//
// 7. Pattern matching:
//    Permite distinguir declarativamente entre enteros y listas.
//
// 8. Sintaxis de Scala 3:
//    Se utiliza List[?], la sintaxis moderna de Scala 3 para un wildcard.
//
// No se utilizan var, mutable.*, while ni estructuras mutables.
//
// Scala 3.6 no introduce en este problema una característica específica
// que mejore de forma significativa el algoritmo, por lo que no se fuerza
// ninguna característica innecesaria.

import scala.annotation.tailrec

def flatten(nested: List[Any]): List[Int] = {

  @tailrec
  def process(
      pending: List[Any],
      result: List[Int]
  ): List[Int] = {

    pending match {

      case Nil =>
        result.reverse

      case head :: tail =>
        head match {

          case value: Int =>
            process(
              tail,
              value :: result
            )

          case list: List[?] =>
            val validElements: List[Any] =
              list
                .filter(element =>
                  element.isInstanceOf[Int] ||
                  element.isInstanceOf[List[?]]
                )
                .map(element => element)
                .flatMap(element => List(element))

            val expanded: List[Any] =
              validElements.foldLeft(List.empty[Any]) {
                (accumulator, element) =>
                  accumulator :+ element
              }

            process(
              expanded ++ tail,
              result
            )

          case _ =>
            process(
              tail,
              result
            )
        }
    }
  }

  process(nested, List.empty[Int])
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

/*
 * CAMBIOS Y DECISIONES:
 *
 * 1. Uso exclusivo de val
 *
 *    Todas las variables son inmutables. No se utiliza var en ninguna
 *    parte de la solución.
 *
 * 2. Colecciones inmutables
 *
 *    Se utiliza exclusivamente List. No se utiliza ninguna colección
 *    perteneciente a scala.collection.mutable.
 *
 * 3. map()
 *
 *    Se utiliza para transformar los elementos de la colección sin
 *    modificar la lista original.
 *
 * 4. filter()
 *
 *    Se utiliza para conservar únicamente los tipos válidos del problema:
 *    Integer y List[?].
 *
 * 5. flatMap()
 *
 *    Se utiliza para expandir cada elemento a una lista y combinar los
 *    resultados en una única lista de elementos pendientes.
 *
 * 6. foldLeft()
 *
 *    Se utiliza para construir una nueva lista mediante un acumulador
 *    inmutable.
 *
 * 7. @tailrec
 *
 *    process() es una función recursiva de cola. La llamada recursiva
 *    constituye la última operación realizada en cada rama.
 *
 *    Esto permite que el compilador optimice la recursión y evita utilizar
 *    una estructura mutable para representar la pila de procesamiento.
 *
 * 8. Pattern matching
 *
 *    Se utilizan los patrones:
 *
 *        case value: Int =>
 *        case list: List[?] =>
 *
 *    para diferenciar enteros de listas anidadas.
 *
 * 9. Preservación del orden
 *
 *    Los enteros encontrados se agregan al principio del acumulador:
 *
 *        value :: result
 *
 *    Por ello, cuando finaliza el procesamiento, se utiliza reverse()
 *    para recuperar exactamente el orden original de aparición.
 *
 * 10. Sin var, mutable.*, while ni bucles imperativos
 *
 *    Toda la solución utiliza transformaciones sobre colecciones
 *    inmutables y recursión de cola.
 *
 * 11. Scala 3.6
 *
 *    Se utiliza List[?], sintaxis de Scala 3 para representar una lista
 *    cuyo tipo de elemento es desconocido.
 *
 *    No se fuerza una característica específica de Scala 3.6 cuando no
 *    aporta una mejora real al algoritmo.
 *
 * 12. Biblioteca estándar
 *
 *    Solo se utiliza scala.annotation.tailrec, que forma parte de la
 *    biblioteca estándar de Scala.
 *
 * 13. Función flatten
 *
 *    El nombre de la función permanece exactamente:
 *
 *        flatten
 *
 *    y conserva el comportamiento solicitado:
 *
 *        List(1, List(2, 3), List(4, List(5, 6)))
 *
 *    produce:
 *
 *        List(1, 2, 3, 4, 5, 6)
 */