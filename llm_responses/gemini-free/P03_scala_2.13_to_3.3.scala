// Características de Scala 3.3 (LTS) utilizadas en este refactor:
// 1. Sintaxis basada en sangría / Braces Opcionales:
//    Elimina llaves `{}` redundantes para definir objetos, métodos y estructuras de control,
//    dando un estilo más limpio e idiómático en Scala 3.
// 2. Anotación @main:
//    Reemplaza la declaración tradicional de `main(args: Array[String]): Unit` dentro de un objeto
//    por una función de punto de entrada limpia y de nivel superior.
// 3. Sustitución de `mutable.Stack` por `mutable.ArrayDeque`:
//    En Scala 2.13+, `mutable.Stack` fue deprecado en favor de `ArrayDeque`, que ofrece un mejor
//    rendimiento en memoria cache y velocidad constante O(1) para operaciones de pila (push/pop).
// 4. Wildcard de tipos mejorado (`?` en lugar de `_`):
//    Utiliza la sintaxis estándar de Scala 3 (`List[?]`) para tipos no especificados (existenciales),
//    alineándose con la especificación moderna del compilador.

import scala.collection.mutable

def flatten(nested: Any): List[Int] =
  val result = mutable.ListBuffer[Int]()
  // ArrayDeque es la colección recomendada para comportamiento de pila en Scala 2.13/3.x
  val stack = mutable.ArrayDeque[Any](nested)

  while stack.nonEmpty do
    stack.removeLast() match
      case list: List[?] =>
        // Agregar en orden inverso para preservar el orden original
        list.reverse.foreach(stack.append)
      case n: Int =>
        result += n
      case _ => () // Ignorar otros tipos

  result.toList

@main def run(): Unit =
  assert(flatten(List(1, List(2, 3), List(4, List(5, 6)))) == List(1, 2, 3, 4, 5, 6))
  assert(flatten(List.empty) == List.empty)
  assert(flatten(List(1, 2, 3)) == List(1, 2, 3))
  println("All tests passed")

// --- Explicación de los cambios ---
// 1. Eliminación de `mutable.Stack` por `mutable.ArrayDeque`:
//    La clase `scala.collection.mutable.Stack` está obsoleta (deprecated). `ArrayDeque` con `append`
//    y `removeLast` implementa la semántica de una pila de manera más eficiente en la JVM.
// 2. Adoptada la sintaxis sin llaves (Quiet Syntax):
//    Se removieron las llaves `{}` en el cuerpo de `flatten`, los bucles `while ... do` y la expresión `match`.
// 3. Uso de `@main`:
//    En Scala 3, los puntos de entrada ya no necesitan estar empaquetados obligatoriamente dentro de un `object P03...`.
//    La anotación `@main` permite ejecutar directamente la función `run()`.
// 4. Wildcard de patrón `List[?]`:
//    En Scala 3, `List[?]` sustituye a `List[_]` en el pattern matching para evitar ambigüedades con lambdas y comodines de tipo.