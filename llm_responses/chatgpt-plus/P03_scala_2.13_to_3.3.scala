// P03: Flatten Nested List — Scala 3.3
// Migración desde Scala 2.13
//
// CARACTERÍSTICAS Y MEJORAS UTILIZADAS:
//
// 1. Sintaxis de indentación de Scala 3:
//    Elimina llaves innecesarias y mejora la legibilidad.
//
// 2. Pattern matching con List[?]:
//    Utiliza el comodín moderno ? en lugar de _.
//
// 3. Sintaxis while ... do:
//    Expresa claramente el recorrido iterativo.
//
// 4. mutable.Stack y mutable.ListBuffer:
//    Conservan las estructuras del algoritmo original.
//
// 5. Tipado explícito de result y stack:
//    Mejora la claridad y el análisis estático.
//
// NOTA:
// La sintaxis de indentación, List[?] y while ... do
// pertenecen a Scala 3 y son compatibles con Scala 3.3.
// No se introducen cambios innecesarios en el algoritmo.

import scala.collection.mutable

object P03FlattenList_v33:

  def flatten(nested: Any): List[Int] =
    val result: mutable.ListBuffer[Int] =
      mutable.ListBuffer.empty[Int]

    val stack: mutable.Stack[Any] =
      mutable.Stack[Any](nested)

    while stack.nonEmpty do
      stack.pop() match
        case list: List[?] =>
          // Agregar en orden inverso para preservar el orden.
          list.reverse.foreach(stack.push)

        case n: Int =>
          result += n

        case _ =>
          // Ignorar otros tipos, igual que en Scala 2.13.
          ()

    result.toList

  def main(args: Array[String]): Unit =

    // TC01: anidamiento básico
    assert(
      flatten(List(1, List(2, 3), List(4, List(5, 6)))) ==
        List(1, 2, 3, 4, 5, 6)
    )

    // TC02: lista vacía
    assert(flatten(List.empty) == List.empty)

    // TC03: lista plana
    assert(flatten(List(1, 2, 3)) == List(1, 2, 3))

    println("All tests passed")


// RESUMEN DE CAMBIOS — SCALA 2.13 A SCALA 3.3
//
// 1. Se sustituyeron las llaves por indentación.
//
//    Motivo: utilizar la sintaxis moderna de Scala 3,
//    que permite definir bloques mediante indentación.
//
// 2. Se reemplazó:
//
//       while (stack.nonEmpty) { ... }
//
//    por:
//
//       while stack.nonEmpty do
//         ...
//
//    Motivo: mejorar la legibilidad con sintaxis Scala 3.
//
// 3. Se reemplazó:
//
//       case list: List[_] =>
//
//    por:
//
//       case list: List[?] =>
//
//    Motivo: utilizar el comodín de tipos moderno.
//
// 4. Se conservaron mutable.Stack y mutable.ListBuffer.
//
//    Motivo: preservar las estructuras de datos y
//    el comportamiento iterativo del código original.
//
// 5. Se añadieron anotaciones de tipos explícitas.
//
//    Motivo: documentar claramente las estructuras
//    utilizadas y facilitar el mantenimiento.
//
// 6. Se conservó list.reverse.foreach(stack.push).
//
//    Motivo: mantener el orden de procesamiento
//    original mediante una pila LIFO.
//
// 7. Se conservó la firma:
//
//       def flatten(nested: Any): List[Int]
//
//    Motivo: mantener la compatibilidad con el
//    código original.
//
// 8. Se conservaron exactamente los tres assert.
//
//    Motivo: comprobar los mismos casos de prueba.
//
// 9. No se introdujeron Streams, recursión ni
//    transformaciones funcionales adicionales.
//
//    Motivo: mantener el algoritmo original.