// CARACTERISTICAS DE SCALA 3.6 UTILIZADAS Y POR QUE:
// 1. Sintaxis de indentación significativa (Braceless Syntax): En Scala 3.6, omitir las llaves {} es el estándar absoluto. Reduce la carga cognitiva y hace que el código luzca limpio y declarativo (similar a Python o Haskell).
// 2. Top-Level Definitions (Definiciones de nivel superior): Ya no es necesario envolver métodos sueltos en un `object` (como en Scala 2.x). Las funciones pueden existir libres en la raíz del archivo.
// 3. Punto de entrada @main: Reemplaza la antigua y verbosa firma `def main(args: Array[String]): Unit`.
// 4. Pattern Matching moderno (match/case): Se utiliza para evaluar la comparación matemática en lugar de encadenar múltiples `if/else`, mejorando la legibilidad.
// 5. Recursividad de Cola (@tailrec): Aceptando el "desafío recursivo" que menciona el comentario del código base original, eliminamos por completo el bucle imperativo (`while`), el estado mutable (`var`) y la palabra clave `return` (cuya deprecación se hizo estricta en las versiones recientes de Scala 3).

import scala.annotation.tailrec

// La función principal se define libremente en el archivo (Top-Level)
def binarySearch(arr: Array[Int], target: Int): Int =
  
  // Función auxiliar anidada recursiva para mantener el estado de forma inmutable
  @tailrec
  def search(left: Int, right: Int): Int =
    // Sintaxis condicional moderna: if ... then
    if left > right then
      -1
    else
      val mid = left + (right - left) / 2
      
      // Pattern Matching funcional: el flujo se decide según el resultado de la comparación
      Integer.compare(arr(mid), target) match
        case 0  => mid                      // Encontrado
        case -1 => search(mid + 1, right)   // El valor en mid es menor, buscar en la derecha
        case 1  => search(left, mid - 1)    // El valor en mid es mayor, buscar en la izquierda

  // Invocación inicial
  search(0, arr.length - 1)


// El punto de entrada se anota simplemente con @main
@main def runTests(): Unit =
  assert(binarySearch(Array(1, 3, 5, 7, 9), 5) == 2)
  assert(binarySearch(Array(1, 3, 5, 7, 9), 4) == -1)
  assert(binarySearch(Array.empty[Int], 1) == -1)
  
  println("All tests passed")


/*
EXPLICACION DE CAMBIOS E IDIOMATISMOS EN SCALA 3.6:

1. Resolución del "Desafío Recursivo" y eliminación de `return`:
   En el código de Scala 2.13 base, se utilizaba un bucle `while` y un `return mid` para una salida temprana. En el ecosistema moderno de Scala 3, el uso explícito de `return` está fuertemente desaconsejado porque rompe la transparencia referencial funcional. La forma idiomática de refactorizar esto es usar la inmutabilidad y `@tailrec`, donde el compilador traduce la recursividad internamente a un bucle `while` ultrarrápido en el bytecode.

2. Sintaxis Braceless estricta:
   Scala 3.6 promueve vigorosamente la escritura sin llaves. Estructuras enteras como el `def`, el `if/then` y el bloque `match` ahora dependen de la sangría (indentación). Esto elimina el "ruido visual" presente en la versión 2.13.

3. Eliminación del patrón Singleton (`object P02BinarySearch_v213`):
   Las limitaciones de la JVM requerían antiguamente que el método `main` y las funciones utilitarias vivieran dentro de un objeto Singleton. Scala 3.x abstrae esto; al usar `@main` y Top-Level definitions, el compilador genera automáticamente las clases contenedoras subyacentes, permitiéndote escribir scripts directos enfocados puramente en el algoritmo.
*/