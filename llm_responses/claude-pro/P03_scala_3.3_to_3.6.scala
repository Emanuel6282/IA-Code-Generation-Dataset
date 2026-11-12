// P03: Flatten Nested List — migrado a Scala 3.6
//
// CARACTERÍSTICAS USADAS Y POR QUÉ
// 1. Pattern matching recursivo con @tailrec en lugar de while + mutable.Stack +
//    ListBuffer: era el objetivo declarado del código base ("forzar pattern matching
//    recursivo en el refactor"). La recursión es de cola, así que el compilador la
//    convierte en un salto: se conserva la ventaja del original de funcionar con
//    cualquier nivel de anidamiento sin desbordar la pila, ahora sin estado mutable.
// 2. Sintaxis sin llaves (indentación significativa, Scala 3): elimina ruido visual.
// 3. Comodín de tipo `List[?]` (Scala 3): sustituye a `List[_]`, que en Scala 3 queda
//    reservado para las lambdas abreviadas y se desaconseja como comodín.
// 4. Definiciones de nivel superior y @main (Scala 3): flatten no necesita un `object`
//    contenedor y el punto de entrada no necesita `def main(args: Array[String])`.
//
// Nota sobre las novedades de Scala 3.6 (verificado con el compilador 3.6.4):
// - Estable: context bounds con nombre (`[A: ClassTag as tag]`, SIP-64). Permitirían
//   una versión genérica para cualquier tipo de hoja, pero cambiarían la firma
//   pública (List[Int] pasaría a List[A] con un ClassTag implícito), lo que rompe la
//   compatibilidad con quien ya llama a la función. Como esta migración debe
//   conservar el comportamiento, no se aplica.
// - Experimentales en 3.6 (requieren -experimental): tuplas con nombre y
//   runtimeChecked. No se usan.
// Tampoco se usa un tipo unión en el parámetro (`Int | List[?]`): el original acepta
// `Any` e ignora los demás tipos, y ese contrato se conserva.

import scala.annotation.tailrec

def flatten(nested: Any): List[Int] =
  @tailrec
  def loop(pending: List[Any], acc: List[Int]): List[Int] =
    pending match
      case Nil                     => acc.reverse              // se acumuló al revés
      case (list: List[?]) :: rest => loop(list ::: rest, acc) // sublista -> su contenido, en orden
      case (n: Int) :: rest        => loop(rest, n :: acc)
      case _ :: rest               => loop(rest, acc)          // ignorar otros tipos

  loop(List(nested), Nil)

@main def P03FlattenList_v36(): Unit =
  assert(flatten(List(1, List(2, 3), List(4, List(5, 6)))) == List(1, 2, 3, 4, 5, 6))
  assert(flatten(List.empty) == List.empty)
  assert(flatten(List(1, 2, 3)) == List(1, 2, 3))
  println("All tests passed")

// CAMBIOS REALIZADOS Y POR QUÉ SON IDIOMÁTICOS EN SCALA 3.6
// 1. while + mutable.Stack + mutable.ListBuffer -> función interna `loop` con
//    @tailrec y pattern matching sobre (cabeza :: resto): los pendientes y el
//    acumulador pasan a ser listas inmutables que viajan como parámetros. Se elimina
//    el import de scala.collection.mutable.
// 2. `list.reverse.foreach(stack.push)` -> `list ::: rest`: en el original había que
//    invertir la sublista para que la pila la devolviera en orden; con una lista de
//    pendientes basta con anteponerla, sin inversión ni efectos secundarios.
// 3. El acumulador se construye al revés (añadir al principio de una List es O(1)) y
//    se invierte una sola vez al final: coste lineal, igual que el original.
// 4. Comportamiento idéntico, incluidos los casos límite del original (verificado con
//    miles de entradas aleatorias): un Int suelto devuelve List(n), los elementos que no
//    son Int ni List (Strings, Doubles, null...) se ignoran, y el anidamiento muy
//    profundo funciona sin StackOverflowError gracias a @tailrec.
// 5. `case list: List[_]` -> `case (list: List[?]) :: rest`: comodín de Scala 3.
// 6. `object P03FlattenList_v213 { ... }` -> definiciones de nivel superior, y
//    `def main(args: Array[String])` -> `@main def P03FlattenList_v36()`.
// 7. Llaves -> sintaxis por indentación, el estilo recomendado en Scala 3.
// 8. Se actualizó la cabecera: el código base decía "Scala 2.13".
// 9. Los tres asserts y el mensaje "All tests passed" no cambian (solo se añadieron
//    espacios en las listas, según las convenciones de estilo).