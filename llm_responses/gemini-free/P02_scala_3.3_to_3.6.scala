// Características de Scala 3.6 utilizadas en este refactor:
// 1. Sintaxis orientada a sangría y llaves opcionales (Quiet Syntax / Optional Braces):
//    Elimina la verbosidad sintáctica de llaves `{}` en funciones y bloques, mejorando la legibilidad.
// 2. Definiciones a nivel superior (Top-level definitions):
//    Permite declarar funciones y ejecutables sin necesidad de envolverlos en un `object` redundante.
// 3. Anotación `@main`:
//    Punto de entrada nativo e idiomático que simplifica la ejecución eliminando `def main(args: Array[String]): Unit`.
// 4. Transformación funcional con `@tailrec` y Pattern Matching:
//    Sustituye la mutabilidad imperativa por una recursión de cola pura garantizando máxima optimización en bytecode.

import scala.annotation.tailrec

// Definición top-level de la función binarySearch
def binarySearch(arr: Array[Int], target: Int): Int =
  @tailrec
  def searchRange(left: Int, right: Int): Int =
    if left > right then -1
    else
      val mid = left + (right - left) / 2
      arr(mid) match
        case `target`        => mid
        case x if x < target => searchRange(mid + 1, right)
        case _               => searchRange(left, mid - 1)

  searchRange(0, arr.length - 1)

// Punto de entrada moderno con la anotación @main en Scala 3.6
@main def run(): Unit =
  assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)
  assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)
  assert(binarySearch(Array.empty[Int], 1) == -1)
  println("All tests passed")

// --- Explicación de los cambios ---
// 1. Eliminación de mutabilidad (Refactor a @tailrec): Se removieron las variables mutables (`var left`, `var right`) 
//    y el bucle `while` imperativo, sustituyéndolos por una función interna recursiva de cola (`@tailrec`). 
//    Esto garantiza inmutabilidad estricta manteniendo rendimiento de tiempo constante $O(1)$ en espacio de pila.
// 2. Transición a Quiet Syntax en Scala 3.6: Se adoptó la sintaxis limpia basada en sangría y el uso de `if/then` 
//    y `match` sin llaves ceremoniales.
// 3. Eliminación del envoltorio `object`: En Scala 3.6 no es necesario declarar un singleton `object` para contener 
//    las funciones o los tests.
// 4. Adaptación del punto de entrada (`@main`): Se sustituyó el método `main` tradicional por un punto de entrada `@main` top-level.