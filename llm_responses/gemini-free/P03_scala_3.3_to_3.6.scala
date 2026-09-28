// Características de Scala 3.6 utilizadas en este refactor:
// 1. Sintaxis basada en sangría (Optional Braces / Quiet Syntax):
//    Elimina llaves `{}` en favor de la estructura por identación idiomática de Scala 3.
// 2. Definiciones Top-Level con la anotación `@main`:
//    Reemplaza la estructura envolvente de `object P03...` por una función de nivel superior directamente ejecutable.
// 3. Eliminación de `mutable.Stack` a favor de `mutable.ArrayDeque`:
//    `mutable.Stack` está obsoleto (deprecated). `ArrayDeque` con `append` y `removeLast` provee el rendimiento
//    óptimo y moderno para estructuras de tipo pila en la JVM.
// 4. Wildcard moderno `?` en Pattern Matching:
//    Utiliza la sintaxis estándar de Scala 3 (`List[?]`) para comodines de tipo existencial, reemplazando la
//    notación antigua `List[_]`.

import scala.collection.mutable

def flatten(nested: Any): List[Int] =
  val result = mutable.ListBuffer[Int]()
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
// 1. Migración de `mutable.Stack` a `mutable.ArrayDeque`:
//    La implementación `mutable.Stack` legacy fue deprecada por problemas de diseño y rendimiento;
//    `ArrayDeque` es la colección mutable estándar recomendada para operar como pila FIFO/LIFO en la biblioteca estándar de Scala 3.x.
// 2. Eliminación de boilerplate de objetos (`object`):
//    Gracias a las definiciones top-level de Scala 3, no es necesario envolver la función o los tests dentro de un `object P03...`.
// 3. Sintaxis Limpia (Quiet Syntax):
//    Se removieron los paréntesis del bucle `while` y las llaves en los bloques de control `while ... do` y `match`.
// 4. Pattern Matching seguro con `List[?]`:
//    En Scala 3, el uso de `?` es la sintaxis explícita e idiómática para comodines de tipos de datos, dejando el guión bajo `_` reservado para descartes o asignaciones.