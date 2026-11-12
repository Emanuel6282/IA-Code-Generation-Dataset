import scala.util.boundary
import scala.util.boundary.break

/**
 * Busca un valor objetivo en un arreglo de enteros ordenado ascendentemente.
 * Retorna el índice del objetivo si se encuentra, o -1 en caso contrario.
 * 
 * Implementación estrictamente iterativa en Scala 3.6.
 */
def binarySearch(arr: Array[Int], target: Int): Int =
  // boundary: define un bloque del cual podemos "escapar" tempranamente
  boundary:
    // Uso explícito de 'var' para mantener estado mutable (Paradigma Iterativo)
    var left = 0
    var right = arr.length - 1

    // Sintaxis moderna 'while ... do' sin llaves
    while left <= right do
      val mid = left + (right - left) / 2
      
      // Condicionales 'if ... then ... else' sin paréntesis
      if arr(mid) == target then
        break(mid) // Salida temprana moderna. Rompe el boundary retornando 'mid'
      else if arr(mid) < target then
        left = mid + 1 // Mutación de la variable
      else
        right = mid - 1 // Mutación de la variable
        
    // Si el bucle termina sin haber ejecutado el 'break', retornamos -1
    -1


// ==========================================
// CASOS DE PRUEBA
// ==========================================

// Scala 3 usa @main para definir el punto de entrada a nivel superior (Top-Level)
@main def testBinarySearchIterative36(): Unit =

  // 1. Caso base del ejemplo: el elemento existe y debe retornar su índice
  val arr1 = Array(1, 3, 5, 7, 9)
  assert(binarySearch(arr1, 5) == 2, "Error: El objetivo 5 debería estar en el índice 2")

  // 2. Caso del ejemplo: el elemento no existe y debe retornar -1
  val arr2 = Array(1, 3, 5, 7, 9)
  assert(binarySearch(arr2, 4) == -1, "Error: El objetivo 4 no está, debería retornar -1")

  // 3. Caso límite: el arreglo está vacío
  val arr3 = Array.empty[Int]
  assert(binarySearch(arr3, 5) == -1, "Error: Arreglo vacío debería retornar -1")

  println("Todos los tests iterativos en Scala 3.6 pasaron correctamente.")