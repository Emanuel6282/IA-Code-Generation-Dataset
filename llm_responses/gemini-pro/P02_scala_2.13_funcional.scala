import scala.annotation.tailrec

object BinarySearchFunctional213 {

  /**
   * Busca un valor objetivo en un arreglo de enteros ordenado ascendentemente.
   * Retorna el índice del objetivo si se encuentra, o -1 en caso contrario.
   * 
   * Implementación estrictamente funcional utilizando recursión de cola (@tailrec)
   * e inmutabilidad (solo vals, sin vars ni bucles imperativos).
   */
  def binarySearch(arr: Array[Int], target: Int): Int = {
    
    // Función pura auxiliar anidada para mantener el estado de forma inmutable
    @tailrec
    def search(left: Int, right: Int): Int = {
      // Condición base (de corte): el rango se ha cruzado y no se encontró el valor
      if (left > right) {
        -1
      } else {
        // Cálculo inmutable del punto medio
        val mid = left + (right - left) / 2
        
        // Evaluación como expresión declarativa (sin usar la palabra clave 'return')
        if (arr(mid) == target) {
          mid
        } else if (arr(mid) < target) {
          // El estado avanza inyectando nuevos valores a la llamada recursiva
          search(mid + 1, right)
        } else {
          search(left, mid - 1)
        }
      }
    }

    // Invocación inicial
    search(0, arr.length - 1)
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

    println("Todos los tests funcionales en Scala 2.13 pasaron correctamente.")
  }
}