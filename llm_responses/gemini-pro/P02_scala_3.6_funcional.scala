import scala.annotation.tailrec

/**
 * Busca un valor objetivo en un arreglo de enteros ordenado ascendentemente.
 * Retorna el índice del objetivo si se encuentra, o -1 en caso contrario.
 * 
 * Implementación estrictamente funcional en Scala 3.6.
 */
def binarySearch(arr: Array[Int], target: Int): Int =
  
  // Función pura anidada: el estado (left, right) solo se actualiza 
  // pasando nuevos valores inmutables en la llamada recursiva.
  @tailrec
  def search(left: Int, right: Int): Int =
    // Sintaxis moderna braceless (sin llaves ni paréntesis obligatorios)
    if left > right then
      -1
    else
      // val garantiza la inmutabilidad local
      val mid = left + (right - left) / 2
      
      // Pattern Matching funcional: Evaluamos la comparación y mapeamos el 
      // resultado directamente a la siguiente expresión, sin control de flujo imperativo.
      Integer.compare(arr(mid), target) match
        case 0  => mid                      // Encontrado: retorna inmutablemente el índice
        case -1 => search(mid + 1, right)   // Menor: genera nueva llamada para la derecha
        case 1  => search(left, mid - 1)    // Mayor: genera nueva llamada para la izquierda

  // Invocación inicial
  search(0, arr.length - 1)


// ==========================================
// CASOS DE PRUEBA
// ==========================================

// En Scala 3.6, la directiva @main a nivel superior (Top-Level) es el estándar 
// absoluto para crear ejecutables limpios sin necesidad de envolverlos en un object.
@main def testBinarySearchFunctional36(): Unit =

  // 1. Caso base del ejemplo: el elemento existe y debe retornar su índice
  val arr1 = Array(1, 3, 5, 7, 9)
  assert(binarySearch(arr1, 5) == 2, "Error: El objetivo 5 debería estar en el índice 2")

  // 2. Caso del ejemplo: el elemento no existe y debe retornar -1
  val arr2 = Array(1, 3, 5, 7, 9)
  assert(binarySearch(arr2, 4) == -1, "Error: El objetivo 4 no está, debería retornar -1")

  // 3. Caso límite: el arreglo está vacío
  val arr3 = Array.empty[Int]
  assert(binarySearch(arr3, 5) == -1, "Error: Arreglo vacío debería retornar -1")

  println("Todos los tests funcionales en Scala 3.6 pasaron correctamente.")