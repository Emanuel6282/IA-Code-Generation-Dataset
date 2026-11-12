// CARACTERISTICAS DE SCALA 3.3 UTILIZADAS Y POR QUE:
// 1. Sintaxis sin llaves (Braceless Syntax / Significant Indentation): Scala 3 permite omitir las llaves {} usando la indentación para delimitar bloques, haciendo el código mucho más limpio y similar a Python.
// 2. Estructuras de control "Quiet" (while ... do): En Scala 3 se elimina la necesidad de poner la condición entre paréntesis y se introduce la palabra clave `do`, logrando un flujo imperativo más legible.
// 3. Método @main: Scala 3 elimina la necesidad de crear un `object` con un `def main(args: Array[String])` o extender `App`. Un simple `@main def nombre()` genera el punto de entrada automáticamente.
// 4. Wildcards modernos (List[?]): En Scala 3, el comodín de tipos existenciales pasó de ser `_` a `?`, por lo que el pattern matching idiomático ahora es `case l: List[?]`.

import scala.collection.mutable.{ArrayDeque, ListBuffer}

/**
 * Toma una lista arbitrariamente anidada (representada como List[Any] en Scala)
 * y retorna una lista plana unidimensional de enteros.
 *
 * Implementación estrictamente iterativa en Scala 3.3: sin recursión, 
 * utilizando un bucle while y colecciones mutables con sintaxis braceless.
 */
def flatten(nestedList: List[Any]): List[Int] =
  // result acumulará los elementos (mutable para alto rendimiento iterativo)
  val result = ListBuffer.empty[Int]
  
  // stack explícito utilizando ArrayDeque, la colección mutable estándar
  // y más eficiente en Scala moderna para operaciones por ambos extremos
  val stack = ArrayDeque.from(nestedList)

  // Bucle while con sintaxis Scala 3 (sin paréntesis, usando 'do')
  while stack.nonEmpty do
    val item = stack.removeHead()
    
    // Pattern matching con sintaxis sin llaves y wildcard '?' de Scala 3
    item match
      case number: Int =>
        result += number
        
      case list: List[?] =>
        // Inyectamos la sublista al inicio del stack para mantener el orden
        stack.prependAll(list)
        
      case _ =>
        // Ignora valores nulos o de otros tipos
        ()

  result.toList


// Punto de entrada generado automáticamente por el compilador de Scala 3
@main def runTests(): Unit =
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

  println("Todos los tests iterativos en Scala 3.3 pasaron correctamente.")