// P03: Flatten Nested List — Scala 3.3
//
// Características utilizadas:
// 1. Wildcard types de Scala 3: List[?]
//    Reemplaza la sintaxis List[_] utilizada en Scala 2 y mejora la
//    legibilidad al expresar una lista de tipo desconocido.
//
// 2. Sintaxis moderna de Scala 3:
//    Se utiliza la sintaxis de Scala 3.3 manteniendo el mismo diseño
//    imperativo del código original.
//
// 3. Pattern matching:
//    Se mantiene para distinguir entre listas anidadas, enteros y
//    otros tipos.
//
// 4. mutable.Stack y mutable.ListBuffer:
//    Se mantienen porque forman parte de la lógica iterativa original.
//    No se introduce una transformación funcional que altere el enfoque.
//
// 5. while:
//    Se mantiene para conservar el algoritmo iterativo y su comportamiento.
//
// No se utilizan características funcionales como map(), filter(),
// foldLeft() o flatMap(), ya que el código original utiliza una pila
// mutable explícita.

import scala.collection.mutable

object P03FlattenList_v33 {

  def flatten(nested: Any): List[Int] = {
    val result = mutable.ListBuffer[Int]()
    val stack = mutable.Stack[Any](nested)

    while (stack.nonEmpty) {
      stack.pop() match {
        case list: List[?] =>
          // Agregar en orden inverso para preservar el orden original.
          list.reverse.foreach(stack.push)

        case n: Int =>
          result += n

        case _ =>
          // Ignorar otros tipos.
      }
    }

    result.toList
  }

  def main(args: Array[String]): Unit = {
    assert(
      flatten(List(1, List(2, 3), List(4, List(5, 6)))) ==
        List(1, 2, 3, 4, 5, 6)
    )

    assert(
      flatten(List.empty) == List.empty
    )

    assert(
      flatten(List(1, 2, 3)) == List(1, 2, 3)
    )

    println("All tests passed")
  }
}

/*
 * CAMBIOS REALIZADOS:
 *
 * 1. List[_] -> List[?]
 *
 *    En Scala 2.13 se utilizaba:
 *
 *        case list: List[_] =>
 *
 *    En Scala 3.3 se utiliza:
 *
 *        case list: List[?] =>
 *
 *    List[?] es la sintaxis de wildcard de Scala 3 y resulta más clara
 *    y moderna para representar una lista cuyo tipo de elemento es
 *    desconocido.
 *
 * 2. Se mantiene el pattern matching
 *
 *    La estructura:
 *
 *        case list: List[?] =>
 *        case n: Int =>
 *        case _ =>
 *
 *    permite conservar exactamente la lógica del código original.
 *
 * 3. Se mantiene mutable.Stack
 *
 *    La pila explícita es fundamental para el enfoque iterativo utilizado
 *    por el código original. Se conserva para evitar introducir recursión
 *    o modificar el algoritmo.
 *
 * 4. Se mantiene mutable.ListBuffer
 *
 *    result continúa utilizando ListBuffer para agregar los enteros
 *    encontrados durante el recorrido.
 *
 * 5. Se mantiene while
 *
 *    El procesamiento continúa siendo iterativo:
 *
 *        while (stack.nonEmpty)
 *
 *    No se introduce recursión ni una transformación mediante Streams
 *    o colecciones funcionales.
 *
 * 6. Se mantiene el procesamiento inverso
 *
 *    Los elementos de cada lista se agregan a la pila mediante:
 *
 *        list.reverse.foreach(stack.push)
 *
 *    Esto conserva el orden original porque la pila utiliza el principio
 *    LIFO (Last In, First Out).
 *
 * 7. Se mantiene exactamente la función flatten
 *
 *    La firma continúa siendo:
 *
 *        def flatten(nested: Any): List[Int]
 *
 *    Por lo tanto, el nombre y el comportamiento de la función permanecen
 *    idénticos.
 *
 * 8. Se conservan los tres tests
 *
 *    TC01: lista con varios niveles de anidamiento.
 *    TC02: lista vacía.
 *    TC03: lista sin anidamiento.
 *
 * 9. Biblioteca estándar
 *
 *    Solo se utiliza scala.collection.mutable, perteneciente a la
 *    biblioteca estándar de Scala.
 *
 * 10. Características específicas de Scala 3.3
 *
 *    La mejora más relevante para este código es la sintaxis List[?].
 *    No se fuerza el uso de otras características de Scala 3 que no
 *    aportarían una mejora real a este algoritmo iterativo.
 */