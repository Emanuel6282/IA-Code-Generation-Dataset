// P03: Flatten Nested List — Scala 2.13
//
// Características utilizadas:
// 1. mutable.ListBuffer:
//    Permite construir la lista resultado de forma incremental.
//
// 2. mutable.Map:
//    Se utiliza para mantener información mutable del procesamiento,
//    siguiendo el paradigma iterativo solicitado.
//
// 3. while:
//    Controla explícitamente el recorrido de la estructura sin utilizar
//    funciones funcionales como map, filter o foldLeft.
//
// 4. var:
//    Permite mantener el estado mutable necesario para el algoritmo iterativo.
//
// 5. Pila explícita:
//    Se utiliza una ListBuffer como pila para procesar cualquier nivel de
//    anidamiento sin utilizar recursión.
//
// Scala 2.13: se utiliza únicamente la biblioteca estándar.

import scala.collection.mutable

def flatten(nested: List[Any]): List[Int] = {
  val result = mutable.ListBuffer[Int]()
  val stack = mutable.ListBuffer[Any]()
  val info = mutable.Map[String, Int]("processed" -> 0)

  // Colocar la lista inicial en la pila
  stack += nested

  while (stack.nonEmpty) {
    val item = stack.remove(stack.length - 1)

    item match {
      case list: List[?] =>
        // Agregar los elementos en orden inverso para conservar
        // el orden original al procesarlos desde la pila.
        var i = list.length - 1

        while (i >= 0) {
          stack += list(i)
          i -= 1
        }

      case value: Int =>
        result += value
        info("processed") = info("processed") + 1

      case _ =>
        // No se procesa ningún otro tipo.
    }
  }

  result.toList
}


// ============================================================
// CASOS DE PRUEBA
// ============================================================

// TC01: anidamiento básico
assert(
  flatten(List(
    1,
    List(2, 3),
    List(4, List(5, 6))
  )) == List(1, 2, 3, 4, 5, 6)
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
 * 1. Se utiliza mutable.ListBuffer para result y stack.
 *    Esto permite agregar y eliminar elementos eficientemente durante
 *    el procesamiento iterativo.
 *
 * 2. Se utiliza mutable.Map para almacenar el contador "processed".
 *    El mapa mantiene estado mutable sin introducir una solución funcional.
 *
 * 3. Se utiliza while para todos los recorridos.
 *    No se utilizan map(), filter(), foldLeft() ni otras operaciones
 *    funcionales de colección.
 *
 * 4. Se utiliza var para el índice i.
 *    El índice se modifica explícitamente durante el recorrido inverso.
 *
 * 5. Se utiliza una pila explícita.
 *    Cuando se encuentra una lista anidada, sus elementos se agregan
 *    a la pila desde el último hasta el primero.
 *
 *    Ejemplo:
 *        List(1, List(2, 3), 4)
 *
 *    se procesa preservando:
 *        1, 2, 3, 4
 *
 * 6. No se utiliza recursión.
 *    Por lo tanto, el algoritmo depende de la memoria de la pila explícita
 *    y no de la pila de llamadas del programa.
 *
 * 7. Pattern matching:
 *    Se utiliza para distinguir entre una lista anidada y un entero:
 *
 *        case list: List[?] => ...
 *        case value: Int    => ...
 *
 *    Esto permite procesar cada tipo de elemento de forma clara.
 *
 * 8. El nombre de la función permanece exactamente "flatten".
 *
 * 9. El comportamiento es idéntico:
 *    - conserva el orden de aparición;
 *    - soporta cualquier nivel de anidamiento;
 *    - una lista vacía produce una lista vacía;
 *    - los tres casos de prueba producen los mismos resultados.
 *
 * 10. Scala 2.13:
 *    ListBuffer y Map pertenecen a scala.collection.mutable y forman parte
 *    de la biblioteca estándar de Scala 2.13.
 */