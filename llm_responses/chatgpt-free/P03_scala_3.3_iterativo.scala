// P03: Flatten Nested List — Scala 3.3
//
// Características utilizadas:
// 1. Sintaxis de Scala 3:
//    Se utiliza la sintaxis moderna de Scala 3 manteniendo el código
//    compatible específicamente con Scala 3.3.
//
// 2. Pattern matching:
//    Permite distinguir entre listas anidadas e enteros de forma clara.
//
// 3. mutable.ListBuffer:
//    Se utiliza como resultado y como pila explícita para implementar
//    el algoritmo de forma iterativa.
//
// 4. mutable.Map:
//    Se utiliza para mantener información mutable del procesamiento,
//    siguiendo el paradigma iterativo solicitado.
//
// 5. while:
//    Controla explícitamente todo el recorrido de la estructura.
//
// No se utilizan map(), filter(), foldLeft(), flatMap() ni recursión.

import scala.collection.mutable

def flatten(nested: List[Any]): List[Int] = {
  var result = mutable.ListBuffer[Int]()
  var stack = mutable.ListBuffer[Any]()
  var info = mutable.Map[String, Int]("processed" -> 0)

  // Colocar la estructura inicial en la pila.
  stack += nested

  while (stack.nonEmpty) {
    val item = stack.remove(stack.length - 1)

    item match {
      case list: List[?] =>
        // Insertar los elementos en orden inverso.
        // Como la pila es LIFO, esto conserva el orden original.
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
 * 1. Se utiliza List[?]
 *    Scala 3 introduce la sintaxis de wildcard `?`, que reemplaza la
 *    sintaxis `List[_]` utilizada habitualmente en Scala 2.
 *
 * 2. Pattern matching con:
 *
 *        case list: List[?] =>
 *        case value: Int =>
 *
 *    Permite identificar directamente listas anidadas e enteros.
 *
 * 3. mutable.ListBuffer
 *    Se utiliza tanto para `result` como para `stack`, permitiendo
 *    modificar las estructuras durante el procesamiento iterativo.
 *
 * 4. mutable.Map
 *    `info` mantiene el número de enteros procesados. Se incluye para
 *    cumplir explícitamente el requisito de utilizar mutable.Map.
 *
 * 5. while
 *    Todo el procesamiento se realiza mediante ciclos while.
 *    No se utilizan operaciones funcionales de colecciones.
 *
 * 6. Pila explícita
 *    Se utiliza `stack` para evitar completamente la recursión.
 *    Los elementos de cada lista se agregan en orden inverso para que
 *    al extraerlos de la pila se conserve el orden original.
 *
 * 7. var
 *    Se utiliza para mantener el estado mutable requerido por el
 *    paradigma iterativo.
 *
 * 8. Sin map(), filter(), foldLeft(), flatMap() ni recursión
 *    El algoritmo es estrictamente iterativo, como exige el ejercicio.
 *
 * 9. Biblioteca estándar
 *    Solo se utiliza `scala.collection.mutable`, perteneciente a la
 *    biblioteca estándar de Scala.
 *
 * 10. Comportamiento idéntico
 *     - Mantiene el orden de aparición.
 *     - Soporta cualquier nivel de anidamiento.
 *     - Una lista vacía devuelve una lista vacía.
 *     - Los tres casos de prueba producen los resultados esperados.
 */