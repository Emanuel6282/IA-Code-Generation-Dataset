// CARACTERISTICAS DE SCALA 2.13 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. @tailrec (Tail Recursion): En el paradigma funcional puro evitamos los bucles (while/for). La recursividad natural puede causar StackOverflow en anidamientos profundos. Para solucionarlo, usamos recursividad de cola (@tailrec), garantizando que el compilador de Scala optimice la función en un bucle de bajo nivel seguro para la memoria.
// 2. Inmutabilidad Absoluta y Pattern Matching: No existe ni un solo `var` ni colecciones mutables. Toda la transformación se realiza deconstruyendo listas inmutables mediante Pattern Matching (`head :: tail`) y creando nuevas listas (`n :: acc`).
// 3. Optimización de List en 2.13: La reescritura de colecciones de Scala 2.13 hace que operaciones como `:::` (concatenación) y `.reverse` sobre la colección estricta `List` sean extremadamente rápidas y seguras.

import scala.annotation.tailrec

object FlattenFunctional {

  /**
   * Toma una lista arbitrariamente anidada (representada como List[Any])
   * y retorna una lista plana unidimensional de enteros.
   *
   * Implementación estrictamente funcional: sin estado mutable, 
   * utilizando recursividad de cola (@tailrec) segura contra desbordamientos.
   */
  def flatten(nestedList: List[Any]): List[Int] = {
    
    // Función anidada recursiva de cola.
    // 'todo' actúa como una pila funcional inmutable de tareas pendientes.
    // 'acc' acumula los resultados temporalmente en orden inverso (O(1) para inserción).
    @tailrec
    def loop(todo: List[Any], acc: List[Int]): List[Int] = todo match {
      // Caso base: no hay más elementos por procesar. Invertimos el acumulador al final.
      case Nil => 
        acc.reverse
        
      // Si el primer elemento es un Int, lo agregamos al acumulador y seguimos iterando.
      case (n: Int) :: tail => 
        loop(tail, n :: acc)
        
      // Si el primer elemento es una sublista, la concatenamos al frente de las 
      // tareas pendientes ('tail') y continuamos, aplanando un nivel a la vez.
      case (list: List[_]) :: tail => 
        loop(list ::: tail, acc)
        
      // Ignoramos de forma segura elementos nulos o de tipos no soportados
      case _ :: tail => 
        loop(tail, acc)
    }

    // Inicializamos el ciclo con la lista completa y un acumulador vacío
    loop(nestedList, Nil)
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

    // 3. Caso extremo: anidamiento profundo asimétrico con listas vacías en el medio
    val nested3 = List(1, List(), List(List(List(3))))
    val expected3 = List(1, 3)
    assert(flatten(nested3) == expected3, "Error: Falló al procesar listas vacías internas")

    println("Todos los tests funcionales en Scala 2.13 pasaron correctamente.")
  }
}