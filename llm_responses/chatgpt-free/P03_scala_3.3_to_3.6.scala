// P03: Flatten Nested List — Scala 3.6
//
// Características utilizadas:
// 1. Wildcard type de Scala 3: List[?]
//    Reemplaza List[_] de Scala 2 y expresa de forma más clara una lista
//    cuyo tipo de elemento es desconocido.
//
// 2. Pattern matching de Scala 3:
//    Permite distinguir entre listas anidadas, enteros y otros tipos
//    de forma clara y concisa.
//
// 3. Sintaxis moderna de Scala 3:
//    Se utiliza la sintaxis de Scala 3.6 manteniendo el algoritmo
//    iterativo original.
//
// 4. mutable.Stack y mutable.ListBuffer:
//    Se mantienen porque son parte fundamental del algoritmo original
//    y permiten procesar la estructura de manera iterativa.
//
// 5. while:
//    Se conserva para evitar recursión y mantener el comportamiento
//    iterativo solicitado.
//
// Nota:
// Scala 3.6 no introduce una característica nueva específica para este
// algoritmo que mejore claramente el rendimiento sin cambiar su diseño.
// Por ello, no se fuerza el uso de características innecesarias.

import scala.collection.mutable

object P03FlattenList_v36 {

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
    // TC01: anidamiento básico
    assert(
      flatten(List(1, List(2, 3), List(4, List(5, 6)))) ==
        List(1, 2, 3, 4, 5, 6)
    )

    // TC02: lista vacía
    assert(
      flatten(List.empty) == List.empty
    )

    // TC03: lista ya plana
    assert(
      flatten(List(1, 2, 3)) == List(1, 2, 3)
    )

    println("All tests passed")
  }
}

/*
 * CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN SCALA 3.6:
 *
 * 1. List[_] -> List[?]
 *
 *    Código original:
 *
 *        case list: List[_] =>
 *
 *    Scala 3.6:
 *
 *        case list: List[?] =>
 *
 *    Scala 3 utiliza ? como wildcard type. Es la sintaxis moderna
 *    para representar una lista cuyo tipo de elemento es desconocido.
 *
 * 2. Pattern matching
 *
 *    Se mantiene:
 *
 *        case list: List[?] =>
 *        case n: Int =>
 *        case _ =>
 *
 *    Esto permite separar claramente los tres casos:
 *    - lista anidada;
 *    - entero;
 *    - cualquier otro tipo.
 *
 * 3. Se mantiene mutable.Stack
 *
 *    La pila explícita permite recorrer cualquier nivel de anidamiento
 *    sin utilizar recursión.
 *
 *    Esto conserva exactamente la estrategia del código original.
 *
 * 4. Se mantiene mutable.ListBuffer
 *
 *    result se construye incrementalmente mientras se encuentran
 *    los enteros.
 *
 * 5. Se mantiene while
 *
 *    El procesamiento continúa siendo iterativo mediante:
 *
 *        while (stack.nonEmpty)
 *
 *    No se introduce recursión ni una solución funcional que cambie
 *    la estructura del algoritmo.
 *
 * 6. Se mantiene el procesamiento inverso
 *
 *    Los elementos se introducen en la pila utilizando:
 *
 *        list.reverse.foreach(stack.push)
 *
 *    Como Stack sigue el principio LIFO, introducirlos en orden inverso
 *    hace que posteriormente sean procesados en su orden original.
 *
 * 7. Se mantiene exactamente la función flatten
 *
 *    La firma continúa siendo:
 *
 *        def flatten(nested: Any): List[Int]
 *
 *    El nombre de la función no cambia.
 *
 * 8. Se conserva el comportamiento
 *
 *    Ejemplo:
 *
 *        List(1, List(2, 3), List(4, List(5, 6)))
 *
 *    produce:
 *
 *        List(1, 2, 3, 4, 5, 6)
 *
 *    Las listas vacías y las listas ya planas también mantienen
 *    exactamente el mismo resultado.
 *
 * 9. Se conservan los tres casos de prueba
 *
 *    TC01: anidamiento.
 *    TC02: lista vacía.
 *    TC03: lista plana.
 *
 * 10. Biblioteca estándar
 *
 *    Solo se utiliza scala.collection.mutable, que forma parte
 *    de la biblioteca estándar de Scala.
 *
 * 11. Características de Scala 3.6
 *
 *    No se fuerza el uso de una característica exclusiva de Scala 3.6
 *    cuando no aporta una mejora real al algoritmo. La migración utiliza
 *    la sintaxis moderna de Scala 3 y conserva la solución iterativa,
 *    evitando cambios artificiales en la lógica.
 */