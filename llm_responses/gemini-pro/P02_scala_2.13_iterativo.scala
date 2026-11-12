object BinarySearchIterative213 {

  /**
   * Busca un valor objetivo en un arreglo de enteros ordenado ascendentemente.
   * Retorna el índice del objetivo si se encuentra, o -1 en caso contrario.
   * 
   * Implementación estrictamente iterativa usando variables mutables y bucle while.
   */
  def binarySearch(arr: Array[Int], target: Int): Int = {
    // Definimos los punteros como variables mutables (var)
    var left = 0
    var right = arr.length - 1

    // Bucle iterativo clásico
    while (left <= right) {
      // Cálculo del punto medio previniendo overflow de enteros
      val mid = left + (right - left) / 2
      
      if (arr(mid) == target) {
        return mid // Elemento encontrado, rompemos el flujo con un return temprano
      } else if (arr(mid) < target) {
        left = mid + 1 // Mutamos el estado desplazando el puntero izquierdo
      } else {
        right = mid - 1 // Mutamos el estado desplazando el puntero derecho
      }
    }

    -1 // Si el bucle termina, el elemento no existe
  }

  def main(args: Array[String]): Unit = {
    // ==========================================
    // CASOS DE PRUEBA
    // ==========================================
    
    // 1. Caso base del ejemplo: el elemento existe y debe retornar su índice
    val arr1 = Array(1, 3, 5, 7, 9)
    assert(binarySearch(arr1, 5) == 2, "Error: El objetivo 5 debería estar en el índice 2")

    // 2. Caso del ejemplo: el elemento no existe y debe retornar -1
    val arr2 = Array(1, 3, 5, 7, 9)
    assert(binarySearch(arr2, 4) == -1, "Error: El objetivo 4 no está, debería retornar -1")

    // 3. Caso límite: el arreglo está vacío
    val arr3 = Array.empty[Int]
    assert(binarySearch(arr3, 5) == -1, "Error: Arreglo vacío debería retornar -1")

    println("Todos los tests iterativos en Scala 2.13 pasaron correctamente.")
  }
}