// P03: Flatten Nested List — Scala 3.6
//
// CARACTERÍSTICAS UTILIZADAS Y JUSTIFICACIÓN:
//
// 1. Sintaxis de indentación de Scala 3:
//    Mejora la legibilidad eliminando llaves innecesarias.
//
// 2. while ... do:
//    Expresa el ciclo iterativo con sintaxis moderna.
//
// 3. Pattern matching con List[?]:
//    Sustituye el comodín de tipos List[_] por List[?].
//
// 4. Tipado explícito en las estructuras mutables:
//    Facilita la lectura y el mantenimiento.
//
// 5. Compatibilidad con Scala 3.6:
//    Se conservan las estructuras y operaciones originales.
//    No se incorpora una característica exclusiva de Scala 3.6
//    porque ninguna mejora este algoritmo sin cambiar su lógica.
//
// Biblioteca estándar únicamente.

import scala.collection.mutable

object P03FlattenList_v36:

  def flatten(nested: Any): List[Int] =
    val result: mutable.ListBuffer[Int] =
      mutable.ListBuffer.empty[Int]

    val stack: mutable.Stack[Any] =
      mutable.Stack[Any](nested)

    while stack.nonEmpty do
      stack.pop() match
        case list: List[?] =>
          // Insertar en orden inverso para preservar
          // el orden original al extraer de la pila.
          list.reverse.foreach(stack.push)

        case n: Int =>
          result += n

        case _ =>
          // Ignorar elementos que no sean listas ni enteros.
          ()

    result.toList

  def main(args: Array[String]): Unit =

    // Test 1: Lista con elementos anidados
    assert(
      flatten(List(1, List(2, 3), List(4, List(5, 6)))) ==
        List(1, 2, 3, 4, 5, 6)
    )

    // Test 2: Lista vacía
    assert(
      flatten(List.empty) == List.empty
    )

    // Test 3: Lista plana
    assert(
      flatten(List(1, 2, 3)) == List(1, 2, 3)
    )

    println("All tests passed")


// CAMBIOS REALIZADOS — MIGRACIÓN A SCALA 3.6
//
// 1. Se renombró el objeto:
//      P03FlattenList_v213 -> P03FlattenList_v36
//    Motivo: identificar la versión de destino.
//
// 2. Se reemplazaron las llaves por indentación.
//    Motivo: utilizar la sintaxis moderna de Scala 3.
//
// 3. Se reemplazó:
//      while (stack.nonEmpty) { ... }
//    por:
//      while stack.nonEmpty do
//    Motivo: utilizar la sintaxis idiomática de Scala 3.
//
// 4. Se reemplazó:
//      case list: List[_] =>
//    por:
//      case list: List[?] =>
//    Motivo: utilizar el comodín de tipos de Scala 3.
//
// 5. Se conservaron mutable.Stack y mutable.ListBuffer.
//    Motivo: mantener las mismas estructuras de datos
//    y el mismo comportamiento del código original.
//
// 6. Se conservó list.reverse.foreach(stack.push).
//    Motivo: preservar el orden de los enteros mediante
//    el comportamiento LIFO de la pila.
//
// 7. Se mantuvo la firma original:
//      def flatten(nested: Any): List[Int]
//    Motivo: garantizar compatibilidad con las llamadas.
//
// 8. Se mantuvieron exactamente los tres casos assert.
//    Motivo: verificar los mismos escenarios originales.
//
// 9. Se añadieron anotaciones de tipos explícitas.
//    Motivo: mejorar la claridad del código.
//
// 10. No se introdujo recursión ni se cambió el algoritmo.
//     Motivo: conservar el comportamiento original.