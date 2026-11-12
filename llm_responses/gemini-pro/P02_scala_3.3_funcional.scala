import scala.annotation.tailrec

/**
 * Busca un valor objetivo en un arreglo de enteros ordenado ascendentemente.
 * Retorna el índice del objetivo si se encuentra, o -1 en caso contrario.
 * 
 * Implementación estrictamente funcional en Scala 3.3 usando recursión de cola.
 */
def binarySearch(arr: Array[Int], target: Int): Int =
  // Función pura anidada. Mantiene el estado inmutable pasando 
  // nuevos valores en cada llamada recursiva.
  @tailrec
  def search(left: Int, right: Int): Int =
    // Sintaxis moderna de Scala 3: if ... then (sin paréntesis obligatorios)
    if left > right then
      -1
    else
      // Cálculo inmutable del punto medio
      val mid = left + (right - left) / 2
      
      if arr(mid) == target then
        mid
      else if arr(mid) < target then
        search(mid + 1, right) // El estado avanza en la pila de forma inmutable
      else
        search(left, mid - 1)

  // Invocación inicial
  search(0, arr.length - 1)


// ==========================================
// CASOS DE PRUEBA
// ==========================================

// En Scala 3, la directiva @main genera automáticamente el código boilerplate 
// necesario para ejecutar la aplicación, reemplazando el tradicional 
// 'def main(args: Array[String])' dentro de un 'object'.
@main def testBinarySearchFunctional33(): Unit =

  // 1. Caso base del ejemplo: el elemento existe (retorna índice 2)
  val arr1 = Array(1, 3, 5, 7, 9)
  assert(binarySearch(arr1, 5) == 2, "Error: El objetivo 5 debería estar en el índice 2")

  // 2. Caso del ejemplo: el elemento no existe (retorna -1)
  val arr2 = Array(1, 3, 5, 7, 9)
  assert(binarySearch(arr2, 4) == -1, "Error: El objetivo 4 no está, debería retornar -1")

  // 3. Caso límite: el arreglo está vacío
  val arr3 = Array.empty[Int]
  assert(binarySearch(arr3, 5) == -1, "Error: Arreglo vacío debería retornar -1")

  println("Todos los tests funcionales en Scala 3.3 pasaron correctamente.")