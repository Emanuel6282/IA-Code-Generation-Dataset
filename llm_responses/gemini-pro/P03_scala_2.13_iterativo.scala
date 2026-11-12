// CARACTERISTICAS DE SCALA 2.13 UTILIZADAS Y POR QUE:
// 1. scala.collection.mutable.ArrayDeque: Introducido en la reescritura de colecciones de Scala 2.13 
//    como la estructura de datos por excelencia para colas y pilas mutables (Double-ended queue), 
//    reemplazando definitivamente a la clase obsoleta `mutable.Stack`. Provee mutaciones ultra rápidas 
//    amortizadas como `removeHead()` y `prependAll()`.
// 2. Jerarquía Strict y ListBuffer: En 2.13, la compatibilidad de `ListBuffer` hacia la creación de 
//    listas inmutables (`.toList`) fue optimizada para no copiar la estructura interna si ya no se mutará,
//    haciendo la conversión final en tiempo O(1).
// 3. Pattern Matching en Bucle: El reemplazo seguro y escalable para `instanceof` dentro del ciclo `while`.

import scala.collection.mutable.{ArrayDeque, ListBuffer}

object FlattenIterative {

  /**
   * Toma una lista arbitrariamente anidada (representada como List[Any] en Scala)
   * y retorna una lista plana unidimensional de enteros.
   *
   * Implementación estrictamente iterativa: sin recursión, sin map/flatMap,
   * utilizando un bucle while y estructuras de datos mutables.
   */
  def flatten(nestedList: List[Any]): List[Int] = {
    // result acumulará los enteros extraídos (mutable, alto rendimiento al final)
    val result = ListBuffer.empty[Int]
    
    // stack funciona como nuestra pila explícita local (mutable, introducida en 2.13)
    val stack = ArrayDeque.from(nestedList)

    while (stack.nonEmpty) {
      // Extraemos el primer elemento disponible de la pila
      val item = stack.removeHead()
      
      item match {
        case number: Int =>
          // Si es un entero escalar, lo anexamos al resultado
          result += number
          
        case list: List[_] =>
          // Si es una sublista, inyectamos sus elementos al frente de la pila.
          // ArrayDeque.prependAll inyecta todos manteniendo el orden correcto
          // para procesarlos consecutivamente de izquierda a derecha en los próximos ciclos.
          stack.prependAll(list)
          
        case _ =>
          // Ignora nulos o tipos no reconocidos de forma segura sin interrumpir el bucle
      }
    }

    // O(1) de conversión a lista inmutable gracias al diseño de Scala 2.13
    result.toList
  }

  def main(args: Array[String]): Unit = {
    // ==========================================
    // CASOS DE PRUEBA
    // ==========================================

    // 1. Caso base del ejemplo: múltiples niveles de anidamiento
    val nested1 = List(1, List(2, 3), List(4, List(5, 6)))
    val expected1 = List(1, 2, 3, 4, 5, 6)
    assert(flatten(nested1) == expected1, "Error: Falló el caso base del ejemplo")

    // 2. Caso límite: lista completamente vacía
    val nested2 = List.empty[Any]
    val expected2 = List.empty[Int]
    assert(flatten(nested2) == expected2, "Error: Una lista vacía debería retornar una lista vacía")

    // 3. Caso extremo: anidamiento profundo con listas vacías en el medio
    val nested3 = List(1, List(), List(List(List(3))))
    val expected3 = List(1, 3)
    assert(flatten(nested3) == expected3, "Error: Falló al procesar listas vacías internas")

    println("Todos los tests iterativos en Scala 2.13 pasaron correctamente.")
  }
}