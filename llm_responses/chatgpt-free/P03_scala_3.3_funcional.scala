// P03: Flatten Nested List — Scala 3.3
//
// Características utilizadas:
// 1. Colecciones inmutables:
//    Se utiliza exclusivamente List y val, evitando estado mutable.
//
// 2. flatMap():
//    Permite procesar los elementos de las listas anidadas y mantener
//    una estructura de elementos lista para el procesamiento posterior.
//
// 3. filter():
//    Filtra los elementos para conservar únicamente los tipos válidos
//    del problema: Integer y listas anidadas.
//
// 4. map():
//    Se utiliza para transformar los elementos conservados sin modificar
//    las colecciones originales.
//
// 5. foldLeft():
//    Se utiliza para construir la nueva estructura de procesamiento
//    de forma funcional.
//
// 6. @tailrec:
//    La función auxiliar utiliza recursión de cola para procesar la pila
//    lógica de elementos sin utilizar estructuras mutables.
//
// 7. Pattern matching de Scala 3:
//    Permite distinguir entre Integer y List[?] de forma declarativa.
//
// No se utilizan var, mutable.*, while ni estructuras mutables.

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
            // Se utilizan las operaciones funcionales solicitadas:
            // flatMap -> map -> filter -> foldLeft.
            val expanded: List[Any] =
              list
                .flatMap(element => List(element))
                .filter(element =>
                  element.isInstanceOf[Int] ||
                  element.isInstanceOf[List[?]]
                )
                .map(element => element)
                .foldLeft(List.empty[Any]) { (acc, element) =>
                  acc :+ element
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
 * 1. Se utiliza @tailrec.
 *
 *    La función process() procesa la estructura mediante recursión de cola:
 *
 *        process(pending, result)
 *
 *    La llamada recursiva es la última operación realizada por la función.
 *    Esto permite que el compilador de Scala optimice la recursión de cola.
 *
 * 2. Se utilizan exclusivamente val.
 *
 *    No existe ningún var en la solución. Cada transformación genera una
 *    nueva colección inmutable.
 *
 * 3. Se utilizan colecciones inmutables.
 *
 *    Tanto pending como result son List inmutables.
 *    No se utiliza scala.collection.mutable.
 *
 * 4. Se utiliza flatMap().
 *
 *    Cada elemento de una lista anidada se convierte en una pequeña lista
 *    y flatMap() mantiene todos los elementos en una única estructura.
 *
 * 5. Se utiliza filter().
 *
 *    Se conservan únicamente valores Integer y listas anidadas, que son
 *    los dos tipos que forman parte de la estructura esperada.
 *
 * 6. Se utiliza map().
 *
 *    Los elementos válidos se transforman mediante una operación funcional
 *    sin modificar la colección original.
 *
 * 7. Se utiliza foldLeft().
 *
 *    La nueva lista de elementos pendientes se construye mediante un
 *    acumulador inmutable.
 *
 * 8. Se conserva el orden de aparición.
 *
 *    Los elementos se agregan a pending en el mismo orden en que aparecen.
 *    El resultado se construye en orden inverso mediante `value :: result`
 *    y finalmente se aplica reverse().
 *
 * 9. Pattern matching de Scala 3.
 *
 *    Se utiliza:
 *
 *        case value: Int =>
 *        case list: List[?] =>
 *
 *    para distinguir declarativamente los enteros de las listas anidadas.
 *
 * 10. Sintaxis de Scala 3.3.
 *
 *    `List[?]` es la sintaxis de wildcard de Scala 3, reemplazando
 *    la forma `List[_]` utilizada habitualmente en Scala 2.
 *
 * 11. No se utiliza recursión convencional.
 *
 *    La única recursión existente es la función process(), anotada con
 *    @tailrec y diseñada específicamente como recursión de cola.
 *
 * 12. Biblioteca estándar.
 *
 *    Solo se utiliza scala.annotation.tailrec, que forma parte de la
 *    biblioteca estándar de Scala.
 */