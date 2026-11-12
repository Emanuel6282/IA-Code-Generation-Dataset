/**
 * Busca un valor objetivo en un arreglo de enteros ordenado ascendentemente.
 * Retorna el índice del objetivo si se encuentra, o -1 en caso contrario.
 * 
 * Implementación estrictamente iterativa en Scala 3.3.
 */
def binarySearch(arr: Array[Int], target: Int): Int =
  // Uso explícito de 'var' para mantener estado mutable, típico del enfoque iterativo
  var left = 0
  var right = arr.length - 1
  var result = -1

  // Bucle while clásico con la sintaxis moderna de Scala 3 (while ... do)
  // Evitamos el uso de 'return' explícito para salir del bucle integrando 
  // la condición de búsqueda en la cabecera del while.
  while left <= right && result == -1 do
    val mid = left + (right - left) / 2
    
    // Estructuras de control condicional de Scala 3 (if ... then)
    if arr(mid) == target then
      result = mid           // Mutamos el resultado para romper la condición del bucle
    else if arr(mid) < target then
      left = mid + 1         // Mutamos el estado desplazando el puntero izquierdo
    else
      right = mid - 1        // Mutamos el estado desplazando el puntero derecho
      
  result


// ==========================================
// CASOS DE PRUEBA
// ==========================================
// En Scala 3, usamos @main como punto de entrada, eliminando la necesidad 
// de crear un 'object' con un 'def main(args: Array[String])'
@main def testBinarySearchIterative33(): Unit =
  
  // 1. Caso base del ejemplo: el elemento existe y debe retornar su índice
  val arr1 = Array(1, 3, 5, 7, 9)
  assert(binarySearch(arr1, 5) == 2, "Error: El objetivo 5 debería estar en el índice 2")

  // 2. Caso del ejemplo: el elemento no existe y debe retornar -1
  val arr2 = Array(1, 3, 5, 7, 9)
  assert(binarySearch(arr2, 4) == -1, "Error: El objetivo 4 no está, debería retornar -1")

  // 3. Caso límite: el arreglo está vacío
  val arr3 = Array.empty[Int]
  assert(binarySearch(arr3, 5) == -1, "Error: Arreglo vacío debería retornar -1")

  println("Todos los tests iterativos en Scala 3.3 pasaron correctamente.")