// CARACTERISTICAS DE SCALA 3.6 Y PARADIGMA FUNCIONAL UTILIZADAS:
// 1. Sintaxis Braceless (Significant Indentation): En Scala 3.6, la sintaxis sin llaves es el estándar absoluto de la industria. Mejora la legibilidad del paradigma declarativo, haciéndolo visualmente similar a lenguajes puramente funcionales como Haskell.
// 2. @tailrec (Recursividad de Cola): En la programación funcional pura evitamos mutar estado (sin variables 'var'). Para lograr esto sin desbordar la pila de llamadas (StackOverflow) con anidamientos profundos, utilizamos @tailrec, forzando al compilador de Scala 3.6 a desenrollar la recursión en un bucle iterativo de bajo nivel (bytecode) inmutable y seguro.
// 3. Wildcard Moderno (List[?]): Reemplaza el antiguo comodín `_` de Scala 2 por `?`, proporcionando una semántica de tipos existenciales mucho más precisa y clara en el Pattern Matching.
// 4. Inmutabilidad Absoluta: Todo el flujo se basa en `val`, evaluación de expresiones y construcción de nuevas listas mediante los operadores inmutables `::` (prepend) y `:::` (concat). 

import scala.annotation.tailrec

/**
 * Toma una lista arbitrariamente anidada (representada como List[Any])
 * y retorna una lista plana unidimensional de enteros.
 *
 * Implementación estrictamente funcional en Scala 3.6:
 * Inmutable, basada en recursividad de cola (@tailrec) y pattern matching.
 */
def flatten(nestedList: List[Any]): List[Int] =
  
  // Función recursiva anidada. 
  // 'todo' actúa como una pila inmutable de elementos por procesar.
  // 'acc' acumula los enteros extraídos. Insertar al principio (::) es O(1),
  // por lo que al finalizar la ejecución simplemente invertimos el resultado.
  @tailrec
  def loop(todo: List[Any], acc: List[Int]): List[Int] = todo match
    // Caso base: La lista de tareas está vacía. Invertimos y retornamos el acumulador.
    case Nil => 
      acc.reverse
      
    // Si la cabeza de la lista es un Int, lo agregamos al acumulador.
    case (n: Int) :: tail => 
      loop(tail, n :: acc)
      
    // Si la cabeza es una sublista, la concatenamos frente a los elementos restantes.
    // Usamos el wildcard '?' moderno de Scala 3.
    case (list: List[?]) :: tail => 
      loop(list ::: tail, acc)
      
    // Ignora elementos nulos o de tipos no enteros de forma funcional y segura.
    case _ :: tail => 
      loop(tail, acc)

  // Invocación inicial con la lista completa y un acumulador vacío
  loop(nestedList, Nil)


// Entrypoint moderno generado automáticamente por el compilador de Scala 3.6
@main def runFunctionalTests36(): Unit =
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

  println("Todos los tests funcionales en Scala 3.6 pasaron correctamente.")