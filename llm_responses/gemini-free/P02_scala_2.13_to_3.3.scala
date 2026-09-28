// Características de Scala 3.3 utilizadas en este refactor:
// 1. Sintaxis basada en sangría / Quiet Syntax (Optional Braces):
//    Elimina llaves `{}` e instrucciones ceremoniales, logrando un código más limpio y legible.
// 2. Definiciones Top-Level (Top-level definitions):
//    Permite declarar funciones directamente en el archivo sin necesidad de envolverlas en un `object`.
// 3. Anotación `@main`:
//    Reemplaza el paradigma tradicional `def main(args: Array[String]): Unit` por un punto de entrada conciso e idiomático.
// 4. Pattern Matching avanzado y sintaxis `if/then`:
//    Estructura el flujo con sintaxis moderna e idiomática de Scala 3.

import scala.annotation.tailrec

// Definición top-level directamente en el archivo
def binarySearch(arr: Array[Int], target: Int): Int =
  // Función auxiliar recursiva de cola para reemplazar el paradigma imperativo
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

// Punto de entrada idiómático con la anotación @main de Scala 3
@main def run(): Unit =
  assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)
  assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)
  assert(binarySearch(Array.empty[Int], 1) == -1)
  println("All tests passed")

// --- Explicación de los cambios ---
// 1. Refactor de Imperativo a Funcional (@tailrec): Se eliminaron las variables mutables (`var left`, `var right`) 
//    y el bucle `while` sustituyéndolos por una función recursiva de cola anotada con `@tailrec`. Esto garantiza 
//    inmutabilidad y rendimiento equivalente a un bucle imperativo a nivel de bytecode (eliminación de llamadas de cola).
// 2. Transición a Quiet Syntax (Scala 3.3): Se reemplazó el uso de llaves `{}` por bloques delimitados mediante sangría, 
//    y se utilizó `then` en condicionales `if/then`.
// 3. Remoción del contenedor `object`: En Scala 3 las funciones no necesitan estar confinadas dentro de un `object` singleton.
// 4. Simplificación del Main (`@main`): Se sustituyó `def main(args: Array[String])` por la anotación `@main`, que es la norma en Scala 3.